package com.healthyfish.healthyfish.POJO;

import java.util.ArrayList;
import java.util.List;

public class BeanNewsResp extends BeanBaseResp {
	
	private List<BeanItemNewsAbstract> newsList = new ArrayList<BeanItemNewsAbstract>();

	public List<BeanItemNewsAbstract> getNewsList() {return newsList;}
	public void setNewsList(List<BeanItemNewsAbstract> newsList) {this.newsList = newsList;}
	
}
