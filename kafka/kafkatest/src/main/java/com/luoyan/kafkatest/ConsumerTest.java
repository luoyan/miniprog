package com.luoyan.kafkatest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerTest extends Thread {
	private final ConsumerConnector consumer;
	private final String topic;
	private static final Logger LOGGER = LoggerFactory
			.getLogger("ConsumerTest");
	private static String zookeeperAddress;
	private static String groupId;
	private static void usage() {
		System.err.println("command topic_name zookeeper/chroot group.id");
	}
	public static void main(String[] args) {
		if (args.length != 3) {
			usage();
			System.exit(-1);
		}
		String topicName = args[0];
		zookeeperAddress = args[1];
		groupId = args[2];
		ConsumerTest consumerThread = new ConsumerTest(topicName);
		consumerThread.start();
	}

	public ConsumerTest(String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig());
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeperAddress);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "400000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

		return new ConsumerConfig(props);

	}

	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		LOGGER.debug("start loop");
		while (it.hasNext())
			// LOGGER.debug("hasNext");
			// System.out.println(new String(it.next().message()));
			LOGGER.debug(new String(it.next().message()));
		LOGGER.debug("end loop");
	}
}
