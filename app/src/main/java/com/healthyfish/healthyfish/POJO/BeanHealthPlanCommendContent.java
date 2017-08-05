package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：推荐的健康计划的数据，以及处理后的数据
 * 作者：WKJ on 2017/8/4.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanHealthPlanCommendContent extends DataSupport{
    private String weekJsonStr ;//星期:   一、二、...
    private String dateJsonStr; //号数，如2,3...
    private String calendarDateJsonStr ;//字符串的日期2017年10月20日
    private String hotPlanListJsonStr ;

    public String getWeekJsonStr() {
        return weekJsonStr;
    }

    public void setWeekJsonStr(String weekJsonStr) {
        this.weekJsonStr = weekJsonStr;
    }

    public String getDateJsonStr() {
        return dateJsonStr;
    }

    public void setDateJsonStr(String dateJsonStr) {
        this.dateJsonStr = dateJsonStr;
    }

    public String getCalendarDateJsonStr() {
        return calendarDateJsonStr;
    }

    public void setCalendarDateJsonStr(String calendarDateJsonStr) {
        this.calendarDateJsonStr = calendarDateJsonStr;
    }

    public String getHotPlanListJsonStr() {
        return hotPlanListJsonStr;
    }

    public void setHotPlanListJsonStr(String hotPlanListJsonStr) {
        this.hotPlanListJsonStr = hotPlanListJsonStr;
    }
}
