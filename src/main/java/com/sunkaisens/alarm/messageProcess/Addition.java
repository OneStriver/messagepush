package com.sunkaisens.alarm.messageProcess;


public class Addition {
	
	private String PBNo;//PBNo	
	
	private String bleId;//bleId
	
	private int bundleNo;//捆号
	
	private String target;//发送的目标（叉车PADID）
	
	private String belongWhId;//所属仓库(洋山)
	
	private String productId;//货物品名
	
	

	public String getPBNo() {
		return PBNo;
	}



	public void setPBNo(String pBNo) {
		PBNo = pBNo;
	}



	public String getBleId() {
		return bleId;
	}



	public void setBleId(String bleId) {
		this.bleId = bleId;
	}



	public int getBundleNo() {
		return bundleNo;
	}



	public void setBundleNo(int bundleNo) {
		this.bundleNo = bundleNo;
	}



	public String getTarget() {
		return target;
	}



	public void setTarget(String target) {
		this.target = target;
	}



	public String getBelongWhId() {
		return belongWhId;
	}



	public void setBelongWhId(String belongWhId) {
		this.belongWhId = belongWhId;
	}



	public String getProductId() {
		return productId;
	}



	public void setProductId(String productId) {
		this.productId = productId;
	}



	@Override
	public String toString() {
		return "Addition [PBNo=" + PBNo + ", bleId=" + bleId + ", bundleNo=" + bundleNo + ", target=" + target
				+ ", belongWhId=" + belongWhId + ", productId=" + productId + "]";
	}
	
}
