package me.sofiworker.easemusic.bean;

import java.util.List;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2020/2/8 15:43
 * @description
 */
public class BannerBean {

    private List<BannerItemBean> banners;
    private int code;

    public List<BannerItemBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerItemBean> banners) {
        this.banners = banners;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
