package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：用户体质报告
 * 作者：LYQ on 2017/8/3.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanUserPhy extends DataSupport implements Serializable {

    private int id;
    private String uid;
    private String jsonStrPhysicalList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getJsonStrPhysicalList() {
        return jsonStrPhysicalList;
    }

    public void setJsonStrPhysicalList(String jsonStrPhysicalList) {
        this.jsonStrPhysicalList = jsonStrPhysicalList;
    }
}
