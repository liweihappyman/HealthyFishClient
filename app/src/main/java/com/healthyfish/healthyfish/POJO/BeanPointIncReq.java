package com.healthyfish.healthyfish.POJO;

//增加用户的积分，每天增加1分，允许提前5分钟进行
//正常情况返回会员的积分
//-1表示用户未登录
//-2表示会话id错误
//其它负数值表示下一次增长的时间，以毫秒为单位，用于app同步更新时间。例如-20000表示离下一次增长还有20秒
public class BeanPointIncReq extends BeanBaseReq {
	public BeanPointIncReq(){super(BeanPointIncReq.class.getSimpleName());}
}
