package com.healthyfish.healthyfish.eventbus;

import java.util.List;

/**
 * 描述：
 * 作者：LYQ on 2017/9/13.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class UploadPhyImgMsg {
    private List<String> imgPaths;
    private List<String> imgUrls;

    public List<String> getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(List<String> imgPaths) {
        this.imgPaths = imgPaths;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
