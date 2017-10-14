package com.healthyfish.healthyfish.utils;

import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;

import java.util.Comparator;

/**
 * 医生职称排序
 * Created by asus on 2017/10/13.
 */

public class DoctorPostComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        BeanHospDeptDoctListRespItem bean1 = (BeanHospDeptDoctListRespItem) o1;
        BeanHospDeptDoctListRespItem bean2 = (BeanHospDeptDoctListRespItem) o2;
        if (bean1.getPRICE() < bean2.getPRICE()) {//当返回1时大于小于号分别表示升序和降序，当返回-1时反之，需要配合才能决定排序方向
            return 1;
        } else if (bean1.getPRICE() == bean2.getPRICE()) {
            if (bean1.getREISTER_NAME().substring(0, 1).equals("副")) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
