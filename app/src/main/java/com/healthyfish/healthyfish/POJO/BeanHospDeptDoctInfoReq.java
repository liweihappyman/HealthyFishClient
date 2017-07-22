package com.healthyfish.healthyfish.POJO;

//响应用BeanHospDeptDoctListRespItem.java
public class BeanHospDeptDoctInfoReq extends BeanBaseReq {
	private String hosp;
	private String dept;
	private String staffNo;
	
	BeanHospDeptDoctInfoReq(){super(BeanHospDeptDoctInfoReq.class.getSimpleName());}

	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getHosp() {
		return hosp;
	}
	public void setHosp(String hosp) {
		this.hosp = hosp;
	}

	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;

	public BeanHospDeptDoctInfoReq(){super(BeanHospDeptDoctInfoReq.class.getSimpleName());}

	
}
