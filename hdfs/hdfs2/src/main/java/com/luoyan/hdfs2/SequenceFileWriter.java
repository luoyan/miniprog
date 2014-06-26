package com.luoyan.hdfs2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.net.URI;

public class SequenceFileWriter {
	private void writeAndRead(Configuration conf, FileSystem fs, Path seqFilePath) throws IOException {
		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, seqFilePath, Text.class, IntWritable.class, org.apache.hadoop.io.SequenceFile.CompressionType.BLOCK);
		//SequenceFile.Writer writer = SequenceFile.createWriter(conf, Writer.file(seqFilePath), Writer.keyClass(Text.class),
		//		Writer.valueClass(IntWritable.class));

		writer.append(new Text("key1"), new IntWritable(1));
		writer.append(new Text("key2"), new IntWritable(2));

		writer.close();

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, seqFilePath, conf);

		Text key = new Text();
		IntWritable val = new IntWritable();

		while (reader.next(key, val)) {
			System.err.println(key + "\t" + val);
		}

		reader.close();
		
	}
	public void testSeqFileReadWrite(String path) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);
		Path seqFilePath = new Path(path);
		writeAndRead(conf, fs, seqFilePath);
	}
	public void testSeqFileReadWriteHdfs(String path) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(path), conf);
		Path seqFilePath = new Path(path);
		writeAndRead(conf, fs, seqFilePath);
	}
	public static void main(String[] args) throws IOException {
		SequenceFileWriter writer = new SequenceFileWriter();
		writer.testSeqFileReadWrite("fileblock.seq");
		writer.testSeqFileReadWriteHdfs("hdfs://namenode:9000/user/test/fileblock2.seq");
	}
}
