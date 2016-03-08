package com.luoyan.kafkatest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerFromFile {

	private static final Logger LOGGER = LoggerFactory
			.getLogger("ProducerFromFile");
	private static void usage() {
		System.err.println("command topicName brokerList=localhost:9092 messageNum fileName");
	}
	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			usage();
			System.exit(-1);
		}
		String topicName = args[0];
		String brokers = args[1];
		int messageNum = Integer.parseInt(args[2]);
		String fileName = args[3];
		Properties props = new Properties();
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", brokers);
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		

		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int total = 0;
        int count = 0;
        int exceptionNum = 0;
        int badFormatNum = 0;
        while ((line = br.readLine()) != null) {
        	try {
    			producer.send(new KeyedMessage<String, String>(topicName, line));
    			System.out.println("send topic " + topicName + " + [" + line + "]");
				if (messageNum > 0 && total >= messageNum) {
					break;
				}
				total++;
				count++;
			}
        	catch (Exception e) {
        		e.printStackTrace();
        		LOGGER.warn("failed to send log [" + line + "]");
        		exceptionNum++;
        	}
        }
        LOGGER.info("total " + total + " valid " + count + " exception " + exceptionNum + " badFormatNum " + badFormatNum);
	}
}
