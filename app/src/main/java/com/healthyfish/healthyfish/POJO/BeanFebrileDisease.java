package com.healthyfish.healthyfish.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */
public class BeanFebrileDisease {
	private String key;
	private String title;
    private String disease;
    private String treKey;
    
    
	public String getTreKey() {
		return treKey;
	}
	public void setTreKey(String treKey) {
		this.treKey = treKey;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
    public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
