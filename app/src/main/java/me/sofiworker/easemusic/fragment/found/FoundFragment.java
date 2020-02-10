package me.sofiworker.easemusic.fragment.found;

import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;
import me.sofiworker.easemusic.R;
import me.sofiworker.easemusic.base.BaseFragment;
import me.sofiworker.easemusic.view.BannerImageLoader;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/29 12:34
 * @description 发现碎片
 */
public class FoundFragment extends BaseFragment<FoundPresenter> implements FoundContract.View{

    @BindView(R.id.banner)
    Banner mBanner;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_found;
    }

    @Override
    protected void initEvent() {
        mPresenter = new FoundPresenter();
        mPresenter.attachView(this, mProvider);

        mPresenter.getBanner(1);
    }

    @Override
    public void showBanner(List<String> bannerUrlList) {
        mBanner.setImageLoader(new BannerImageLoader()).setImages(bannerUrlList).start();
    }
}
