package com.luoyan.kafkatest;

import java.util.Properties;  

import kafka.javaapi.producer.Producer;  
import kafka.producer.KeyedMessage;  
import kafka.producer.ProducerConfig;  

/** 
 * Test the Kafka Producer 
 * @author luoyan 
 * 
 */  
public class ProducerTest {  
        public static void main(String[] args) {  
                Properties props = new Properties();  
                //props.put("zk.connect", "localhost:2181/kafak");  
                props.put("serializer.class", "kafka.serializer.StringEncoder");  
                props.put("metadata.broker.list", "localhost:9092");  
                ProducerConfig config = new ProducerConfig(props);  
                Producer<String, String> producer = new Producer<String, String>(config);  
                for (int i = 0; i < 10; i++)  {
                        producer.send(new KeyedMessage<String, String>("test", "test" + i));  
                        System.out.println("send topic test [test" + i + "]");
                }

        }  
}  
