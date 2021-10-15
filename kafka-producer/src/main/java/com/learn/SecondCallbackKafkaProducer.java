package com.learn;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//The main aim to create the callback producer to find the another info 
// like how  the data goes on which partiton and offset how multiple msg forward to topic.
public class SecondCallbackKafkaProducer {


	
	public static void main(String[] args) {
		final Logger logger =LoggerFactory.getLogger(SecondCallbackKafkaProducer.class);
		String bootstrapServer="127.0.0.1:9092";

     //Create kafka producer properties

	
		//New way to configure Producer configuration properties
		Properties properties=new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //we are define the key data format
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//we are define the value  data format by default kafka use binary(0,1)
		
		
    //Create the producer
	KafkaProducer<String, String>producer=new KafkaProducer<String, String>(properties);
	
	//Create producer Record
	String topicName="first-topic";
	String msg= "Good Evening greeting : ";
	
	//send the data in round robin fashion in consumer side(asynchronous fashion.means no ordering f message at consumer side)
	for(int count=0;count<6;count++)
	{
			ProducerRecord<String, String>record=new ProducerRecord<String, String>(topicName, msg+Integer.toString(count));
					
		   //Send the Data  with callable -asynchronous method
			producer.send(record, new Callback() {
				
				/*When a message is sent asynchronously, you need to provide a CallBack class that implements onCompletion() method
				which is called when a message is sent successfully and acknowledged by Kafka Server
				*/
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					
					// this method execute every time msg sent sucessfully or execption thrown
					
					if(exception==null) {
						//msg sent sucessfully.
						
						logger.info(" New Metadata record \n"+
					                "Topic : "+metadata.topic() +"\n"+
					                "Partition : "+metadata.partition()+"\n"+
					                "Offset : "+metadata.offset()+"\n"+
					                "Timestamp: "+metadata.timestamp());
						
					}else {
						logger.error("Exception occured :"+exception);
					}
					
				}
			});
	}			
    //flush
	producer.flush();
	producer.close();
		
		
		
	}

}
