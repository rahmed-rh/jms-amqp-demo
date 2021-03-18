package com.redhat.demo.rahmed;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration parameters filled in from application.properties and overridden
 * using env variables on Openshift.
 */
@Configuration
@ConfigurationProperties(prefix = "amqp")
public class AMQPConfiguration {

	/**
	 * AMQ service name
	 */
	private String url;

	/**
	 * AMQ username
	 */
	private String username;

	/**
	 * AMQ password
	 */
	private String password;


	/**
	 * AMQ parameters
	 */
	private String parameters;

	public AMQPConfiguration() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
