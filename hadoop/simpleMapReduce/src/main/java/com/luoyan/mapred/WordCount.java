package com.luoyan.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class WordCount {
	public static class WordCountMapper extends Mapper<Object, BytesWritable, Text, Text> {
        @Override
        public void map(Object key, BytesWritable value, Context context) throws IOException, InterruptedException {
        	System.out.println("map key " + key.toString() + " value " + value.toString());
        }
	}
    public static class WordCountReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        	System.out.println("reduce key " + key.toString() + " values " + values.toString());
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
        System.out.println("args.length " + args.length); 
        if (args.length < 3) {
            usage();
            return;
        }
        String fromFile = otherArgs[0];
        String toFile = otherArgs[1];
        int reduceNum = Integer.parseInt(otherArgs[2]);

        Job job = Job.getInstance(configuration, "word_count");
        job.setNumReduceTasks(reduceNum);
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //job.setInputFormatClass(SequenceFileInputFormat.class);
        //HDFSHelper.addInputPath(job, fromFile);
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
