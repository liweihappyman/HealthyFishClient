package com.healthyfish.healthyfish.POJO;


/**
 * 描述：预约时间封装类
 * 作者：LYQ on 2017/7/22.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

//获取到的数据：
// [{"HZS":"2017-07-27 08:07:20","HID":"应启益/220_2017-07-27_1_08:07:20","SX":2},
// {"HZS":"2017-07-27 08:14:40","HID":"应启益/220_2017-07-27_1_08:14:40","SX":3},
// {"HZS":"2017-07-27 08:22:00","HID":"应启益/220_2017-07-27_1_08:22:00","SX":4},
// {"HZS":"2017-07-27 08:29:20","HID":"应启益/220_2017-07-27_1_08:29:20","SX":5},
// {"HZS":"2017-07-27 08:36:40","HID":"应启益/220_2017-07-27_1_08:36:40","SX":6},
// {"HZS":"2017-07-27 08:44:00","HID":"应启益/220_2017-07-27_1_08:44:00","SX":7},
// {"HZS":"2017-07-27 08:51:20","HID":"应启益/220_2017-07-27_1_08:51:20","SX":8},
// {"HZS":"2017-07-27 08:58:40","HID":"应启益/220_2017-07-27_1_08:58:40","SX":9},
// {"HZS":"2017-07-27 09:06:00","HID":"应启益/220_2017-07-27_1_09:06:00","SX":10},
// {"HZS":"2017-07-27 09:13:20","HID":"应启益/220_2017-07-27_1_09:13:20","SX":11},
// {"HZS":"2017-07-27 09:20:40","HID":"应启益/220_2017-07-27_1_09:20:40","SX":12},
// {"HZS":"2017-07-27 09:28:00","HID":"应启益/220_2017-07-27_1_09:28:00","SX":13},
// {"HZS":"2017-07-27 09:35:20","HID":"应启益/220_2017-07-27_1_09:35:20","SX":14},
// {"HZS":"2017-07-27 09:42:40","HID":"应启益/220_2017-07-27_1_09:42:40","SX":15},
// {"HZS":"2017-07-27 09:50:00","HID":"应启益/220_2017-07-27_1_09:50:00","SX":16},
// {"HZS":"2017-07-27 09:57:20","HID":"应启益/220_2017-07-27_1_09:57:20","SX":17},
// {"HZS":"2017-07-27 10:04:40","HID":"应启益/220_2017-07-27_1_10:04:40","SX":18},
// {"HZS":"2017-07-27 10:12:00","HID":"应启益/220_2017-07-27_1_10:12:00","SX":19},
// {"HZS":"2017-07-27 10:19:20","HID":"应启益/220_2017-07-27_1_10:19:20","SX":20},
// {"HZS":"2017-07-27 10:26:40","HID":"应启益/220_2017-07-27_1_10:26:40","SX":21},
// {"HZS":"2017-07-27 10:34:00","HID":"应启益/220_2017-07-27_1_10:34:00","SX":22},
// {"HZS":"2017-07-27 10:41:20","HID":"应启益/220_2017-07-27_1_10:41:20","SX":23},
// {"HZS":"2017-07-27 10:48:40","HID":"应启益/220_2017-07-27_1_10:48:40","SX":24},
// {"HZS":"2017-07-27 10:56:00","HID":"应启益/220_2017-07-27_1_10:56:00","SX":25},
// {"HZS":"2017-07-27 11:03:20","HID":"应启益/220_2017-07-27_1_11:03:20","SX":26},
// {"HZS":"2017-07-27 11:10:40","HID":"应启益/220_2017-07-27_1_11:10:40","SX":27},
// {"HZS":"2017-07-27 11:18:00","HID":"应启益/220_2017-07-27_1_11:18:00","SX":28},
// {"HZS":"2017-07-27 11:25:20","HID":"应启益/220_2017-07-27_1_11:25:20","SX":29},
// {"HZS":"2017-07-27 11:32:40","HID":"应启益/220_2017-07-27_1_11:32:40","SX":30}]

public class BeanHospRegNumListRespItem extends BeanBaseResp {
    private String HZS;
    private String HID;
    private int SX;

    public String getHZS() {
        return HZS;
    }

    public void setHZS(String HZS) {
        this.HZS = HZS;
    }

    public String getHID() {
        return HID;
    }

    public void setHID(String HID) {
        this.HID = HID;
    }

    public int getSX() {
        return SX;
    }

    public void setSX(int SX) {
        this.SX = SX;
    }
}
