package com.healthyfish.healthyfish.POJO;

/**
 * 描述：首页搜索结果封装类
 * 作者：LYQ on 2017/10/25.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanHomeSearchResult {

    public static final String SEARCH_TYPE_HOSP = "hosp";
    public static final String SEARCH_TYPE_NEWS = "news";

    private String key;
    private String title;
    private String type;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
