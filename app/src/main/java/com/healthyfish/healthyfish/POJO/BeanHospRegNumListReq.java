package com.healthyfish.healthyfish.POJO;
//获取医生某天的排班情况
//返回List<String>
public class BeanHospRegNumListReq extends BeanBaseReq {
	private String hosp;
	private String dept;
	private String doct;
	private String date;
	private String type;

	public BeanHospRegNumListReq(){super(BeanHospRegNumListReq.class.getSimpleName());}

	public String getHosp() {
		return hosp;
	}
	public void setHosp(String hosp) {
		this.hosp = hosp;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDoct() {
		return doct;
	}
	public void setDoct(String doct) {
		this.doct = doct;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
