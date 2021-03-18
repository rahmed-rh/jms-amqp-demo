package com.redhat.rahmed.jms;

import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JMSDurableConsumer implements Runnable, ExceptionListener {
    private static final Logger log = LoggerFactory.getLogger(JMSDurableConsumer.class);
    private final ConnectionFactory connectionFactory;
    private final String topicName;
    private final String subscriptionName;
    private final String clientID;
    

    public JMSDurableConsumer(ConnectionFactory connectionFactory, String topicName, String subscriptionName,String clientID) {
    	
        this.connectionFactory = connectionFactory;
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try (Connection connection = connectionFactory.createConnection()) {
            connection.setExceptionListener(this);
            connection.setClientID(clientID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //MessageConsumer messageConsumer = session.createConsumer(destination);
            
            // test shared durable
            Topic topic = session.createTopic(topicName);
            MessageConsumer messageConsumer = session.createDurableConsumer(topic, subscriptionName);
            
            

            while (true) {
                TextMessage message = (TextMessage) messageConsumer.receive();

                log.info("I'm '{}', Received '{}'",clientID, message.getText());
            }
        } catch (Exception e) {
            log.warn("Received exception in consumer", e);
        }
    }

    @Override
    public void onException(JMSException exception) {
        log.warn("Received JMSException", exception);
    }
}
