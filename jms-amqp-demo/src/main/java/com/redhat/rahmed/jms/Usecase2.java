package com.redhat.rahmed.jms;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.qpid.jms.JmsConnectionFactory;

public class Usecase2 {
	
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
		
		
		String usecase2ProducerQueueName="rh.usecase.2.queue";
		String usecase2Consumer1QueueName="rh.usecase.2.q1";
		String usecase2Consumer2QueueName="rh.usecase.2.q2";

		JMSProducer usecase2QueueProducer = new JMSProducer(connectionFactory1, usecase2ProducerQueueName,false);
		JMSQueueConsumer usecase2QueueConsumer1 = new JMSQueueConsumer(connectionFactory2,usecase2Consumer1QueueName,"Consumer-1");
		JMSQueueConsumer usecase2QueueConsumer2 = new JMSQueueConsumer(connectionFactory2,usecase2Consumer2QueueName,"Consumer-2");

		Executor usecase2QueueExecutor = Executors.newFixedThreadPool((consumerCount*2)+producerCount);

		// Generate Producers on queue
		for (int i=0;i<producerCount;i++)
			usecase2QueueExecutor.execute(usecase2QueueProducer);
	
		// Generate Consumers on each queue(q1,q2).
		for (int i=0;i<consumerCount;i++)
			usecase2QueueExecutor.execute(usecase2QueueConsumer1);
		
		for (int i=0;i<consumerCount;i++)
			usecase2QueueExecutor.execute(usecase2QueueConsumer2);
    }
} 