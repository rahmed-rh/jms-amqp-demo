package com.redhat.rahmed.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSharedDurableConsumer implements Runnable, ExceptionListener {
    private static final Logger log = LoggerFactory.getLogger(JMSSharedDurableConsumer.class);
    private final ConnectionFactory connectionFactory;
    private final JmsPoolConnectionFactory pool;

    private final String topicName;
    private final String subscriptionName;
    private final String clientID;

    public JMSSharedDurableConsumer(ConnectionFactory connectionFactory, String topicName, String subscriptionName,
            String clientID) {

        this.connectionFactory = connectionFactory;
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
        this.clientID = clientID;

        pool = new JmsPoolConnectionFactory();

    }

    @Override
    public void run() {
        try {

            pool.setConnectionFactory(connectionFactory);
            try (Connection connection = pool.createConnection()) {
                connection.setExceptionListener(this);
                // connection.setClientID(clientID); // <-- Please note that i commented clientID
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // test shared durable
                Topic topic = session.createTopic(topicName);
                MessageConsumer messageConsumer = session.createSharedDurableConsumer(topic, subscriptionName);

                while (true) {
                    TextMessage message = (TextMessage) messageConsumer.receive();

                    log.info("I'm '{}', Received '{}'", clientID, message.getText());
                }
            } catch (Exception e) {
                log.warn("Received exception in consumer", e);
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
