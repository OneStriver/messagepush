package com.sunkaisens.alarm.messageProcess;

public class WarningMessage {
	
	
	private int code;//告警码
	
	private boolean clear;
	
	private Addition addition_pairs;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}



	public boolean isClear() {
		return clear;
	}



	public void setClear(boolean clear) {
		this.clear = clear;
	}



	public Addition getAddition_pairs() {
		return addition_pairs;
	}



	public void setAddition_pairs(Addition addition_pairs) {
		this.addition_pairs = addition_pairs;
	}



	@Override
	public String toString() {
		return "WarningMessage [code=" + code + ", clear=" + clear + ", addition_pairs=" + addition_pairs + "]";
	}
	
	
}
