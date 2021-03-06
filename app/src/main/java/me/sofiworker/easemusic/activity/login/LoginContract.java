package me.sofiworker.easemusic.activity.login;

import java.util.Map;

import me.sofiworker.easemusic.base.IBasePresenter;
import me.sofiworker.easemusic.base.IBaseView;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/27 20:28
 * @description login契约接口
 */
public interface LoginContract {

    interface View extends IBaseView {
        void isLoginSuccess(boolean isTrue);
    }

    interface Presenter extends IBasePresenter<View> {

        void postLoginInfo(Map<String, String> userInfo);
    }
}
