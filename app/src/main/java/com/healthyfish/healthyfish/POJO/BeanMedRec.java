package com.healthyfish.healthyfish.POJO;

/**
 * 描述：
 * 作者： TMXK on 2017/7/1.
 */

public class BeanMedRec {
    protected String lable;//标签
    protected String name;//姓名
    protected String gender;//性别
    protected String birthday;//出生日期
    protected String IDno;//身份证号码
    protected String occupation;//职业
    protected String marital_status;//婚姻情况
    protected String diagnose;//诊断
    protected String diseaseInfor;//病情信息
    protected String clinicDepartement;//就诊科室
    protected String cliniTime;//就诊时间
    protected boolean state;//记录是自己的还是医生改过的状态  false表示是自己的，true表示改过的
//  protected  String CourseOfDiseaseInfo;//病程信息
//

//
//        里面包括：
//        病程信息里的String是List<AddCourseOfDiseaseBean>打包成的JsonStr;
//        public static String menu;//菜单选项
//        public static String recordsOfPatients;//记录患者病情
//        public static List<String> paths;//患者病情图片
//        public static String date;//日期
    public boolean isState() {
    return state;
}

    public void setState(boolean state) {
        this.state = state;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getLable() {
        return lable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setIDno(String IDno) {
        this.IDno = IDno;
    }

    public String getIDno() {
        return IDno;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiseaseInfor(String diseaseInfor) {
        this.diseaseInfor = diseaseInfor;
    }

    public String getDiseaseInfor() {
        return diseaseInfor;
    }

    public void setClinicDepartement(String clinicDepartement) {
        this.clinicDepartement = clinicDepartement;
    }

    public String getClinicDepartement() {
        return clinicDepartement;
    }

    public void setCliniTime(String cliniTime) {
        this.cliniTime = cliniTime;
    }

    public String getCliniTime() {
        return cliniTime;
    }

}
