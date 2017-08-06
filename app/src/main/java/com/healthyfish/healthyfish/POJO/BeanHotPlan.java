package com.healthyfish.healthyfish.POJO;

/**
 * 描述：健康计划的热门计划
 * 作者：WKJ on 2017/8/3.
 * 邮箱：
 * 编辑：WKJ
 */

public class BeanHotPlan {

    /**
     * description : 一周的针灸计划
     * idOffset : 13
     * title : 针灸
     * url : /demo/getKey/hpc_20170313_4582b0dd-bd49-4485-82fc-47f65129a130
     */

    private String description;
    private int idOffset;
    private String title;
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdOffset() {
        return idOffset;
    }

    public void setIdOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
