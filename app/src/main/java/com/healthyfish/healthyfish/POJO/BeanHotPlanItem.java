package com.healthyfish.healthyfish.POJO;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 作者：WKJ on 2017/8/3.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanHotPlanItem extends DataSupport implements Serializable{

    /**
     * title : 艾灸
     * description : 一周的艾灸
     * todoList : [{"progress":"1","todo":"艾灸","done":false,"state":false},{"progress":"3","todo":"艾灸","done":false,"state":false},{"progress":"7","todo":"艾灸","done":false,"state":false}]
     */

    private String title;
    private String description;
    private List<TodoListBean> todoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TodoListBean> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<TodoListBean> todoList) {
        this.todoList = todoList;
    }

    public static class TodoListBean implements Serializable{


        /**
         * progress : 1
         * todo : 艾灸
         * done : false
         * state : false
         */

        protected String progress;
        protected String todo;
        protected boolean done;
        protected boolean state;

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getTodo() {
            return todo;
        }

        public void setTodo(String todo) {
            this.todo = todo;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }
}
