package com.healthyfish.healthyfish.POJO;

import java.util.List;

/**
 * 描述：自定义计划
 * 作者：LYQ on 2017/7/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanCustomPlan {

    private String dateAndWeek;//日期和星期
    private String time;//具体时间
    private List<String> individualPlan;//单项计划列表

    public String getDateAndWeek() {
        return dateAndWeek;
    }

    public void setDateAndWeek(String dateAndWeek) {
        this.dateAndWeek = dateAndWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getIndividualPlan() {
        return individualPlan;
    }

    public void setIndividualPlan(List<String> individualPlan) {
        this.individualPlan = individualPlan;
    }
}
