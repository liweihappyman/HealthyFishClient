package com.healthyfish.healthyfish.POJO;

/**
 * 描述：单项计划bean
 * 作者：WKJ on 2017/7/16.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanSinglePlan {
    private String type;//针灸、艾灸...
    private String title;//。。。养生计划
    private int progress;//完成次数
    private boolean todo = false;//是否完成的状态

    public BeanSinglePlan(String type, String title, int progress, boolean todo) {
        this.type = type;
        this.title = title;
        this.progress = progress;
        this.todo = todo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isTodo() {
        return todo;
    }

    public void setTodo(boolean todo) {
        this.todo = todo;
    }
}
