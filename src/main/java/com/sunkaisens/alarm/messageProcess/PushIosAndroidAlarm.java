package com.sunkaisens.alarm.messageProcess;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.model.notification.IosAlert;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HeYin
 * @date 2018/09/05
 * 极光推送IosAndroid类
 */
public class PushIosAndroidAlarm {

	private static final Logger logger = LoggerFactory.getLogger(PushIosAndroidAlarm.class);
	// 上线之后要改为true
	private static Boolean ApnsProduction = true;

	/**
	 * 发送给所有用户
	 * @param notification_title 通知内容标题
	 * @param msg_title 消息内容标题
	 * @param sendContentStr 消息内容
	 * @return 0推送失败，1推送成功
	 */
	public static int sendToAll(JPushClient jPushClient, String notification_title, String msg_title,String alertMessageStr,String sendContentStr) {
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_android_and_ios(notification_title, msg_title,alertMessageStr,sendContentStr);
			logger.info(">>>>>>>>推送的PushPayload:" + pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			logger.info(">>>>>>>>推送消息之后的结果PushResult:" + pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 向所有平台所有用户推送消息
	 */
	public static PushPayload buildPushObject_android_and_ios(String notification_title, String msg_title,
		String alertMessageStr,String sendContentStr) {
		logger.info(">>>>>>>>向所有平台所有用户推送消息中>>>>>>>>");
		return PushPayload.newBuilder()
			.setPlatform(Platform.android_ios())
			.setAudience(Audience.all())
			.setNotification(
				Notification.newBuilder()
				.setAlert(notification_title)
				.addPlatformNotification(
					AndroidNotification.newBuilder()
					.addExtra("alarm",sendContentStr)
					.setAlert(alertMessageStr)
					.setTitle(notification_title)
					// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
					.build())
				.addPlatformNotification(
					IosNotification.newBuilder()
					.addExtra("alarm",sendContentStr)
					// 传一个IosAlert对象，指定apns title、title、subtitle等
					.setAlert(alertMessageStr)
					// 直接传alert
					// 此项是指定此推送的badge自动加1
					.incrBadge(1)
					// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
					// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
					.setSound("default")
					// 此项说明此推送是一个background推送
					// .setContentAvailable(true)
					.build())
				.build())
			// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。jpush的自定义消息，sdk默认不做任何处理，不会有通知提示。
			.setMessage(Message.newBuilder().setMsgContent(sendContentStr).setTitle(msg_title).build())
			.setOptions(Options.newBuilder()
				// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
				.setApnsProduction(ApnsProduction)
				// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
				.setSendno(1)
				// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
				.setTimeToLive(86400).build())
			.build();
	}

	/**
	 * 向android平台所有用户推送消息
	 * @param notification_title
	 * @param msg_title
	 * @param alertMessageStr
	 * @param alertMessageStr
	 * @return
	 */
	private static PushPayload buildPushObject_android_all(String notification_title, String msg_title,
		String alertMessageStr,SendMessagePOJO sendMessagePOJO,String sendContentStr) {
		logger.info("----------向android平台所有用户推送消息中......");
		return PushPayload.newBuilder()
			// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
			.setPlatform(Platform.android())
			// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
			.setAudience(Audience.all())
			// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发
			.setNotification(Notification.newBuilder()
				// 指定当前推送的android通知
				.addPlatformNotification(AndroidNotification.newBuilder().setAlert(notification_title)
				.setTitle(notification_title)
				// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
				.addExtra("androidNotification extras key", alertMessageStr).build())
				.build())
			// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
			// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
			// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
			.setMessage(
				Message.newBuilder().setMsgContent(alertMessageStr).setTitle(msg_title)
				.addExtra("message extras key", alertMessageStr).build())
			.setOptions(Options.newBuilder()
					// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
					.setApnsProduction(ApnsProduction)
					// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
					.setSendno(1)
					// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
					.setTimeToLive(86400).build())
			.build();
	}

	/**
	 * 向ios平台所有用户推送消息
	 * @param notification_title
	 * @param msg_title
	 * @param alertMessageStr
	 * @param alertMessageStr
	 * @return
	 */
	private static PushPayload buildPushObject_ios_all(String notification_title, String msg_title,
		String alertMessageStr,SendMessagePOJO sendMessagePOJO,String sendContentStr) {
		logger.info("----------向ios平台所有用户推送消息中.......");
		return PushPayload.newBuilder()
			// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
			.setPlatform(Platform.ios())
			// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
			.setAudience(Audience.all())
			// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
			.setNotification(Notification.newBuilder()
				// 指定当前推送的android通知
				.addPlatformNotification(
					IosNotification.newBuilder()
					// 传一个IosAlert对象，指定apns title、title、subtitle等
					.setAlert(alertMessageStr)
					// 直接传alert
					// 此项是指定此推送的badge自动加1
					.incrBadge(1)
					// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
					// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
					.setSound("default")
					// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
					.addExtra("iosNotification extras key", alertMessageStr)
					// 此项说明此推送是一个background推送
					// .setContentAvailable(true)
					.build())
				.build())
			// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
			// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
			// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
			.setMessage(
				Message.newBuilder().setMsgContent(alertMessageStr).setTitle(msg_title)
				.addExtra("message extras key", alertMessageStr).build())
			.setOptions(Options.newBuilder()
				// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
				.setApnsProduction(ApnsProduction)
				// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
				.setSendno(1)
				// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
				.setTimeToLive(86400).build())
			.build();
	}

}

