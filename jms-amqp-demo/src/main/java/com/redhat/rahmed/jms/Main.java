package com.redhat.rahmed.jms;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTopic;
import org.apache.qpid.jms.selector.SelectorParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.redhat.rahmed.jms.*;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		// https://access.redhat.com/documentation/en-us/red_hat_amq/2021.q1/html-single/using_the_amq_jms_client/index#connection_uri_options_ssl
		
		String zoneAUrl ="failover:(amqps://amq-edge-amqps-broker-with-interconnect-mesh.apps.cluster-f037.gcp.testdrive.openshift.com:443?transport.verifyHost=false&transport.trustAll=true&amqp.saslMechanisms=SCRAM-SHA-1&amqp.idleTimeout=120000&amqp.traceFrames=true)";
		String zoneBUrl ="failover:(amqps://amq-edge-amqps-broker-with-interconnect-mesh.apps.y3tpfd3p.westeurope.aroapp.io:443?transport.verifyHost=false&transport.trustAll=true&amqp.saslMechanisms=SCRAM-SHA-1&amqp.idleTimeout=120000&amqp.traceFrames=true)";

				
		/* Usecase 1: Test for Usecase message routing */
		// Usecase1.executeUseCase(zoneAUrl);
		
		/* Usecase 2: Test for Usecase message routing across dc with multicasting*/
		// Usecase2.executeUseCase(zoneAUrl,zoneBUrl);

		/* Usecase 3: Test for Usecase link routing across dc with wildcards*/
		// Usecase3.executeUseCase(zoneAUrl,zoneBUrl);

		/* Usecase 4: Test for Usecase link routing across dc with wildcards*/
		Usecase4.executeUseCase(zoneAUrl,zoneBUrl);

		/* Test for usecase edge q (edge.zonea.messaging.queue), the queue only exists on edge broker*/
		// String usecase2ProducerQueueName="edge.zonea.messaging.queue";
		// String usecase2ConsumerQueueName="edge.zonea.messaging.queue";
		// JMSProducer usecase2QueueProducer = new JMSProducer(connectionFactory1, usecase2ProducerQueueName,false);
		// JMSQueueConsumer usecase2QueueConsumer = new JMSQueueConsumer(connectionFactory1,usecase2ConsumerQueueName,"Consumer-1");
		
		
		// Executor usecase2QueueExecutor = Executors.newFixedThreadPool(2);

		// only produce and observe messages on the broker.
		// usecase2QueueExecutor.execute(usecase2QueueProducer);

		
		// only consume and observe messages on the broker.
		// usecase2QueueExecutor.execute(usecase2QueueConsumer);

		/* Test for usecase edge q (edge.zonea.messaging.queue), the queue only exists on edge broker*/
		// String usecase3ProducerQueueName="obis.onshore.vt.q1";
		// String usecase3ConsumerQueueName="obis.onshore.vt.q1";
		// JMSProducer usecase3QueueProducer = new JMSProducer(connectionFactory2, usecase3ProducerQueueName,false);
		// JMSQueueConsumer usecase3QueueConsumer = new JMSQueueConsumer(connectionFactory2,usecase3ConsumerQueueName,"Consumer-1");
		
		
		// Executor usecase3QueueExecutor = Executors.newFixedThreadPool(1);

		// // only produce and observe messages on the broker.
		// usecase2QueueExecutor.execute(usecase2QueueProducer);

		
		// // only consume and observe messages on the broker.
		// usecase3QueueExecutor.execute(usecase3QueueConsumer);


		
	}

}
