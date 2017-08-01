package com.healthyfish.healthyfish.POJO;

/**
 * 描述：我的挂号列表信息封装类
 * 作者：LYQ on 2017/8/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanMyAppointmentItem {

    private String respKey;//挂号成功返回的key
    private String imgUrl;//医生头像
    private String doctorName;//医生姓名
    private String consultationRoom;//诊室
    private String dutise;//医生门诊类型
    private String hospital;//医院
    private String appointmentTime;//预约时间
    private String visitingPerson;//就诊人


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

    public String getDutise() {
        return dutise;
    }

    public void setDutise(String dutise) {
        this.dutise = dutise;
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
}
