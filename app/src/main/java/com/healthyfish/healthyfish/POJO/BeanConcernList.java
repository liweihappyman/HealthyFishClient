package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

/**
 * 描述：
 * 作者：LYQ on 2017/7/27.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanConcernList extends DataSupport {
    private int id;
    private String key;//已关注的key

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
