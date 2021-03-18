package com.redhat.rahmed.jms;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.qpid.jms.JmsConnectionFactory;

public class Usecase1 {
	
    public static void executeUseCase(String zoneAUrl)
    {
		final int consumerCount=1;
		final int producerCount=1;
        JmsConnectionFactory connectionFactory1 = new JmsConnectionFactory();
		connectionFactory1.setRemoteURI(
			String.format("%s?%s", zoneAUrl, "failover.reconnectDelay=2000&failover.maxReconnectAttempts=-1&failover.warnAfterReconnectAttempts=10&failover.startupMaxReconnectAttempts=3"));
		connectionFactory1.setUsername("admin@amq-interconnect-edge");
		connectionFactory1.setPassword("admin");
		
		
		/* Usecase 1: Test for Usecase message routing */
		String usecase1ProducerQueueName="rh.usecase.1.queue";
		String usecase1ConsumerQueueName="rh.usecase.1.queue";
		JMSProducer usecase1QueueProducer = new JMSProducer(connectionFactory1, usecase1ProducerQueueName,false);
		JMSQueueConsumer usecase1QueueConsumer = new JMSQueueConsumer(connectionFactory1,usecase1ConsumerQueueName,"Consumer-1");
				
		// The interconnect will create a queue "rh.usecase.1.queue::rh.usecase.1.queue" 
		// We will generate 10 connection on the edge, those will not reflect on the broker.
		// Only 2 connection (1 for producer & 1 for consumer) will be created on broker

		Executor usecase1QueueExecutor = Executors.newFixedThreadPool(consumerCount+producerCount);

		// Generate Producers on queue
		for (int i=0;i<producerCount;i++)
			usecase1QueueExecutor.execute(usecase1QueueProducer);
	
		// Generate Consumers on queue.
		for (int i=0;i<consumerCount;i++)
			usecase1QueueExecutor.execute(usecase1QueueConsumer);
    }
} 