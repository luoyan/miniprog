package com.luoyan.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class WordCount {
	public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        	System.out.println("map key " + key.toString() + " value " + value.toString());
        	int args1 = context.getConfiguration().getInt("args1", -1);
        	String args2 = context.getConfiguration().get("args2");
        	System.out.println("args1 " + args1 + " args2 " + args2);
        	String[] tokens = value.toString().split(" ");
        	for (String token : tokens) {
        		context.write(new Text(token),
                    new IntWritable(1));
        	}
        }
	}
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        	int sum = 0;
        	String values_string = "";
        	for (IntWritable value : values) {
        		sum += value.get();
        		values_string = values_string + " " + value.get();
        	}
        	
        	System.out.println("reduce key " + key.toString() + " values [" + values_string + "]");
        	context.write(new Text(key),
                    new IntWritable(sum));
        }
    }
    private static void usage() {
        System.out.println("usage : command fromFile toFile reduceNum");
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        GenericOptionsParser genericOptionsParser = new GenericOptionsParser(configuration, args);
        String[] otherArgs = genericOptionsParser.getRemainingArgs();
        configuration = genericOptionsParser.getConfiguration();
        System.out.println("otherArgs.length " + otherArgs.length); 
        if (otherArgs.length < 3) {
            usage();
            return;
        }
        String fromFile = otherArgs[0];
        String toFile = otherArgs[1];
        int reduceNum = Integer.parseInt(otherArgs[2]);
        
        configuration.setInt("args1", 1);
        configuration.set("args2", "string2");

        Job job = Job.getInstance(configuration, "word_count");
        job.setNumReduceTasks(reduceNum);
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //job.setInputFormatClass(SequenceFileInputFormat.class);
        //HDFSHelper.addInputPath(job, fromFile);
        job.setInputFormatClass(TextInputFormat.class);
        //job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(job, new Path(fromFile));
        Path outputPath = new Path(toFile);
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        FileOutputFormat.setOutputPath(job, new Path(toFile));

        job.waitForCompletion(true);
    }
}
