package com.learn;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateSimpleFirstKafaProducer {

	Logger logger =LoggerFactory.getLogger(CreateSimpleFirstKafaProducer.class);
	public static void main(String[] args) {
		String bootstrapServer="127.0.0.1:9092";
		
     //Create kafka producer properties
		
	/*
		//the properties configuration is old Style.
		Properties properties=new Properties();
		properties.setProperty("bootstrap.servers", bootstrapServer);
		properties.setProperty("key.serializer", StringSerializer.class.getName()); //we are define the key data format
		properties.setProperty("value.serializer", StringSerializer.class.getName());//we are define the value  data format by default kafka use binary(0,1)
	*/
	
		//New way to configure Producer configuration properties
		Properties properties=new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //we are define the key data format
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//we are define the value  data format by default kafka use binary(0,1)
		
		
    //Create the producer
	KafkaProducer<String, String>producer=new KafkaProducer<String, String>(properties);
	
	String topicName="first-topic";
	//send the data
	for(int count=0;count<10;count++) {
		
	//Create producer Record
	String msg= "kafa send msg in synchronous or asynchronos "+count ;
	ProducerRecord<String, String>record=new ProducerRecord<String, String>(topicName, msg);
			
   //this Send the Data -asynchronous fashion,means no ordering of message at consumer side
	//producer.send(record);
		
   //To send msgs in synchronous method use below one.
	try {
		producer.send(record).get();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
		
	}	
	    //flush
		producer.flush();
		producer.close();
		
	}

}
