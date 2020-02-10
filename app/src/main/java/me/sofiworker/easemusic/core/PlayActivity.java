package me.sofiworker.easemusic.core;

import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import me.sofiworker.easemusic.R;
import me.sofiworker.easemusic.base.BaseActivity;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/12/5 13:51
 * @description 音乐播放活动
 */
public class PlayActivity extends BaseActivity {

    @BindView(R.id.toolbar_play)
    Toolbar mToolbar;
    @BindView(R.id.iv_previous)
    ImageView mPreviousIv;
    @BindView(R.id.iv_pause)
    ImageView mPauseIv;
    @BindView(R.id.iv_next)
    ImageView mNextIv;

    private MediaBrowserManager mManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initEvent() {
        initToolbar();
        clickEvent();
    }

    private void initToolbar() {

    }

    private void clickEvent(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        mManager = new MediaBrowserManager(this);
        mManager.onStart();
        mManager.addOnMediaStatusListener(new MediaBrowserManager.OnMediaStatusChangeListener() {
            @Override
            public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {

            }

            @Override
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mManager.onStop();
    }

}
