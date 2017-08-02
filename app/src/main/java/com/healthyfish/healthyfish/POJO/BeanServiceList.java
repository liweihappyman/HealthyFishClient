package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;


import java.io.Serializable;


/**
 * 描述：用户已购买的服务列表
 * 作者：LYQ on 2017/7/31.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */


public class BeanServiceList extends DataSupport implements Serializable{
    private int id;//数据库id
    private String key;//唯一的key（格式为：type_uid_hosp_dept_staffNo）
    private String type;//服务类型（图文咨询：GraphicConsultation ；私人医生：PrivateDoctor ）

    private String startTime;//起始时间
    private String endTime;//截止时间

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
