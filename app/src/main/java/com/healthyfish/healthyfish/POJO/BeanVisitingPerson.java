package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 描述：就诊人，挂号用
 * 作者：LYQ on 2017/7/23.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanVisitingPerson extends DataSupport implements Serializable {

    private int id;//数据库自动生成的id
    private String phoneId;//主键（用户uid,手机号）
    private String hospital;//医院
    private String visitingPerson;//就诊人姓名
    private String phoneNumber;//手机号
    private String visitingCard;//就诊卡号
    private String IDCard;//身份证号
    private String sick_id;//患者在医院的内部编号（验证就诊卡返回的数据）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getVisitingPerson() {
        return visitingPerson;
    }

    public void setVisitingPerson(String visitingPerson) {
        this.visitingPerson = visitingPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVisitingCard() {
        return visitingCard;
    }

    public void setVisitingCard(String visitingCard) {
        this.visitingCard = visitingCard;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getSick_id() {
        return sick_id;
    }

    public void setSick_id(String sick_id) {
        this.sick_id = sick_id;
    }

}
