package com.healthyfish.healthyfish.POJO;

public class BeanListReq extends BeanBaseReq {
	private String prefix;//phc_健康计划
	int	from;
	int num;
	int to;

	public BeanListReq(){super(BeanListReq.class.getSimpleName());}
	
	public String getPrefix() {return prefix;}
	public void setPrefix(String prefix) {this.prefix = prefix;}
	public int getFrom() {return from;}
	public void setFrom(int from) {this.from = from;}
	public int getNum() {return num;}
	public void setNum(int num) {this.num = num;}
	public int getTo() {return to;}
	public void setTo(int to) {this.to = to;}
}
