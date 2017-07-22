package com.healthyfish.healthyfish.POJO;
//查询已有的检查报告，返回报告的list
/*
	var bean = {
		act: 'BeanUserListValueReq',
		ver: '0.1',
		prefix: "rept_<%= uid %>_",
		from: 0,
		num: -1,
		to: -1
	} 
*/

//获取新的检查报告

//返回报告的List
/*
	(elem.ITEM_NAME);
	(elem.PATIENT_NAME);
	(elem.REQUESTED_DATE);
	(elem.PATIENT_ID);
	for(var x in elem.testList){
		var item = elem.testList[x];
	    item.ITEM_NAME
        item.RESULT
    	item.UNITS
	    item.REFERENCE_RANGE
	}
*/
public class BeanUserRetrReptReq extends BeanBaseReq {
	private String user;
	private String hosp;
	private String sickId;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSickId() {
		return sickId;
	}

	public void setSickId(String sickId) {
		this.sickId = sickId;
	}

	BeanUserRetrReptReq(){super(BeanUserRetrReptReq.class.getSimpleName());}
	
	public String getHosp() {
		return hosp;
	}

	public void setHosp(String hosp) {
		this.hosp = hosp;
	}

}
