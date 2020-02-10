package me.sofiworker.easemusic.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/12/3 18:42
 * @description 音乐后台播放服务（服务端）
 */
public class MusicPlayService extends MediaBrowserServiceCompat {

    private static final String TAG = "MusicPlayService";
    private static final String MEDIA_ROOT_ID = "media_root_id";
    private static final String EMPTY_MEDIA_ROOT_ID = "empty_root_id";

    private MediaSessionCompat mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat mPlaybackState;

    /**
     * 初始化MediaSessionCompat
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // 创建媒体会话
        mMediaSession = new MediaSessionCompat(this, TAG);

        // 创建UI回调
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // 使用ACTION_PLAY设置初始PlaybackState，以便媒体按钮可以启动播放器
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE).build();
        mMediaSession.setPlaybackState(mPlaybackState);

        // MySessionCallback()中含有处理UI回调的方法
         mMediaSession.setCallback(new MediaSessionCallback());

        // 设置媒体会话令牌
        setSessionToken(mMediaSession.getSessionToken());

        //创建播放器
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build();
        //设置播放属性
        mExoPlayer.setAudioAttributes(audioAttributes);
    }

    /**
     * 控制对音乐播放服务的访问
     * @return null为拒绝连接，必须返回非null的BrowserRoot，代表内容层次结构的根ID。
     */
    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        // 通过包名控制访问权限
//        if (true) {
//            // Returns a root ID that clients can use with onLoadChildren() to retrieve
//            // the content hierarchy.
//           // 返回一个根ID，客户端可以将其与onLoadChildren（）一起使用来检索内容层次结构。
//            return new BrowserRoot(MEDIA_ROOT_ID, null);
//        } else {
//            // Clients can connect, but this BrowserRoot is an empty hierachy
//            // so onLoadChildren returns nothing. This disables the ability to browse for content.
//            //客户端可以连接，但是此BrowserRoot是一个空的层次结构，
//            // 因此onLoadChildren不返回任何内容。这将禁用浏览内容的功能。
//            return new BrowserRoot(EMPTY_MEDIA_ROOT_ID, null);
//        }
        return new BrowserRoot(MEDIA_ROOT_ID, null);
    }

    /**
     * 与客户端通信与订阅回调相对应
     * @param s 媒体id
     */
    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        if (TextUtils.equals(EMPTY_MEDIA_ROOT_ID, s)) {
            result.sendResult(null);
            return;
        }
        // 外部音乐目录
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        if (MEDIA_ROOT_ID.equals(s)) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
            result.sendResult(mediaItems);
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
            result.detach();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSession.release();
    }

    /**
     * 客户端控制指令所到达处
     */
    public class MediaSessionCallback extends MediaSessionCompat.Callback {
        // 播放列表
        private final List<MediaSessionCompat.QueueItem> mPlaylist = new ArrayList<>();
        private int mQueueIndex = -1;
        // 准备播放的音频数据
        private MediaMetadataCompat mPreparedMedia;

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mQueueIndex == -1) ? 0 : mQueueIndex;
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            mPlaylist.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mPlaylist.isEmpty()) ? -1 : mQueueIndex;
        }

        @Override
        public void onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()) {
                // Nothing to play.
                return;
            }

            final String mediaId = mPlaylist.get(mQueueIndex).getDescription().getMediaId();
            // 根据音频 获取音频数据
//            mPreparedMedia = MusicLibrary.getMetadata(MusicService.this, mediaId);
            // 设置音频数据
            // 该方法将回调到 Client 的 MediaControllerCallback.onMetadataChanged
            mMediaSession.setMetadata(mPreparedMedia);
            // 激活mediaSession
            if (!mMediaSession.isActive()) {
                mMediaSession.setActive(true);
            }
        }

        @Override
        public void onPlay() {
            //
            if (!isReadyToPlay()) {
                // Nothing to play.
                return;
            }
            // 准备数据
            if (mPreparedMedia == null) {
                onPrepare();
            }
            // 播放
//            mMediaPlayerManager.playFromMedia(mPreparedMedia);
            Log.d(TAG, "onPlayFromMediaId: MediaSession active");
        }

        @Override
        public void onPause() {
//            mMediaPlayerManager.pause();
        }

        @Override
        public void onStop() {
//            mMediaPlayerManager.stop();
            mMediaSession.setActive(false);
        }

        @Override
        public void onSkipToNext() {
            mQueueIndex = (++mQueueIndex % mPlaylist.size());
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            mQueueIndex = mQueueIndex > 0 ? mQueueIndex - 1 : mPlaylist.size() - 1;
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSeekTo(long pos) {
//            mMediaPlayerManager.seekTo(pos);
        }

        /**
         * 判断列表数据状态
         */
        private boolean isReadyToPlay() {
            return (!mPlaylist.isEmpty());
        }
    }

}
