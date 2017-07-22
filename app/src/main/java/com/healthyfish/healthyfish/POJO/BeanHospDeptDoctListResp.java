package com.healthyfish.healthyfish.POJO;

public class BeanHospDeptDoctListResp extends BeanBaseReq {
	private String hosp;
	private String dept;
	
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
	BeanHospDeptDoctListResp(){super(BeanHospDeptDoctListResp.class.getSimpleName());}
	
}
