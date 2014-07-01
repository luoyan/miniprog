package com.luoyan.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HdfsLogHandler implements ILogHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger("HfsLogHandler");
	private int taskId;
    public HdfsLogHandler(int taskId) {
    	this.taskId = taskId;
    	LOGGER.info("create  HdfsLogHandler " + taskId);
    	System.out.println("create  HdfsLogHandler " + taskId);
    }
	@Override
	public void parseInfo(List<String> lines) {
    	LOGGER.info("hdfs : parseInfo taskId " + this.taskId);
    	System.out.println("hdfs : parseInfo taskId " + this.taskId);
	}

}
