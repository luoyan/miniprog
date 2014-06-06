package com.luoyan.hdfsclienttest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.net.URI;

public class HdfsWriterTest {
    private static Properties extraProperties = PropertiesHelper.getProperties();
    private Configuration conf = new Configuration();
    private OutputStream logStream;
    private OutputStream createHdfsFile(String fileName) throws IOException {
    	FileSystem hdfs = FileSystem.get(URI.create(fileName), this.conf);
        Path path = new Path(fileName);
        OutputStream output = hdfs.create(path);
        return output;
    }
    
    private void writeHdfsFile(OutputStream stream, byte[] bytes) throws IOException {
    	stream.write(bytes);
    }
    
    private void writeHdfsLog(String s) throws IOException {
    	writeHdfsFile(this.logStream, s.getBytes());
    }
    //list all files
    public void listFiles(String dirName) throws IOException {
    		FileSystem hdfs = FileSystem.get(URI.create(dirName), this.conf);
            Path f = new Path(dirName);
            FileStatus[] status = hdfs.listStatus(f);
            System.out.println(dirName + " has all files:");
            for (int i = 0; i< status.length; i++) {
                    System.out.println(status[i].getPath().toString());
            }
    }
    
	public static void main(String[] args) {
		HdfsWriterTest h = new HdfsWriterTest();
		try {
			//h.listFiles("hdfs:///");
			h.listFiles("hdfs://namenode:9000/");
			h.listFiles("hdfs://namenode:9000/user");
			h.listFiles("hdfs://namenode:9000/user/test");
			h.logStream = h.createHdfsFile("hdfs://namenode:9000/user/h_miui/luoyan/miui_ad_storm_app_store.log");
            if (h.logStream == null) {
                System.out.println("h.logStream is null");
            }
            else
			    h.writeHdfsLog("helloworld");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
