package com.redhat.rahmed.jms;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.qpid.jms.JmsConnectionFactory;

public class Usecase3 {
	
    public static void executeUseCase(String zoneAUrl,String zoneBUrl)
    {
		final int consumerCount=1;
		final int producerCount=1;
        JmsConnectionFactory connectionFactory1 = new JmsConnectionFactory();
		connectionFactory1.setRemoteURI(
			String.format("%s?%s", zoneAUrl, "failover.reconnectDelay=2000&failover.maxReconnectAttempts=-1&failover.warnAfterReconnectAttempts=10&failover.startupMaxReconnectAttempts=3"));
		connectionFactory1.setUsername("admin@amq-interconnect-edge");
		connectionFactory1.setPassword("admin");
		
		JmsConnectionFactory connectionFactory2 = new JmsConnectionFactory();
		connectionFactory2.setRemoteURI(
			String.format("%s?%s", zoneBUrl, "failover.reconnectDelay=2000&failover.maxReconnectAttempts=-1&failover.warnAfterReconnectAttempts=10&failover.startupMaxReconnectAttempts=3"));
		connectionFactory2.setUsername("admin@amq-interconnect-edge");
		connectionFactory2.setPassword("admin");

				
		String usecase3ProducerQueueName="rh.usecase.3.dummy";
		String usecase3ConsumerQueueName="rh.usecase.3.dummy";

		JMSProducer usecase3QueueProducer = new JMSProducer(connectionFactory1, usecase3ProducerQueueName,false);
		JMSQueueConsumer usecase3QueueConsumer1 = new JMSQueueConsumer(connectionFactory2,usecase3ConsumerQueueName,"Consumer-1");

		Executor usecase3QueueExecutor = Executors.newFixedThreadPool(consumerCount+producerCount);

		// Generate Producers on any dummy queue, it should be auto-created
		for (int i=0;i<producerCount;i++)
			usecase3QueueExecutor.execute(usecase3QueueProducer);
	
		// Generate Consumers on the previously defined dummy queue
		for (int i=0;i<consumerCount;i++)
			usecase3QueueExecutor.execute(usecase3QueueConsumer1);
		

    }
} 