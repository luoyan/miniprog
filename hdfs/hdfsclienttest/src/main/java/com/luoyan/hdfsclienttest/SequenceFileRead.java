package com.luoyan.hdfsclienttest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.BytesWritable;

import java.io.IOException;

public class SequenceFileRead {
	public void read(Configuration conf, FileSystem fs, Path path) throws IOException {
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);

		BytesWritable key = new BytesWritable();
		BytesWritable val = new BytesWritable();

		while (reader.next(key, val)) {
			System.out.println("key : " + key.getBytes().length + " : [" + new String(key.getBytes()) + "]\t" + " value : " + val.getBytes().length + " : [" + new String(val.getBytes()) + "]");
		}

		reader.close();
	}
	public void ReadLocal(String path) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		Path seqFilePath = new Path(path);
		read(conf, fs, seqFilePath);
	}
	public static void main(String[] args) throws IOException {
		SequenceFileRead sfr = new SequenceFileRead();
		if (args.length < 1) {
			System.err.println("command path(file key=BytesWritable value=BytesWritable)");
			System.exit(-1);
		}
		String path = args[0];
		sfr.ReadLocal(path);
	}

}
