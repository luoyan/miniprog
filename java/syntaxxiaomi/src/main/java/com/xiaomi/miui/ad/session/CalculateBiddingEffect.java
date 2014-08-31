package com.xiaomi.miui.ad.session;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.thrift.TBase;

import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogBiddingEffectRecord;
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;

public class CalculateBiddingEffect {

	public static class CalculateBiddingEffectMapper extends Mapper<Object, BytesWritable, Text, LongWritable> {
		

		public static TBase parseBiddingEffectRecord(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
			if (items.length != 5)
				return null;
	    	MiuiAdStoreServiceLogBiddingEffectRecord miuiAdStoreServiceLogBiddingEffectRecord = new MiuiAdStoreServiceLogBiddingEffectRecord();
	    	miuiAdStoreServiceLogBiddingEffectRecord.setScribeInfo(miuiLogScribeInfo);
	    	miuiAdStoreServiceLogBiddingEffectRecord.setLogType(items[0]);
	    	miuiAdStoreServiceLogBiddingEffectRecord.setPackageName(items[1]);
	    	miuiAdStoreServiceLogBiddingEffectRecord.setDownloadNo(Long.parseLong(items[2]));
	    	miuiAdStoreServiceLogBiddingEffectRecord.setPrice(Long.parseLong(items[3]));
	    	miuiAdStoreServiceLogBiddingEffectRecord.setConsumption(Long.parseLong(items[4]));
			return miuiAdStoreServiceLogBiddingEffectRecord;
		}
		
        @Override
        public void map(Object key, BytesWritable value, Context context) throws IOException, InterruptedException {
        	String rawLine = new String(value.getBytes(), 0, value.getLength(), "UTF-8");
        	String[] tokens = rawLine.split("\t");
        	if (tokens.length != 3) {
        		context.getCounter("ERROR", "invalid input").increment(1);
        		return;
        	}

			String scribeInfo = tokens[0];
			String time = tokens[1];
			MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
			miuiLogScribeInfo.setScribeInfo(scribeInfo);
			miuiLogScribeInfo.setTime(time);
			
        	String content = new String(Base64.decodeBase64(tokens[2]));
        	System.out.println("content : [" + content + "]");
        	if (content.startsWith("{")) {
                JSONObject jsonObject = JSONObject.fromObject(content);
                String logType = jsonObject.getString("log_type");
                if (logType.equals("algorithm_download_detail")) {
                	long consumption = Long.parseLong(jsonObject.getString("consumption"));
					context.write(
							new Text("algorithm_download_detail_consumption_byjson"),
							new LongWritable(consumption));
                }
        		return;
        		
        	}
        	else {
				String[] items = content.split("\t");
				String logType = items[0];
				if (items.length != 5)
					return;
				if (logType.equals("bidding_effect_record")) {
					MiuiAdStoreServiceLogBiddingEffectRecord thriftObject = (MiuiAdStoreServiceLogBiddingEffectRecord) parseBiddingEffectRecord(
							miuiLogScribeInfo, items);
					long consumption = thriftObject.getConsumption();
					context.write(
							new Text("bidding_effect_record_consumption"),
							new LongWritable(consumption));
				}
        	}
        	//context.getCounter("INFO", "valid input").increment(1);
        }
	}
    public static class CalculateBiddingEffectReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        	long sum = 0;
        	for (LongWritable value : values) {
        		sum += value.get();
        	}
        	context.write(new Text(key),
                    new LongWritable(sum));
        }
    }
    

    private static void usage() {
        System.out.println("usage : command fromFile toFile");
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
        String fromFile = otherArgs[0];
        String toFile = otherArgs[1];

        Job job = Job.getInstance(configuration, "CalculateBiddingEffect");
        job.setNumReduceTasks(1);
        job.setJarByClass(CalculateBiddingEffect.class);
        job.setMapperClass(CalculateBiddingEffectMapper.class);
        job.setReducerClass(CalculateBiddingEffectReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
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
