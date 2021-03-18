package com.redhat.demo.rahmed;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "address")
public class AMQPDemo extends RouteBuilder {

	private String sourceName;
	private String destinationName;
	

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}


	@Override
	public void configure() {

		// Using Recipient List pattern: https://access.redhat.com/documentation/en-us/red_hat_fuse/7.8/html-single/apache_camel_development_guide/index#MsgRout-RecipientList
		
		from(getSourceName())
		.routeId("route-from-queue-consumer").streamCaching().tracing()
			.log("Recieved Message ${body} from Address " + getSourceName())
		.log("Sending Message to both Queues " + getDestinationName())
		.marshal().gzip() // use gzip to compress the message text
		.setExchangePattern(ExchangePattern.InOnly)
		.recipientList()
			.constant(getDestinationName())
			.parallelProcessing()
			.log(String.format("Calling ${in.header.%s}", Exchange.RECIPIENT_LIST_ENDPOINT))
			   
		.end();
		

	}
}
