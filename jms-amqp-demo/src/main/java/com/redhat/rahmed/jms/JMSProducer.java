package com.redhat.rahmed.jms;

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicLong;

public class JMSProducer implements Runnable, ExceptionListener {

    private static final Logger log = LoggerFactory.getLogger(JMSProducer.class);
    private final ConnectionFactory connectionFactory;
    private final JmsPoolConnectionFactory pool;

    private final String destinationName;
    private final boolean isTopic;
    private final int sleepInterval = 2000;
    private final AtomicLong counter = new AtomicLong(0);

    public JMSProducer(ConnectionFactory connectionFactory, String destinationName, boolean isTopic) {
        this.connectionFactory = connectionFactory;
        this.destinationName = destinationName;
        this.isTopic = isTopic;
        pool = new JmsPoolConnectionFactory();

    }

    @Override
    public void run() {

        try {

            pool.setConnectionFactory(connectionFactory);
            pool.setUseAnonymousProducers(false);

            try (Connection connection = pool.createConnection()) {
                connection.setExceptionListener(this);
                // connection.setClientID("Producer"); // <-- Please note that i commented clientID
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = null;

                if (isTopic) {
                    destination = session.createTopic(destinationName);
                } else {
                    destination = session.createQueue(destinationName);
                }

                MessageProducer messageProducer = session.createProducer(destination);
                // messageProducer.setTimeToLive(1);

                while (true) {
                    TextMessage message = session.createTextMessage("JMSHello " + counter.incrementAndGet());
                    messageProducer.send(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY,
                            Message.DEFAULT_TIME_TO_LIVE);
                    // messageProducer.send(message, DeliveryMode.PERSISTENT,
                    // Message.DEFAULT_PRIORITY, 1);
                    log.info("Producer, sending message \"'{}'\"", message.getText());
                    Thread.sleep(sleepInterval);
                }

            } catch (Exception e) {
                log.warn("Received exception in producer", e);
            }
        } finally {
            pool.stop();
        }

    }

    @Override
    public void onException(JMSException exception) {
        log.warn("Received JMSException", exception);

    }
}
