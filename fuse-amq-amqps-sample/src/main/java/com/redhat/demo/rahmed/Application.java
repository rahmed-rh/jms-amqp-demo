/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.demo.rahmed;

import javax.net.ssl.SSLContext;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.transports.TransportOptions;
import org.apache.qpid.jms.transports.TransportSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.micrometer.core.instrument.util.StringUtils;



/**
 * The Spring-boot main class.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "amqp-component")
    AMQPComponent amqpComponent(AMQPConfiguration config) throws Exception {
		String remoteURI = StringUtils.isBlank(config.getParameters())
				? String.format("%s", config.getUrl())
				: String.format("%s?%s", config.getUrl(), config.getParameters());
		System.out.println("remoteURI =" + remoteURI);
		JmsConnectionFactory qpid = new JmsConnectionFactory(config.getUsername(), config.getPassword(),
				remoteURI);
		/*
		// Work In progress
        TransportOptions sslOptions = new TransportOptions();
        sslOptions.setKeyStoreLocation("");
        sslOptions.setKeyStorePassword("");
        sslOptions.setVerifyHost(false);

        SSLContext context = TransportSupport.createJdkSslContext(sslOptions);
        
        qpid.setSslContext(context);
        */
              
        
        //PooledConnectionFactory factory = new PooledConnectionFactory();
        //factory.setConnectionFactory(qpid);
        //return new AMQPComponent(factory);
        
        
        return new AMQPComponent(qpid);
    }

}
