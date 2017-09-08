package com.healthyfish.healthyfish.POJO;

//查询用户的积分
//正常情况返回会员的积分
//-1表示用户未登录
public class BeanPointQueryReq extends BeanBaseReq {
	public BeanPointQueryReq(){super(BeanPointQueryReq.class.getSimpleName());}
}
