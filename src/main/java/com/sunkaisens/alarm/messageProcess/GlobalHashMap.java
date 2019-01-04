package com.sunkaisens.alarm.messageProcess;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * 数据缓存类
 * Quartz配置属性(Job,JobGroupName,Trigger,TriggerGroupName)
 */
public class GlobalHashMap {
	
	//緩存 MqttClient
	public static ConcurrentHashMap<String, MqttClient> mqttClientMap = new ConcurrentHashMap<String, MqttClient>();

	// Job
	private static String jobName = "AlarmJob";
	private static String jobGroupName = "AlarmJobGroup";
	// Trigger
	private static String triggerName = "AlarmTrigger";
	private static String triggerGroupName = "AlarmTriggerGroup";

	public static String getJobName(String alarmSingleFalg) {
		return jobName+"-"+alarmSingleFalg;
	}

	public static void setJobName(String jobName) {
		GlobalHashMap.jobName = jobName;
	}

	public static String getJobGroupName(String alarmSingleFalg) {
		return jobGroupName+"-"+alarmSingleFalg;
	}

	public static void setJobGroupName(String jobGroupName) {
		GlobalHashMap.jobGroupName = jobGroupName;
	}

	public static String getTriggerName(String alarmSingleFalg) {
		return triggerName+"-"+alarmSingleFalg;
	}

	public static void setTriggerName(String triggerName) {
		GlobalHashMap.triggerName = triggerName;
	}

	public static String getTriggerGroupName(String alarmSingleFalg) {
		return triggerGroupName+"-"+alarmSingleFalg;
	}

	public static void setTriggerGroupName(String triggerGroupName) {
		GlobalHashMap.triggerGroupName = triggerGroupName;
	}

}
