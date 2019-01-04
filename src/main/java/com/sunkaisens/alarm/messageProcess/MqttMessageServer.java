package com.sunkaisens.alarm.messageProcess;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sunkaisens.alarm.service.MqttMessageProcessService;

/**
 * MQTT消息处理类
 */
@Component
public class MqttMessageServer implements ApplicationRunner {
	
	private static Logger logger = LoggerFactory.getLogger(MqttMessageServer.class);
	private static MqttClient mqttClient = null;
    private static int allQos = 2;  
    private  MemoryPersistence persistence = new MemoryPersistence();
  
  	private final BlockingQueue<PostedMsg> messageQueue = new LinkedBlockingQueue<PostedMsg>();
  	
  	@Resource
  	private MqttConfiguration mqttConfiguration;
  	@Resource
	private MqttMessageProcessService mqttMessageProcessService;
  	
  	@Override
  	public void run(ApplicationArguments args) throws Exception {
		String clientUrl = mqttConfiguration.getClientUrl();
		String clientTopic = mqttConfiguration.getClientTopic();
		String clientId = mqttConfiguration.getClientId();
		//初始化参数
		MqttConnectOptions connOpts = new MqttConnectOptions();    
        connOpts.setCleanSession(true);  
        connOpts.setConnectionTimeout(30);  
        connOpts.setKeepAliveInterval(60);
        connOpts.setAutomaticReconnect(true);
        try {
			mqttClient = new MqttClient(clientUrl, clientId, persistence);
			mqttClient.setCallback(new MqttCallbackExtended() {
				@Override
				public void connectComplete(boolean arg0, String arg1) {
					//订阅主题
					subscribeInformation(mqttClient,clientTopic, allQos);
			        logger.info(">>>>>>>>订阅所有主题成功>>>>>>>>");
				}
				
				@Override
		        public void connectionLost(Throwable throwable) {
		            logger.info(">>>>>>>>MQTT服务器失去连接！！！>>>>>>>>");
		        }
				
				@Override
		        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
					System.err.println("========================");
					logger.info(">>>>>>>>接收到MQTT推送的消息:" + topic);
					logger.info(">>>>>>>>接收到MQTT推送的消息内容:" + new String(mqttMessage.getPayload(), 0, mqttMessage.getPayload().length,"UTF-8"));
					PostedMsg postedMsg = new PostedMsg(mqttMessage,topic);
					messageQueue.offer(postedMsg);
		        }
				
				@Override
		        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					logger.info("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
		        }
			});
			mqttClient.connect(connOpts);
			logger.info(">>>>>>>>连接MQTT服务器成功！！！,连接信息:"+clientUrl);	

			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							PostedMsg msg = (PostedMsg) messageQueue.take();
							if (msg != null) {
								MqttMessage mqttMessage = (MqttMessage) msg.getMsg();
								String strRevMsg = null;
								try {
									strRevMsg = new String(mqttMessage.getPayload(), 0, mqttMessage.getPayload().length,"UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								mqttMessageProcessService.processMqttMsg(msg.getTopic(), strRevMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			//缓存MqttClient实例在Map中
			GlobalHashMap.mqttClientMap.put("mqttClient", mqttClient);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
  	
	private void subscribeInformation(MqttClient mqttClient,String allTopics,int allQos) {  
        try {
			mqttClient.subscribe(allTopics, allQos);
		} catch (MqttException e) {
			e.printStackTrace();
		} 
    }

	/**
	 * PUSH消息MQTT
	 */
	public void sendMqttMessage(String content, int qos, String topic) {
		try {
			MqttMessage message = new MqttMessage(content.getBytes("UTF-8"));
			message.setQos(qos);
			message.setRetained(false);
			mqttClient.publish(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
