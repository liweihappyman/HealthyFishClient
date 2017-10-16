package com.healthyfish.healthyfish.POJO;

import java.util.ArrayList;
import java.util.List;

public class BeanListKeyValueResp extends BeanBaseResp {
    private List<BeanKeyValue> resultList = new ArrayList<BeanKeyValue>();

    public List<BeanKeyValue> getResultList() {
        return resultList;
    }

    public void setResultList(List<BeanKeyValue> resultList) {
        this.resultList = resultList;
    }
}
