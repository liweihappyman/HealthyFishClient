package com.healthyfish.healthyfish.POJO;

//验证就诊卡
//返回sick_id(患者在医院的内部编号，不同与card_id)
public class BeanHospCardAuthReq extends BeanBaseReq {
	private String hosp;
	private String cardId;
	private String name;
				
	BeanHospCardAuthReq(){super(BeanHospCardAuthReq.class.getSimpleName());}
	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHosp() {
		return hosp;
	}

	public void setHosp(String hosp) {
		this.hosp = hosp;
	}

}
