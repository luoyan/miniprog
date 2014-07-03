package com.luoyan.mapred;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.io.BytesWritable;

public class ConvertKafka {
	public static class ConvertKafkaMapper extends Mapper<Object, BytesWritable, BytesWritable, BytesWritable> {
        @Override
        public void map(Object key, BytesWritable value, Context context) throws IOException, InterruptedException {
        	String rawLine = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
        	String[] tokens = rawLine.split("\t");
        	if (tokens.length != 3) {
        		context.getCounter("ERROR", "invalid input").increment(1);
        		return;
        	}
        	String content = new String(Base64.decodeBase64(tokens[2]));
        	System.out.println("content : [" + content + "]");
        	context.write(new BytesWritable(), new BytesWritable(content.getBytes()));
        	context.getCounter("INFO", "valid input").increment(1);
        }
	}
    public static class ConvertKafkaReducer extends Reducer<BytesWritable, BytesWritable, BytesWritable, BytesWritable> {
        @Override
        public void reduce(BytesWritable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {

        }
    }
    private static void usage() {
        System.out.println("usage : command fromFiles=dir1:dir2 toFile");
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        GenericOptionsParser genericOptionsParser = new GenericOptionsParser(configuration, args);
        String[] otherArgs = genericOptionsParser.getRemainingArgs();
        configuration = genericOptionsParser.getConfiguration();
        System.out.println("otherArgs.length " + otherArgs.length); 
        if (otherArgs.length < 2) {
        	System.out.println("otherArgs.length < 2");
            usage();
            return;
        }
        String fromFiles = otherArgs[0];
        String toFile = otherArgs[1];

        Job job = Job.getInstance(configuration, "convert_kafka");
        job.setNumReduceTasks(0);
        job.setJarByClass(ConvertKafka.class);
        job.setMapperClass(ConvertKafkaMapper.class);
        job.setReducerClass(ConvertKafkaReducer.class);
        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(BytesWritable.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        for (String file : fromFiles.split(":")) {
        	System.out.println("input file : " + file);
        	FileInputFormat.addInputPath(job, new Path(file));
        }
        Path outputPath = new Path(toFile);
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        FileOutputFormat.setOutputPath(job, new Path(toFile));

        job.waitForCompletion(true);
    }
}
