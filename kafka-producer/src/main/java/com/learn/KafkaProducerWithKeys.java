package com.learn;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
By using the Keys ,it  provides the gurantee that every time when we run this producer with key 
then the msg will goes to same partition number.
Please run this application twice or more time and check the logs  to find the behaviour .
*/

public class KafkaProducerWithKeys {


	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final Logger logger =LoggerFactory.getLogger(KafkaProducerWithKeys.class);
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
	
	
	//send the data in round robin fashion in consumer side.
	for(int count=0;count<10;count++)
	{
		String key="Id_"+count;
		String msg= "Good AfterNoon ALL : "+Integer.toString(count);	
		//Created record with Key
		
		logger.info(" -----KEY ----- :"+key);
		ProducerRecord<String, String>record=new ProducerRecord<String, String>(topicName, key,msg);
					
		   //Send the Data  with callable -asynchronous method
			producer.send(record, new Callback() {
				
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					// this method execute every time msg sent sucessfully or execption thrown
					
					if(exception==null) {
						//msg sent sucessfully.
						
						logger.info("Kafka Producer metadata record \n"+
					                "Topic : "+metadata.topic() +"\n"+
					                "Partition : "+metadata.partition()+"\n"+
					                "Offset : "+metadata.offset()+"\n"+
					                "Timestamp: "+metadata.timestamp());
						
					}else {
						logger.error("Exception occured :"+exception);
					}
					
				}
			}).get(); //block the send() to work as synchronous mode -but dont do this in production environment
	}			
    //flush
	producer.flush();
	producer.close();
		
		
		
	}

}
