package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class BeanUserPhysical {
	public BeanUserPhysical(){}

	public BeanUserPhysical(String t, String d, String s){
		title = t;
		desc = d;
		suggest = s;
	}

	private String title;
	private String desc;
	private String suggest;

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
