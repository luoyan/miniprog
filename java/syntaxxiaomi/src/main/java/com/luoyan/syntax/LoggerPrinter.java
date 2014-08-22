package com.luoyan.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerPrinter {
	private static final Logger LOGGER = LoggerFactory.getLogger("LoggerPrinter");
	private static final Logger consoleLOGGER = LoggerFactory.getLogger("consoleLOGGER");
	public static void main(String[] args) throws InterruptedException {
		int count = Integer.parseInt(args[0]);
		System.out.println("std out hello");
		consoleLOGGER.debug("console hello");
		LOGGER.debug("hello");
		System.out.println("std out world");
		consoleLOGGER.debug("console world");
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
