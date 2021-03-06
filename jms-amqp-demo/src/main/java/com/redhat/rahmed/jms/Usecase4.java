package com.redhat.rahmed.jms;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.qpid.jms.JmsConnectionFactory;

public class Usecase4 {

	public static void executeUseCase(String zoneAUrl, String zoneBUrl) {
		final int consumerCount = 1;
		final int producerCount = 1;
		JmsConnectionFactory connectionFactory1 = new JmsConnectionFactory();
		connectionFactory1.setRemoteURI(String.format("%s?%s", zoneAUrl,
				"failover.reconnectDelay=2000&failover.maxReconnectAttempts=-1&failover.warnAfterReconnectAttempts=10&failover.startupMaxReconnectAttempts=3"));
		connectionFactory1.setUsername("admin@amq-interconnect-edge");
		connectionFactory1.setPassword("admin");

		String usecase4ProducerQueueName = "telemetry.cortex.train.window.applicatienaam1";

		JMSProducer usecase4QueueProducer = new JMSProducer(connectionFactory1, usecase4ProducerQueueName, false);

		Executor usecase4QueueExecutor = Executors.newFixedThreadPool(consumerCount + producerCount);

		// Generate Producers on any dummy queue, it should be auto-created
		for (int i = 0; i < producerCount; i++)
			usecase4QueueExecutor.execute(usecase4QueueProducer);

	}
}
