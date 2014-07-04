package com.luoyan.hdfsclienttest;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.hadoop.io.Text;


public class SequenceFileReader {
    //private static final Logger LOGGER = LoggerFactory.getLogger("SequenceFileReader");
	public void read(Configuration conf, FileSystem fs, Path path, int recordNum) throws IOException {
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);

		BytesWritable key = new BytesWritable();
		BytesWritable val = new BytesWritable();
		//LOGGER.debug("start to read");
		int count = 0;
		while (reader.next(key, val)) {
			if (recordNum != 0 && count >= recordNum) {
				break;
			}
			count += 1;
			System.out.println(count + " key : [" + new String(key.getBytes(), 0, key.getLength(), "UTF-8") + "]\t"
			+ " value : [" + new String(val.getBytes(), 0, val.getLength(), "UTF-8") + "]");
			if ((count % 1000) == 0) {
				//LOGGER.debug("read " + count + "records");
			}
		}
		System.err.println("end to read total " + count);
		//LOGGER.debug("end to read total " + count);

		reader.close();
	}
	
	public void read2(Configuration conf, FileSystem fs, Path path, int recordNum) throws IOException {
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);

		BytesWritable key = new BytesWritable();
		IntWritable val = new IntWritable();
		//LOGGER.debug("start to read");
		int count = 0;
		while (reader.next(key, val)) {
			if (recordNum != 0 && count >= recordNum) {
				break;
			}
			count += 1;
			System.out.println(count + " key : [" + (new String(key.getBytes(), 0, key.getLength(), "UTF-8")).trim() + "]\t"
			+ " value : [" + val + "]");
			if ((count % 1000) == 0) {
				//LOGGER.debug("read " + count + "records");
			}
		}
		System.err.println("end to read total " + count);
		//LOGGER.debug("end to read total " + count);

		reader.close();
	}
	
	public void readAndWrite(Configuration conf, FileSystem fs, Path path, int recordNum) throws IOException {
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
		Path writePath = new Path(path + ".part.copy");
		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, writePath, BytesWritable.class, BytesWritable.class, org.apache.hadoop.io.SequenceFile.CompressionType.BLOCK);

		BytesWritable key = new BytesWritable();
		BytesWritable val = new BytesWritable();
		//LOGGER.debug("start to read");
		int count = 0;
		while (reader.next(key, val)) {
			if (recordNum != 0 && count >= recordNum) {
				break;
			}
			count += 1;
			System.out.println(count + " key : [" + new String(key.getBytes(), 0, key.getLength(), "UTF-8") + "]\t"
			+ " value : [" + new String(val.getBytes(), 0, val.getLength(), "UTF-8") + "]");
			if ((count % 1000) == 0) {
				//LOGGER.debug("read " + count + "records");
			}
			writer.append(new BytesWritable(key.getBytes()), new BytesWritable(val.getBytes()));
		}
		System.err.println("end to read total " + count);
		//LOGGER.debug("end to read total " + count);

		reader.close();
		writer.close();
	}
	public void ReadLocal(String path, int recordNum, int type) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		Path seqFilePath = new Path(path);
		if (type == 1)
			read(conf, fs, seqFilePath, recordNum);
		else if (type == 2)
			read2(conf, fs, seqFilePath, recordNum);
	}
	public void ReadAndWriteLocal(String path, int recordNum) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		Path seqFilePath = new Path(path);
		readAndWrite(conf, fs, seqFilePath, recordNum);
	}
	public static void usage() {
		System.err.println("command read/read2/readAndWrite path(file key=BytesWritable value=BytesWritable) recordNum=0");
		System.err.println("command read key=BytesWritable value=BytesWritable");
		System.err.println("command read2 key=BytesWritable value=IntWritable");
	}
	public static void main(String[] args) throws IOException {
		SequenceFileReader reader = new SequenceFileReader();
		System.out.println("args.length " + args.length);
		if (args.length < 3) {
			usage();
			System.exit(-1);
		}
		String command = args[0];
		String path = args[1];
		int recordNum = Integer.parseInt(args[2]);
		if (command.equals("read")) {
			reader.ReadLocal(path, recordNum, 1);
		}
		else if (command.equals("read2")) {
			reader.ReadLocal(path, recordNum, 2);
		}
		else if (command.equals("readAndWrite")) {
			reader.ReadAndWriteLocal(path, recordNum);
		}
		else {
			usage();
			System.exit(-1);
		}
	}
}
