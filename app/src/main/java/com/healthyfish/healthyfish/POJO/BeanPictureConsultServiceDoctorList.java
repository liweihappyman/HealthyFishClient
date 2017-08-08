package com.healthyfish.healthyfish.POJO;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 描述：添加过图文资讯的医生列表
 * 作者：Wayne on 2017/8/7 22:02
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class BeanPictureConsultServiceDoctorList extends DataSupport{
    private int id;
    @Column(unique = true)
    private String DoctorNumber;

    private String DoctorName;

    private String DoctorHostipal;

    private String DoctorPortrait;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorNumber() {
        return DoctorNumber;
    }

    public void setDoctorNumber(String doctorNumber) {
        DoctorNumber = doctorNumber;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorHostipal() {
        return DoctorHostipal;
    }

    public void setDoctorHostipal(String doctorHostipal) {
        DoctorHostipal = doctorHostipal;
    }

    public String getDoctorPortrait() {
        return DoctorPortrait;
    }

    public void setDoctorPortrait(String doctorPortrait) {
        DoctorPortrait = doctorPortrait;
    }
}
