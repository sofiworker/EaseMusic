package me.sofiworker.easemusic.activity.localmusic;


import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.ImageView;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.sofiworker.easemusic.R;
import me.sofiworker.easemusic.base.BaseActivity;
import me.sofiworker.easemusic.base.BaseFragment;
import me.sofiworker.easemusic.fragment.single.SingleFragment;
import me.sofiworker.easemusic.util.IndicatorUtil;
import me.sofiworker.easemusic.util.ToastUtil;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/12/1 15:15
 * @description 本地音乐活动
 */
public class LocalMusicActivity extends BaseActivity<LocalMusicPresenter> implements LocalMusicContract.View {

    @BindView(R.id.mi_common)
    MagicIndicator mLocalMusicMi;
    @BindView(R.id.vp_common)
    ViewPager mCommonVp;
    @BindView(R.id.iv_toolbar_back)
    ImageView mBackIv;
    @BindView(R.id.iv_toolbar_more_info)
    ImageView mMoreInfoIv;

    @OnClick({R.id.iv_toolbar_more_info, R.id.iv_toolbar_back})
    void clickEvent(View v){
        switch (v.getId()) {
            case R.id.iv_toolbar_more_info:
                showRightMenu();
                break;
            case R.id.iv_toolbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void showRightMenu() {
        PopupMenu menu = new PopupMenu(this, mMoreInfoIv);
        menu.inflate(R.menu.menu_local_music);
        menu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_scan_local_music) {
                ToastUtil.showShort("扫描本地音乐");
            }
            return true;
        });
        menu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_music;
    }

    @Override
    protected void initEvent() {
        mPresenter = new LocalMusicPresenter();
        mPresenter.attachView(this, mProvider);
//        mPresenter.setContext(this);

        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SingleFragment());
        IndicatorUtil.combineFv(this, getSupportFragmentManager(), mLocalMusicMi,
                mCommonVp, R.array.local_music_mi_title, fragmentList);
    }

    @Override
    public void getMusicList(List<String> list) {
    }
}
