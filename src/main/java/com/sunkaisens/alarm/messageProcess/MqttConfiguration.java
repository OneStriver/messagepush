package com.sunkaisens.alarm.messageProcess;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alarm.mqtt")
public class MqttConfiguration {

	private String clientUrl;

	private String clientId;

	private String clientTopic;
	
	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientTopic() {
		return clientTopic;
	}

	public void setClientTopic(String clientTopic) {
		this.clientTopic = clientTopic;
	}

}
