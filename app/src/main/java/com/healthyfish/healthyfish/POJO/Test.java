package com.healthyfish.healthyfish.POJO;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 作者：WKJ on 2017/7/12.
 * 邮箱：
 * 编辑：WKJ
 */

public class Test implements Serializable{
    private int type;

    public Test(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
