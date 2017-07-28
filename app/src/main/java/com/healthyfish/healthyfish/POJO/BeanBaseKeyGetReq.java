package com.healthyfish.healthyfish.POJO;

public class BeanBaseKeyGetReq extends BeanBaseReq {
	private String key;
	
	public BeanBaseKeyGetReq(){super(BeanBaseKeyGetReq.class.getSimpleName());}
	
	public String getKey() {return key;}
	public void setKey(String key) {this.key = key;}
}
