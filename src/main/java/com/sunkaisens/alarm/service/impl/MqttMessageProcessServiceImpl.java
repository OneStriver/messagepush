package com.sunkaisens.alarm.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sunkaisens.alarm.messageProcess.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sunkaisens.alarm.service.MqttMessageProcessService;

import cn.jpush.api.JPushClient;

/**
 * @author HeYin
 * @date 2018/09/05
 * MQTT处理消息实现类
 */
@Service("mqttMessageProcessService")
public class MqttMessageProcessServiceImpl implements MqttMessageProcessService {
	
	private static Logger logger = LoggerFactory.getLogger(MqttMessageProcessServiceImpl.class);
	private Gson gson = new Gson();
	private Properties properties = new Properties();
	private SendMessagePOJO sendMessagePOJO = new SendMessagePOJO();
	private Map<String,Object> sendMessageMap = new ConcurrentHashMap<String,Object>();
	private static JPushClient jPushClient;
	
	@Override
	public void processMqttMsg(String topic,String messageInfo) {
		sendMessageMap.clear();
		logger.info("==============开始处理消息============");
		Properties propertyEntity = getPropertyEntity();
		String appKey = propertyEntity.getProperty("appKey");
		String masterSecret = propertyEntity.getProperty("masterSecret");
		System.err.println(">>>>>>appKey>>>>>>:"+appKey);
		System.err.println(">>>>>>masterSecret>>>>>>:"+masterSecret);
		jPushClient = new JPushClient(masterSecret, appKey);
		//转换Json数据
		AlarmProcessPOJO alarmProcessPOJO = gson.fromJson(messageInfo, AlarmProcessPOJO.class);
		String additionPairs = alarmProcessPOJO.getAddition_pairs();
		Addition addition = gson.fromJson(additionPairs, Addition.class);
		Map<String,Object> additionMap = alarmProcessPOJO.getAdditionMap();
		sendMessagePOJO.setBelongWhId(addition.getBelongWhId());
		sendMessagePOJO.setAlarmNumber(alarmProcessPOJO.getNo().toString());
		sendMessagePOJO.setAlarmSource(alarmProcessPOJO.getSource());
		sendMessagePOJO.setAlarmCode(alarmProcessPOJO.getCode());
		sendMessagePOJO.setAlarmReason(alarmProcessPOJO.getCause());
		sendMessagePOJO.setAlarmHappenTime(alarmProcessPOJO.getRaised_time());
		sendMessagePOJO.setAdditionMap(additionMap);
		String sendContentStr = gson.toJson(sendMessagePOJO);
		String alertMessageStr = "PBNO:"+sendMessagePOJO.getAlarmNumber()+"\n捆号:"+addition.getBundleNo()
								+"\n蓝牙标签:"+addition.getBleId()+"\n告警原因:"+sendMessagePOJO.getAlarmReason()
								+"\n发生时间:"+TimeUtil.getNowTime();
		//推送所有平台
		PushIosAndroidAlarm.sendToAll(jPushClient,"告警消息", "告警消息",alertMessageStr,sendContentStr);
    }
	
	private Properties getPropertyEntity(){
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/jgpush.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
