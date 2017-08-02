package com.healthyfish.healthyfish.POJO;

public class BeanUserPhysical {
	public BeanUserPhysical(){}

	public BeanUserPhysical(String t, String d, String s){
		title = t;
		desc = d;
		suggest = s;
	}
	public String title;
	public String desc;
	public String suggest;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
}
