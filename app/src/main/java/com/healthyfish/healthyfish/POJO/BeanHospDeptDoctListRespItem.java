package com.healthyfish.healthyfish.POJO;

import java.util.List;

public class BeanHospDeptDoctListRespItem extends BeanBaseReq {
	private int STAFF_NO; //员工编号
	private String REISTER_NAME; //职称
	private int CLINIQUE_CODE; //诊室
	private int PRICE; //挂号价格
	private String DOCTOR_NAME; //姓名。挂号时，提供DOCTOR而非DOCTOR_NAME
	private String WEB_INTRODUCE; //简介
	private String ZHAOPIAN; //照片
	private int WORK_TYPE; //
	public int getSTAFF_NO() {
		return STAFF_NO;
	}

	public void setSTAFF_NO(int sTAFF_NO) {
		STAFF_NO = sTAFF_NO;
	}

	public String getREISTER_NAME() {
		return REISTER_NAME;
	}

	public void setREISTER_NAME(String rEISTER_NAME) {
		REISTER_NAME = rEISTER_NAME;
	}

	public int getCLINIQUE_CODE() {
		return CLINIQUE_CODE;
	}

	public void setCLINIQUE_CODE(int cLINIQUE_CODE) {
		CLINIQUE_CODE = cLINIQUE_CODE;
	}

	public int getPRICE() {
		return PRICE;
	}

	public void setPRICE(int pRICE) {
		PRICE = pRICE;
	}

	public String getDOCTOR_NAME() {
		return DOCTOR_NAME;
	}

	public void setDOCTOR_NAME(String dOCTOR_NAME) {
		DOCTOR_NAME = dOCTOR_NAME;
	}

	public String getWEB_INTRODUCE() {
		return WEB_INTRODUCE;
	}

	public void setWEB_INTRODUCE(String wEB_INTRODUCE) {
		WEB_INTRODUCE = wEB_INTRODUCE;
	}

	public String getZHAOPIAN() {
		return ZHAOPIAN;
	}

	public void setZHAOPIAN(String zHAOPIAN) {
		ZHAOPIAN = zHAOPIAN;
	}

	public int getWORK_TYPE() {
		return WORK_TYPE;
	}

	public void setWORK_TYPE(int wORK_TYPE) {
		WORK_TYPE = wORK_TYPE;
	}

	public String getDOCTOR() {
		return DOCTOR;
	}

	public void setDOCTOR(String dOCTOR) {
		DOCTOR = dOCTOR;
	}

	public int getPRE_ALLOW() {
		return PRE_ALLOW;
	}

	public void setPRE_ALLOW(int pRE_ALLOW) {
		PRE_ALLOW = pRE_ALLOW;
	}

	public List<String> getSchdList() {
		return schdList;
	}

	public void setSchdList(List<String> schdList) {
		this.schdList = schdList;
	}

	private String DOCTOR; //挂号用的医生标识
	private int PRE_ALLOW;
	/*"schdList":[\"2017-07-21_1\",\"2017-07-24_1\",\"2017-07-25_1\",\"2017-07-26_1\",\"2017-07-27_1\"*/
	private List<String> schdList; //排班表, 1:上午，2:下午
	
	BeanHospDeptDoctListRespItem(){super(BeanHospDeptDoctListRespItem.class.getSimpleName());}


	
}
