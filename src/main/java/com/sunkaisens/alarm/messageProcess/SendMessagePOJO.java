package com.sunkaisens.alarm.messageProcess;

import java.util.Map;

/**
 * @author HeYin
 * @date 2018/9/25
 */
public class SendMessagePOJO {

    private String belongWhId;
    private String alarmNumber;
    private String alarmSource;
    private int alarmCode;
    private String alarmReason;
    private String alarmHappenTime;
    private Map<String,Object> additionMap;

    public String getBelongWhId() {
        return belongWhId;
    }

    public void setBelongWhId(String belongWhId) {
        this.belongWhId = belongWhId;
    }

    public String getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    public int getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(int alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason;
    }

    public String getAlarmHappenTime() {
        return alarmHappenTime;
    }

    public void setAlarmHappenTime(String alarmHappenTime) {
        this.alarmHappenTime = alarmHappenTime;
    }

    public Map<String, Object> getAdditionMap() {
        return additionMap;
    }

    public void setAdditionMap(Map<String, Object> additionMap) {
        this.additionMap = additionMap;
    }
}
