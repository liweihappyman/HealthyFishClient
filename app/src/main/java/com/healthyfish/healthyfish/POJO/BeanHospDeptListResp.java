package com.healthyfish.healthyfish.POJO;

import java.util.List;
/*
把这三个课时排在前面
"脾胃病科门诊"
"肝病门诊"
"中医预防保健科"
*/
public class BeanHospDeptListResp extends BeanBaseResp {
	private List<BeanHospDeptListRespItem> hospDeptList;

	public List<BeanHospDeptListRespItem> getHospDeptList() {
		return hospDeptList;
	}

	public void setHospDeptList(List<BeanHospDeptListRespItem> hospDeptList) {
		this.hospDeptList = hospDeptList;
	}

}
