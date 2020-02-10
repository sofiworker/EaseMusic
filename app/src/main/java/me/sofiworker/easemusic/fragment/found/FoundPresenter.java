package me.sofiworker.easemusic.fragment.found;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.sofiworker.easemusic.base.BaseObserver;
import me.sofiworker.easemusic.base.BasePresenter;
import me.sofiworker.easemusic.bean.BannerBean;
import me.sofiworker.easemusic.bean.BannerItemBean;
import me.sofiworker.easemusic.util.RxUtil;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/29 12:36
 * @description
 */
public class FoundPresenter extends BasePresenter<FoundContract.View> implements FoundContract.Presenter {


    @Override
    public void getBanner(int type) {
        mApi.getBanner(type)
                .compose(RxUtil.transform(mProvider))
                .subscribe(new BaseObserver<BannerBean>() {
                    @Override
                    protected void onSuccess(BannerBean bannerBean) {
                        if (bannerBean.getCode() == 200) {
                            List<BannerItemBean> bannerItemBeanList = bannerBean.getBanners();
                            List<String> picList = new ArrayList<>();
                            for (BannerItemBean itemBean : bannerItemBeanList) {
                                picList.add(itemBean.getPic());
                            }
                            mView.showBanner(picList);
                        }
                    }
                });
    }
}
