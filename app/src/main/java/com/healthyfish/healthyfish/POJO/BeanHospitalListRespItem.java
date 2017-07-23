package com.healthyfish.healthyfish.POJO;

//返回数据：
//{"code":0,"hospitalList":[{"address":"柳州市解放北路32号","code":0,"id":"lzzyy","img":"/demo/img/lzzyy.jpg","intr":"广西中医药大学第三附属医院, 广西中医药大学第三临床医学院","level":"三级医院","name":"柳州市中医院","ver":"0.1"}],"ver":"0.1"}

public class BeanHospitalListRespItem extends BeanBaseResp {
//	private String url; //医院详细介绍的链接
	private String name; //名称
	private String level; //等级：三级甲等
	private String address; //地址
	private String intr; //简介
	private String img;
	

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getIntr() {
		return intr;
	}
	public void setIntr(String intr) {
		this.intr = intr;
	}
//	public String getUrl() {return url;}
//	public void setUrl(String url) {this.url = url;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getAddress() {return address;}
	public void setAddress(String address) {this.address = address;}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
