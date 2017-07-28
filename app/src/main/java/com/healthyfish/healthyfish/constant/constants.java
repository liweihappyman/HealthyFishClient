package com.healthyfish.healthyfish.constant;

import com.healthyfish.healthyfish.POJO.BeanMedRec;

/**
 * 描述：常数
 * 作者：Wayne on 2017/6/27 15:25
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class constants {

    // OkHttpClient设置连接超时时间
    public final static int CONNECT_TIMEOUT = 5000;
    // OkHttpClient设置读取超时时间
    public final static int READ_TIMEOUT = 5000;
    // OkHttpClient设置写的超时时间
    public final static int WRITE_TIMEOUT = 5000;

    public static int POSITION_MED_REC = -1;//病历夹是从item点击进入，还是点击新建病历夹进入
    public static String NEW_MED_REC = "save";//标志病历夹是要更新还是直接保存
    public static int POSITION_COURSE = -1;//标志是否从病程的item进入，item进入可以更新，否则回到病历页面保存



    // 网络访问服务器主机地址
    public final static String HttpHealthyFishyUrl = "http://www.kangfish.cn";
    public final static String HttpsHealthyFishyUrl = "https://www.kangfish.cn";

}
