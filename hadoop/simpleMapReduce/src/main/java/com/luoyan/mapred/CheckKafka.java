package com.luoyan.mapred;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.io.BytesWritable;

public class CheckKafka {
	public static class CheckKafkaMapper extends Mapper<Object, BytesWritable, BytesWritable, IntWritable> {
        @Override
        public void map(Object key, BytesWritable value, Context context) throws IOException, InterruptedException {
        	String rawLine = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
        	String[] tokens = rawLine.split("\t");
        	if (tokens.length != 3) {
        		context.getCounter("WARNING", "input token length " + tokens.length).increment(1);
        		return;
        	}
        	if (!tokens[0].equals("APP_STORE")) {
        		context.getCounter("WARNING", "input token not APP_STORE " + tokens[0]).increment(1);
        		return;	
        	}
        	context.getCounter("INFO", "input token APP_STORE").increment(1);
			String time_str = tokens[2].trim();
        	try {
				long start_time = context.getConfiguration().getLong(
						"start_time", 0);
				long end_time = context.getConfiguration().getLong("end_time",
						0);
				//System.out.println("parseLong [" + time_str + "]");
				long time = Long.parseLong(time_str);
				//System.out.println("start_time " + start_time + " end_time "
				//		+ end_time);
				if (time < start_time || time >= end_time) {
					context.getCounter("WARNING", "input out of day")
							.increment(1);
					return;
				}
				//System.out.println("map value [" + rawLine + "]");
	        	context.write(new BytesWritable(rawLine.trim().getBytes()),
	                    new IntWritable(1));
	        	context.getCounter("INFO", "valid map records").increment(1);
        	} catch (NumberFormatException e) {
        		//System.err.println("")
        		e.printStackTrace();
        		System.out.println("failed to parseLong[" + time_str + "] tokens [" + rawLine + "]");
        		System.out.println("length " + time_str.length() + " digit list for number [" + displayCharValues(tokens[2]) + "]");
        		context.getCounter("ERROR", "invalid timestamp").increment(1);
        	}
        }
        public static String displayCharValues(String s) {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                sb.append((int) c).append(",");
            }
            return sb.toString();
        }
	}
    public static class CheckKafkaReducer extends Reducer<BytesWritable, IntWritable, BytesWritable, IntWritable> {
        @Override
        public void reduce(BytesWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        	int sum = 0;
        	String values_string = "";
        	for (IntWritable value : values) {
        		sum += value.get();
        		values_string = values_string + "][" + value.get();
        	}
        	String rawKey = (new String(key.getBytes(), 0, key.getLength(), "UTF-8")).trim();
        	System.out.println("reduce key " + rawKey + " values [" + values_string + "]");
        	if (sum == 2) {
        		context.getCounter("INFO", "same reduce records").increment(sum);
        		return;
        	}
        	System.out.println("diff reduce key [" + rawKey + "] sum = " + sum);
        	context.getCounter("ERROR", "different reduce records sum = " + sum).increment(sum);
        	context.write(new BytesWritable(rawKey.getBytes()),
                    new IntWritable(sum));
        }
    }
    private static void usage() {
        System.out.println("usage : command fromFiles=dir1:dir2 toFile=dir start_time end_time");
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        GenericOptionsParser genericOptionsParser = new GenericOptionsParser(configuration, args);
        String[] otherArgs = genericOptionsParser.getRemainingArgs();
        configuration = genericOptionsParser.getConfiguration();
        System.out.println("otherArgs.length " + otherArgs.length); 
        if (otherArgs.length < 4) {
            usage();
            return;
        }
        String fromFiles = otherArgs[0];
        String toFile = otherArgs[1];
        //int reduceNum = Integer.parseInt(otherArgs[2]);
        long start_time = Long.parseLong(otherArgs[2]);
        long end_time = Long.parseLong(otherArgs[3]);
        configuration.setLong("start_time", start_time);
        configuration.setLong("end_time", end_time);

        Job job = Job.getInstance(configuration, "check_kafka");
        //job.setNumReduceTasks(reduceNum);
        job.setJarByClass(CheckKafka.class);
        job.setMapperClass(CheckKafkaMapper.class);
        job.setReducerClass(CheckKafkaReducer.class);
        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(BytesWritable.class);
        job.setOutputValueClass(IntWritable.class);

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
