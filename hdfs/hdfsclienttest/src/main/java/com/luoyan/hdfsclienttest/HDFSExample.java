package com.luoyan.hdfsclienttest;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.conf.Configuration;

public class HDFSExample {
  public static void main(String[] args) throws IOException {
    String localSrc = args[0];
    String dst = args[1];

    InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(URI.create(dst), conf);

    OutputStream out = fs.create(new Path(dst), new Progressable() {
        public void progress() {
        System.out.print(".");
        }  
    });

    byte[] buffer = new byte[4096];
    int bytesRead = in.read(buffer);
    while (bytesRead > 0) {
      out.write(buffer, 0, bytesRead);
      bytesRead = in.read(buffer);
    }  
  }
}
