package com.healthyfish.healthyfish.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：Wayne on 2017/9/29 10:25
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class BeanUserPhyIdReq2 extends BeanBaseReq {
    private List<Integer> phyList = new ArrayList<Integer>(); //体质类型

    //用户问卷的答案，null或者[]代表用户已进行红外皮温测试而直接选择了体质类型
    private List<Integer> ansList = new ArrayList<Integer>();

    BeanUserPhyIdReq2() {
        super(BeanUserPhyIdReq2.class.getSimpleName());
    }

    public List<Integer> getAnsList() {
        return ansList;
    }
}