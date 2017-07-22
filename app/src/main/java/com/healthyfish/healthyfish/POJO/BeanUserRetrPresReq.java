package com.healthyfish.healthyfish.POJO;

//查询已有的电子处方
/*
	var bean = {
		act: 'BeanUserListValueReq',
		ver: '0.1',
		prefix: "pres_<%= uid %>_",
		from: 0,
		num: -1,
		to: -1
	} 
*/

//获取新的电子处方

//返回
/*
//[{"PRESCRIBE_OPERATOR":"杨坚毅/142","DEPT_NAME":"心病科门诊","RESCRIBE_STATUS":"已执行","SICK_ID":"0000281122","SEX":"男","PRESCRIPTION_NUMBER":"12953414","ITEM_CLASS":"西药","APPLY_DEPT":"2216","SICK_NAME":"邹玉贵","DIAGNOSIS_NAME":"高血压(慢)","AGE":"66","WRITE_TIME":"2016-12-15T09:00:35+08:00"},{"PRESCRIBE_OPERATOR":"杨坚毅/142","DEPT_NAME":"心病科门诊","RESCRIBE_STATUS":"已执行","SICK_ID":"0000281122","SEX":"男","PRESCRIPTION_NUMBER":"12870968","ITEM_CLASS":"西药","APPLY_DEPT":"2216","SICK_NAME":"邹玉贵","DIAGNOSIS_NAME":"高血压(慢)","AGE":"66","WRITE_TIME":"2016-12-01T11:28:08+08:00"}]
	var elem = objList[x];
	(elem.DIAGNOSIS_NAME);
	(elem.SICK_NAME);
	(elem.SEX);
	(elem.AGE);
	(elem.DEPT_NAME);
	var doctIndex = elem.PRESCRIBE_OPERATOR.indexOf('/');
	$obj.find('.PRESCRIBE_OPERATOR').text(elem.PRESCRIBE_OPERATOR.substr(0,doctIndex));
	$obj.find('.reReg').prop('href', '../test/reg_doct_detail.jsp?hosp=lzzyy&dept='+elem.APPLY_DEPT+'&capt='+encodeURI(elem.DEPT_NAME)+'&staffNo='+elem.PRESCRIBE_OPERATOR.substr(Number(doctIndex)+Number(1))+'&doct='+encodeURI(elem.PRESCRIBE_OPERATOR));  
	for(var y in elem.presList){
		var item = elem.presList[y];
	    var str =  item.PHYSIC_NAME;
	}
	(elem.SICK_ID); // for filter
 
//[{"LAY_PHYSIC_DAYS":"14","PRICE":"44.35","FREQ_DESCRIBE":"q.d.","LAY_PHYSIC_QUANTITY":"14","PACK_SPEC":"10mg*7","PHYSIC_DOSEAGE":"10","STATUS":"已完成","COST":"88.70","USAGE":"口服","SICK_ID":"0000281122","DOSE_UNIT":"mg","PHYSIC_NAME":"瑞舒伐他汀钙片","PRESCRIBE_NUMBER":"12870968","TAKE_MEDICINE_WAYS_CODE":"Y020","PHYSIC_UNIT":"片","MODIFY_TIME":"2016-12-01T11:28:09+08:00"}]                    
	if(item.FREQ_DESCRIBE=='q.d.'){
		freqStr='每天一次';
	}
	(item.PHYSIC_NAME);
	(item.USAGE);
	(item.LAY_PHYSIC_DAYS);
	(item.LAY_PHYSIC_QUANTITY);
	(item.PACK_SPEC);
	(item.PHYSIC_DOSEAGE);
	(item.DOSE_UNIT);
	(item.PHYSIC_UNIT);
 */
public class BeanUserRetrPresReq extends BeanBaseReq {
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

	public BeanUserRetrPresReq(){super(BeanUserRetrPresReq.class.getSimpleName());}
	
	public String getHosp() {
		return hosp;
	}

	public void setHosp(String hosp) {
		this.hosp = hosp;
	}

}
