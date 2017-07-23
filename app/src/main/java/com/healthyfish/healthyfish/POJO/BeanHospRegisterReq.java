package com.healthyfish.healthyfish.POJO;

import java.io.Serializable;

//挂号请求
//返回BeanKeyValue
//成功的key："reg_"+uid+"_"+doctBean.getHosp()+"_"+DateTimeUtils.getTime()，value为""
//			可用于将来查询已挂号的信息。
//失败的key:"failed"，value为失败的原因
public class BeanHospRegisterReq extends BeanBaseReq implements Serializable{
	private String hosp;//医院编号"lzzyy"
	private String hospTxt;//医院名称"柳州市中医院"
	private String dept;//部门编号
	private String deptTxt;//部门名称
	private String doct;//医生挂号标识
	private String doctTxt;//医生名字
	private String date;//医生排班表中的HID
	private String dateTxt;//医生排班表中的HZS
	private String staffNo;//员工编号
	private String name; //挂号患者的姓名
	private String sickId; //验证就诊卡后返回的患者在医院的内部编号
	
	public BeanHospRegisterReq(){super(BeanHospRegisterReq.class.getSimpleName());}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public String getHosp() {
		return hosp;
	}
	public void setHosp(String hosp) {
		this.hosp = hosp;
	}
	public String getHospTxt() {
		return hospTxt;
	}
	public void setHospTxt(String hospTxt) {
		this.hospTxt = hospTxt;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptTxt() {
		return deptTxt;
	}
	public void setDeptTxt(String deptTxt) {
		this.deptTxt = deptTxt;
	}
	public String getDoct() {
		return doct;
	}
	public void setDoct(String doct) {
		this.doct = doct;
	}
	public String getDoctTxt() {
		return doctTxt;
	}
	public void setDoctTxt(String doctTxt) {
		this.doctTxt = doctTxt;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateTxt() {
		return dateTxt;
	}
	public void setDateTxt(String dateTxt) {
		this.dateTxt = dateTxt;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSickId() {
		return sickId;
	}

	public void setSickId(String sickId) {
		this.sickId = sickId;
	}

}
