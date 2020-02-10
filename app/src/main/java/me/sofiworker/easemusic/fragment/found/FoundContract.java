package me.sofiworker.easemusic.fragment.found;

import java.util.List;

import me.sofiworker.easemusic.base.IBasePresenter;
import me.sofiworker.easemusic.base.IBaseView;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/29 12:37
 * @description
 */
public interface FoundContract {

    interface View extends IBaseView{
        void showBanner(List<String> bannerUrlList);
    }

    interface Presenter extends IBasePresenter<View>{

        void getBanner(int type);
    }
}
