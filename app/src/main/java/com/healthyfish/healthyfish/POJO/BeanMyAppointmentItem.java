package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：我的挂号列表信息封装类
 * 作者：LYQ on 2017/8/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanMyAppointmentItem extends DataSupport implements Serializable {

    private int id;
    private String respKey;//挂号成功返回的key
    private String imgUrl;//医生头像
    private String doctorName;//医生姓名
    private String consultationRoom;//诊室
    private String duties;//医生门诊类型
    private String hospital;//医院
    private String appointmentTime;//预约时间
    private String visitingPerson;//就诊人
    private String department;//科室

    private String introduce;  //医生简介
    private String price;  //医生问诊价格
    private String hosp;//医院编号"lzzyy"
    private String dept;//部门编号
    private int STAFF_NO; //员工编号
    private int WORK_TYPE; //
    private String DOCTOR; //挂号用的医生标识
    private int PRE_ALLOW;
    private List<String> schdList; //排班表, 1:上午，2:下午

    private boolean isPast;//挂号是否过期

    public BeanMyAppointmentItem() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRespKey() {
        return respKey;
    }

    public void setRespKey(String respKey) {
        this.respKey = respKey;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(String consultationRoom) {
        this.consultationRoom = consultationRoom;
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

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getVisitingPerson() {
        return visitingPerson;
    }

    public void setVisitingPerson(String visitingPerson) {
        this.visitingPerson = visitingPerson;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public int getSTAFF_NO() {
        return STAFF_NO;
    }

    public void setSTAFF_NO(int STAFF_NO) {
        this.STAFF_NO = STAFF_NO;
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

    public boolean isPast() {
        return isPast;
    }

    public void setPast(boolean past) {
        isPast = past;
    }
}
