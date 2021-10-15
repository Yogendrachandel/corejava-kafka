package com.learn;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerGroup {
	public static void main(String[] args) {
		
	final Logger logger =LoggerFactory.getLogger(KafkaConsumerGroup.class);
	String bootstrapServer="127.0.0.1:9092";
	String consumerId= "kafka-group-application";
	
	//Create Consumer Properties
	Properties properties=new Properties();
	properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
	properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); //we are define the key data format that is recived as Byte then deserialize to String format.
	properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); //we are define the key data format that is recived as Byte then deserialize to String format.
	properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //possible values -earliest/latest/none. (means consumer read msgs from begning/latest/none).
	properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerId); // We are define the consumer belong to which Group.
	//Create consumer
	KafkaConsumer<String, String> consumer= new KafkaConsumer<String, String>(properties);
	
	//Subscribe consumer to out Topic(s)
     consumer.subscribe(Arrays.asList("first-topic")); 		//we can pass multiple topics.
	
   //Poll for new Data.
     while(true) {
	
    	ConsumerRecords<String, String>records= consumer.poll(Duration.ofMillis(100));
    	
    	for(ConsumerRecord<String, String>record :records) {
    		logger.info("----Kafka Consumer Group----"+" "+
    				"Key :"+record.key()+" "+
    				    "Value :"+record.value()+" "+
    				    "Partition :"+record.partition()+" "+
    				    "Offset :"+record.offset());
    		
    	}
     }
}

}
