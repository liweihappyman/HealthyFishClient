package com.healthyfish.healthyfish.POJO;

/**
 * 描述：个人信息类
 * 作者：LYQ on 2017/7/25.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanPersonalInformation {

    private String phone;//手机号
    private String name;//姓名
    private String nickname;//昵称
    private String imgUrl;//头像
    private String gender;//性别
    private String birthDate;//出生日期
    private String idCard;//身份证号


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        birthDate = birthDate;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
