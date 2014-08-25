package com.luoyan.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.web.miliao.access.log4j.AccessAppender;

public class LoggerPrinter {
	private static final Logger LOGGER = LoggerFactory.getLogger("LoggerPrinter");
	private static final Logger consoleLogger = LoggerFactory.getLogger("consoleLogger");
	public static void main(String[] args) throws InterruptedException {
        //System.out.println("clientInfoV3 " + clientInfoV3.toString());
		int count = Integer.parseInt(args[0]);
		System.out.println("std out hello");
		consoleLogger.debug("console hello");
		LOGGER.debug("hello");
		System.out.println("std out world");
		consoleLogger.debug("console world");
		LOGGER.debug("world");
		for (int i = 0; i < count; i++) {
			LOGGER.debug("hello " + i);
			Thread.sleep(1000);
			LOGGER.debug("world " + i);
		}
		/*
		System.out.println("start to sleep");
		Thread.sleep(1000000 * 1000);
		System.out.println("end to sleep");
		*/
	}

}
