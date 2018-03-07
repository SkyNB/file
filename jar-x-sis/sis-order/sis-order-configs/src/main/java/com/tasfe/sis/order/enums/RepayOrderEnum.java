package com.tasfe.sis.order.enums;

public enum RepayOrderEnum {

	REPAY_NORMAL(0,"还款正常"),REPAY_EXECEED(2,"还款逾期");
	
	private Integer status;
	private String desc;
	
	RepayOrderEnum(Integer status,String desc){
		this.status = status;
		this.desc = desc;
	}
	
	public static Integer getState(String remark) {
		for(RepayOrderEnum enumItem :RepayOrderEnum.values()) {
			if (enumItem.desc.equals(remark)) {
				return enumItem.status;
			}
		}
		return 0;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
