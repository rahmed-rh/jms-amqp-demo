package com.redhat.rahmed.jms;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsTopic;
import org.apache.qpid.proton.engine.impl.StringUtils;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JMSQueueConsumer implements Runnable, ExceptionListener {
    private static final Logger log = LoggerFactory.getLogger(JMSQueueConsumer.class);
    private final ConnectionFactory connectionFactory;
    private final JmsPoolConnectionFactory pool;

    private final String queueName;
    private final String clientID;

    private final int sleepInterval = 2000;

    public JMSQueueConsumer(ConnectionFactory connectionFactory, String queueName, String clientID) {

        this.connectionFactory = connectionFactory;
        this.queueName = queueName;
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

                // test Queue
                Queue queue = session.createQueue(queueName);
                MessageConsumer messageConsumer = session.createConsumer(queue);

                while (true) {
                    Thread.sleep(sleepInterval);
                    TextMessage message = (TextMessage) messageConsumer.receive();
                    if (!(message.getText() != null & message.getText().isEmpty()))
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
