package com.luoyan.kafkatest;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Test the Kafka Producer
 * 
 * @author luoyan
 * 
 */
public class ProducerTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger("ProducerTest");
	private static void usage() {
		System.err.println("command topicName brokerList=localhost:9092 messageNum");
	}
	public static void main(String[] args) {
		if (args.length != 3) {
			usage();
			System.exit(-1);
		}
		String topicName = args[0];
		String brokers = args[1];
		int messageNum = Integer.parseInt(args[2]);
		Properties props = new Properties();
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", brokers);
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		for (int i = 0; i < messageNum; i++) {
			producer.send(new KeyedMessage<String, String>(topicName, i + "test"));
			System.out.println("send topic test [test" + i + "]");
			LOGGER.debug("send topic test2 [" + i + "test]");
		}

	}
}
