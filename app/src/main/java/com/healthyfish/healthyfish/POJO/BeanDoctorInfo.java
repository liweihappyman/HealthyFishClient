package com.healthyfish.healthyfish.POJO;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：医生详情实体类
 * 作者：LYQ on 2017/7/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanDoctorInfo implements Serializable{

    private String imgUrl;  //医生头像地址或者路径
    private String name;  //医生姓名
    private String department;  //医生所在科室
    private String duties;  //医生职务
    private String hospital;  //医生所在医院
    private String introduce;  //医生简介
    private String price;  //医生问诊价格

    private String hosp;//医院编号"lzzyy"
    private String dept;//部门编号
    private int STAFF_NO; //员工编号
    private int CLINIQUE_CODE; //诊室
    private int WORK_TYPE; //
    private String DOCTOR; //挂号用的医生标识
    private int PRE_ALLOW;
    private List<String> schdList; //排班表, 1:上午，2:下午


    public BeanDoctorInfo() {
    }

    public BeanDoctorInfo(String imgUrl, String name, String department, String duties, String hospital, String introduce, String price) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.department = department;
        this.duties = duties;
        this.hospital = hospital;
        this.introduce = introduce;
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSTAFF_NO() {
        return STAFF_NO;
    }

    public void setSTAFF_NO(int STAFF_NO) {
        this.STAFF_NO = STAFF_NO;
    }

    public int getCLINIQUE_CODE() {
        return CLINIQUE_CODE;
    }

    public void setCLINIQUE_CODE(int CLINIQUE_CODE) {
        this.CLINIQUE_CODE = CLINIQUE_CODE;
    }

    public int getWORK_TYPE() {
        return WORK_TYPE;
    }

    public void setWORK_TYPE(int WORK_TYPE) {
        this.WORK_TYPE = WORK_TYPE;
    }

    public String getDOCTOR() {
        return DOCTOR;
    }

    public void setDOCTOR(String DOCTOR) {
        this.DOCTOR = DOCTOR;
    }

    public int getPRE_ALLOW() {
        return PRE_ALLOW;
    }

    public void setPRE_ALLOW(int PRE_ALLOW) {
        this.PRE_ALLOW = PRE_ALLOW;
    }

    public List<String> getSchdList() {
        return schdList;
    }

    public void setSchdList(List<String> schdList) {
        this.schdList = schdList;
    }

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
}
