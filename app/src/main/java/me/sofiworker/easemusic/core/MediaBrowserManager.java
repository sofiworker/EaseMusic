package me.sofiworker.easemusic.core;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2020/2/8 21:44
 * @description
 */
public class MediaBrowserManager {

    private static final String TAG = "MediaBrowserManager";


    private final Context mContext;

    private MediaBrowserCompat mMediaBrowserCompat;

    private MediaControllerCompat mMediaController;

    private final MediaBrowserConnectionCallback mMediaBrowserConnectionCallback =
            new MediaBrowserConnectionCallback();

    private final MediaControllerCallback mMediaControllerCallback =
            new MediaControllerCallback();

    private final MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback =
            new MediaBrowserSubscriptionCallback();


    public MediaBrowserManager(Context context) {
        this.mContext = context;
    }

    /**
     * 建立连接，创建控制器
     */
    public void onStart() {
        if (mMediaBrowserCompat == null) {
            // 创建MediaBrowserCompat
            mMediaBrowserCompat = new MediaBrowserCompat(
                    mContext,
                    // 创建ComponentName 连接 MusicService
                    new ComponentName(mContext, MusicPlayService.class),
                    // 创建连接回调
                    mMediaBrowserConnectionCallback,
                    //
                    null);
            // 连接service
            mMediaBrowserCompat.connect();
        }
        Log.d(TAG, "onStart: Creating MediaBrowser, and connecting");
    }

    /**
     * 跟随Activity的生命周期
     */
    public void onStop() {
        if (mMediaController != null) {
            mMediaController.unregisterCallback(mMediaControllerCallback);
            mMediaController = null;
        }
        if (mMediaBrowserCompat != null && mMediaBrowserCompat.isConnected()) {
            mMediaBrowserCompat.disconnect();
            mMediaBrowserCompat = null;
        }
        Log.d(TAG, "onStop: Releasing MediaController, Disconnecting from MediaBrowser");
    }

    /**
     * 连接结果回调
     * 构造 MediaBrowserCompat 时，您必须创建 ConnectionCallback 的实例。
     * 修改其 onConnected() 方法以从播放服务检索媒体会话令牌，并使用该令牌创建 MediaControllerCompat。
     */
    public class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        /**
         * 连接成功调用该方法，创建控制器
         */
        @Override
        public void onConnected() {
            try {
                mMediaController = new MediaControllerCompat(
                        mContext,
                        mMediaBrowserCompat.getSessionToken());
                //mediaController注册回调，callback就是媒体信息改变后，服务给客户端的回调
                mMediaController.registerCallback(mMediaControllerCallback);
//                mMediaControllerCallback.onMetadataChanged(mMediaController.getMetadata());
//                mMediaControllerCallback.onPlaybackStateChanged(mMediaController.getPlaybackState());
            } catch (RemoteException e) {
                Log.d(TAG, String.format("onConnected: Problem: %s", e.toString()));
                throw new RuntimeException(e);
            }

            //订阅服务端与服务端的onChildrenLoaded()方法对应
            mMediaBrowserCompat.subscribe(mMediaBrowserCompat.getRoot(), mMediaBrowserSubscriptionCallback);
        }
    }

    /**
     * 服务端允许连接后发送数据至此
     */
    public class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                                     @NonNull List<MediaBrowserCompat.MediaItem> children) {
            //订阅信息回调，parentID为标识，children为传回的媒体列表
            if (mMediaController == null) {
                return;
            }
            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mMediaController.addQueueItem(mediaItem.getDescription());
            }
            mMediaController.getTransportControls().prepare();
        }
    }

    /**
     * 服务端通过MediaControllerCallback回调到客户端
     */
    public class MediaControllerCallback extends MediaControllerCompat.Callback {

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            for (OnMediaStatusChangeListener callback : mMediaStatusChangeListenerList) {
                callback.onMetadataChanged(metadata);
            }
        }

        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackStateCompat state) {
            for (OnMediaStatusChangeListener callback : mMediaStatusChangeListenerList) {
                assert state != null;
                callback.onPlaybackStateChanged(state);
            }
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
            for (OnMediaStatusChangeListener callback : mMediaStatusChangeListenerList) {
                callback.onQueueChanged(queue);
            }
        }

        // 服务端被杀死时调用
        @Override
        public void onSessionDestroyed() {
            onPlaybackStateChanged(null);
        }
    }

    /**
     * 获取播放控制器 通过该方法控制播放
     */
    public MediaControllerCompat.TransportControls getTransportControls() {
        if (mMediaController == null) {
            Log.d(TAG, "getTransportControls: MediaController is null!");
            throw new IllegalStateException();
        }
        return mMediaController.getTransportControls();
    }

    /**
     * 音频变化回调 管理列表
     */
    private List<OnMediaStatusChangeListener> mMediaStatusChangeListenerList = new ArrayList<>();

    /**
     * 添加音频变化回调
     */
    public void addOnMediaStatusListener(OnMediaStatusChangeListener l) {
        mMediaStatusChangeListenerList.add(l);
    }

    /**
     * 移除音频变化回调
     */
    public void removeOnMediaStatusListener(OnMediaStatusChangeListener l) {
        mMediaStatusChangeListenerList.remove(l);
    }


    /**
     * 暴露给UI的音频变化回调
     */
    public interface OnMediaStatusChangeListener {

        /**
         * 播放状态修改
         */
        void onPlaybackStateChanged(@NonNull PlaybackStateCompat state);

        /**
         * 当前播放歌曲信息修改
         */
        void onMetadataChanged(MediaMetadataCompat metadata);

        /**
         * 播放队列修改
         */
        void onQueueChanged(List<MediaSessionCompat.QueueItem> queue);
    }
}
