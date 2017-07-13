package com.healthyfish.healthyfish.utils;

/**
 * 描述：用来判断输入是否合法的工具类
 * 作者：LYQ on 2017/7/13.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class InputUtils {

    /**
     * 判断输入的性别是否合法
     * @param strGender 传入的性别字符串
     * @return
     */
    public static boolean isGender(String strGender){
        if (strGender.equals("男") || strGender.equals("女")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断输入的年龄是否合法
     * @param strAge 传入的年龄字符串
     * @return
     */
    public static boolean isAge(String strAge){
        int age = Integer.valueOf(strAge);
        if (age >0 && age <150){
            return true;
        }else {
            return false;
        }
    }
}
