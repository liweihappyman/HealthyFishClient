package com.healthyfish.healthyfish.POJO;

//响应用BeanListKeyValueResp.java
//key: hosp_lzzyy_ds_doct_dept
//value: schdList

//当dept为空时的响应：[{"key":"hosp_lzzyy_ds_186_27000","value":"[\\\"2017-09-29_1\\\"]"},{"key":"hosp_lzzyy_ds_186_23300","value":"[\\\"2017-09-27_1\\\"]"}]
//当dept不为空时的响应：[{"key":"hosp_lzzyy_ds_186_27000","value":"[\\\"2017-09-29_1\\\"]"}]

public class BeanHospDoctMoreSchdReq extends BeanBaseReq {
    private String hosp;
    private String dept; //注意：排除的部门。要所有部门的排班，此字段设置为""
    private String staffNo;

    public BeanHospDoctMoreSchdReq() {
        super(BeanHospDoctMoreSchdReq.class.getSimpleName());
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

}
