package com.healthyfish.healthyfish.utils;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：日期加减工具类
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class DateUtils {

    public static String addAndSubtractDate(String ymd, int count) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        if (ymd.equals("Y")) {
            rightNow.add(Calendar.YEAR, count);//日期加1年
        } else if (ymd.equals("M")) {
            rightNow.add(Calendar.MONTH, count);//日期加1个月
        } else if (ymd.equals("D")) {
            rightNow.add(Calendar.DAY_OF_YEAR, count);//日期加10天
        }
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    /**
     * 获取当天日期并转换为想要的格式
     *
     * @param DateType 想要的日期格式
     * @return
     */
    public static String getCurrentDate(String DateType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateType);
        Date currentDate = new Date(System.currentTimeMillis());
        String strCurrentDate = dateFormat.format(currentDate);
        return strCurrentDate;
    }

    /**
     * 在点前日期的基础上加减日期，并返回想要的日期格式
     *
     * @param DateType 想要的日期格式
     * @param ymd      年月日
     * @param count    加减的天数
     * @return
     */
    public static String addOrSubtractDate(String DateType, String ymd, int count) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateType);
        Date date = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        if (ymd.equals("Y") || ymd.equals("y")) {
            rightNow.add(Calendar.YEAR, count);//日期加1年
        } else if (ymd.equals("M") || ymd.equals("m")) {
            rightNow.add(Calendar.MONTH, count);//日期加1个月
        } else if (ymd.equals("D") || ymd.equals("d")) {
            rightNow.add(Calendar.DAY_OF_YEAR, count);//日期加10天
        }
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    /**
     * 获取字符串日期是星期几
     *
     * @param DateType 想要的日期格式
     * @param dateStr  字符串日期
     * @return
     */
    public static String getWeekFromStr(String DateType, String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateType);//设置想要的日期格式
        Calendar calendar = Calendar.getInstance();//获取日历实例
        Date date = dateFormat.parse(dateStr, new ParsePosition(0));//反向操作，将字符格式的转化为日期格式
        calendar.setTime(date);//将转化回来的日期设置成当前的日期
        int number = calendar.get(Calendar.DAY_OF_WEEK);
        String[] str = {"", "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return str[number];
    }

}
