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
    public List<BeanWeekAndDate> getList() {
        return list;
    }

    public void setList(List<BeanWeekAndDate> list) {
        this.list = list;
    }

    private List<BeanWeekAndDate> list ;

}
