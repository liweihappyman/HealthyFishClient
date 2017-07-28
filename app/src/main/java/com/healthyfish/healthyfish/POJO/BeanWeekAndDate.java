package com.healthyfish.healthyfish.POJO;

import java.io.Serializable;

/**
 * 描述：选择预约时间星期的展示
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanWeekAndDate implements Serializable{

    private BeanDoctorInfo beanDoctorInfo;
    private BeanHospRegisterReq beanHospRegisterReq;
    //private BeanHospDeptDoctListRespItem beanHospDeptDoctListRespItem;
    private String time;//预约时间
    private String titleWeek;
    private String Date;

    public BeanWeekAndDate(BeanDoctorInfo beanDoctorInfo, String date, String am, String pm) {
        this.beanDoctorInfo = beanDoctorInfo;
        Date = date;
        this.am = am;
        this.pm = pm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String am;
    private String pm;
    private boolean outTime = false;//是否过时

//    public BeanWeekAndDate(BeanHospRegisterReq beanHospRegisterReq, BeanHospDeptDoctListRespItem beanHospDeptDoctListRespItem,String date, String am, String pm) {
//        this.beanHospRegisterReq = beanHospRegisterReq;
//        this.beanHospDeptDoctListRespItem = beanHospDeptDoctListRespItem;
//        Date = date;
//        this.am = am;
//        this.pm = pm;
//    }


    public boolean isOutTime() {
        return outTime;
    }

    public void setOutTime(boolean outTime) {
        this.outTime = outTime;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getTitleWeek() {
        return titleWeek;
    }
    public void setTitleWeek(String titleWeek) {
        this.titleWeek = titleWeek;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public BeanWeekAndDate(String titleWeek, String date, String am, String pm,boolean outTime) {
        this.titleWeek = titleWeek;
        Date = date;
        this.am = am;
        this.pm = pm;
        this.outTime = outTime;
    }

    public BeanWeekAndDate(String titleWeek, String date) {
        this.titleWeek = titleWeek;
        Date = date;
    }


    public BeanWeekAndDate(String date, String am, String pm) {
        Date = date;
        this.am = am;
        this.pm = pm;
    }

    public BeanWeekAndDate() {
    }

    public BeanHospRegisterReq getBeanHospRegisterReq() {
        return beanHospRegisterReq;
    }

    public void setBeanHospRegisterReq(BeanHospRegisterReq beanHospRegisterReq) {
        this.beanHospRegisterReq = beanHospRegisterReq;
    }

//    public BeanHospDeptDoctListRespItem getBeanHospDeptDoctListRespItem() {
//        return beanHospDeptDoctListRespItem;
//    }
//
//    public void setBeanHospDeptDoctListRespItem(BeanHospDeptDoctListRespItem beanHospDeptDoctListRespItem) {
//        this.beanHospDeptDoctListRespItem = beanHospDeptDoctListRespItem;
//    }

    public BeanDoctorInfo getBeanDoctorInfo() {
        return beanDoctorInfo;
    }

    public void setBeanDoctorInfo(BeanDoctorInfo beanDoctorInfo) {
        this.beanDoctorInfo = beanDoctorInfo;
    }
}
