package com.healthyfish.healthyfish.POJO;

import java.util.List;

/**
 * 描述：
 * 作者：WKJ on 2017/7/21.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanPrescriptiom {

    /**
     * PRESCRIBE_OPERATOR : 杨坚毅/142
     * DEPT_NAME : 心病科门诊
     * SEX : 男
     * ITEM_CLASS : 西药
     * APPLY_DEPT : 2216
     * SICK_NAME : 邹玉贵
     * DIAGNOSIS_NAME : 高血压(慢)
     * WRITE_TIME : 2016-12-15T09:00:35+08:00
     * presList : [{"LAY_PHYSIC_DAYS":"14","PRICE":"44.35","FREQ_DESCRIBE":"q.d.","LAY_PHYSIC_QUANTITY":"14","PACK_SPEC":"10mg*7","PHYSIC_DOSEAGE":"10","STATUS":"已完成","COST":"88.70","USAGE":"口服","SICK_ID":"0000281122","DOSE_UNIT":"mg","PHYSIC_NAME":"瑞舒伐他汀钙片","PRESCRIBE_NUMBER":"12953414","TAKE_MEDICINE_WAYS_CODE":"Y020","PHYSIC_UNIT":"片","MODIFY_TIME":"2016-12-15T09:00:36+08:00"}]
     * RESCRIBE_STATUS : 已执行
     * SICK_ID : 0000281122
     * PRESCRIPTION_NUMBER : 12953414
     * key : pres_null_lzzyy_12953414
     * AGE : 66
     */

    private String PRESCRIBE_OPERATOR;
    private String DEPT_NAME;
    private String SEX;
    private String ITEM_CLASS;
    private String APPLY_DEPT;
    private String SICK_NAME;
    private String DIAGNOSIS_NAME;
    private String WRITE_TIME;
    private String RESCRIBE_STATUS;
    private String SICK_ID;
    private String PRESCRIPTION_NUMBER;
    private String key;
    private String AGE;
    private List<BeanPresList> presList;

    public String getPRESCRIBE_OPERATOR() {
        return PRESCRIBE_OPERATOR;
    }

    public void setPRESCRIBE_OPERATOR(String PRESCRIBE_OPERATOR) {
        this.PRESCRIBE_OPERATOR = PRESCRIBE_OPERATOR;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getITEM_CLASS() {
        return ITEM_CLASS;
    }

    public void setITEM_CLASS(String ITEM_CLASS) {
        this.ITEM_CLASS = ITEM_CLASS;
    }

    public String getAPPLY_DEPT() {
        return APPLY_DEPT;
    }

    public void setAPPLY_DEPT(String APPLY_DEPT) {
        this.APPLY_DEPT = APPLY_DEPT;
    }

    public String getSICK_NAME() {
        return SICK_NAME;
    }

    public void setSICK_NAME(String SICK_NAME) {
        this.SICK_NAME = SICK_NAME;
    }

    public String getDIAGNOSIS_NAME() {
        return DIAGNOSIS_NAME;
    }

    public void setDIAGNOSIS_NAME(String DIAGNOSIS_NAME) {
        this.DIAGNOSIS_NAME = DIAGNOSIS_NAME;
    }

    public String getWRITE_TIME() {
        return WRITE_TIME;
    }

    public void setWRITE_TIME(String WRITE_TIME) {
        this.WRITE_TIME = WRITE_TIME;
    }

    public String getRESCRIBE_STATUS() {
        return RESCRIBE_STATUS;
    }

    public void setRESCRIBE_STATUS(String RESCRIBE_STATUS) {
        this.RESCRIBE_STATUS = RESCRIBE_STATUS;
    }

    public String getSICK_ID() {
        return SICK_ID;
    }

    public void setSICK_ID(String SICK_ID) {
        this.SICK_ID = SICK_ID;
    }

    public String getPRESCRIPTION_NUMBER() {
        return PRESCRIPTION_NUMBER;
    }

    public void setPRESCRIPTION_NUMBER(String PRESCRIPTION_NUMBER) {
        this.PRESCRIPTION_NUMBER = PRESCRIPTION_NUMBER;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public List<BeanPresList> getPresList() {
        return presList;
    }

    public void setPresList(List<BeanPresList> presList) {
        this.presList = presList;
    }
}
