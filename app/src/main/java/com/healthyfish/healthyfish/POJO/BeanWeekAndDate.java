package com.healthyfish.healthyfish.POJO;

/**
 * 描述：选择预约时间头部星期的展示
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanWeekAndDate {
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

    private String titleWeek;
    private String Date;

    public BeanWeekAndDate(String titleWeek, String date) {
        this.titleWeek = titleWeek;
        Date = date;
    }
    public BeanWeekAndDate(String titleWeek) {
        this.titleWeek = titleWeek;
    }
    public BeanWeekAndDate() {
    }
}
