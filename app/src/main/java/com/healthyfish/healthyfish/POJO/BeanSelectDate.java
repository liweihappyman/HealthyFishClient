package com.healthyfish.healthyfish.POJO;

/**
 * 描述：自定义计划选择养生日期类
 * 作者：LYQ on 2017/8/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanSelectDate {
    private String date;
    private boolean isCheck;

    public BeanSelectDate(String date, boolean isCheck) {
        this.date = date;
        this.isCheck = isCheck;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
