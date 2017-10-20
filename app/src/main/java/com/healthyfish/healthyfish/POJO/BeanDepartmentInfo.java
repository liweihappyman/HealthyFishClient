package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

/**
 * 医院科室信息封装类，用来保存科室信息
 * Created by asus on 2017/10/19.
 */

public class BeanDepartmentInfo extends DataSupport {

    private int id; //数据库生成的唯一id
    private String key; //格式为hosp+"_"+dept_code
    private String hospital; //医院名
    private String departmentName; //部门名

    public BeanDepartmentInfo() {
    }

    public BeanDepartmentInfo(String key, String departmentName) {
        this.key = key;
        this.departmentName = departmentName;
    }

    public BeanDepartmentInfo(String key, String hospital, String departmentName) {
        this.key = key;
        this.hospital = hospital;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

}
