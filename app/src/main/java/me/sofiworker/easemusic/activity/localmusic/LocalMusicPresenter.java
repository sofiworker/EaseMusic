package me.sofiworker.easemusic.activity.localmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import me.sofiworker.easemusic.base.BasePresenter;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/12/1 15:59
 * @description 本地音乐presenter
 */
class LocalMusicPresenter extends BasePresenter<LocalMusicContract.View> implements LocalMusicContract.Presenter {

    private LocalMusicModel mModel;
    private Context mContext;

    LocalMusicPresenter(){
        mModel = new LocalMusicModel();
    }

//    @Override
//    public void setContext(Context context) {
//        this.mContext = context;
//    }
//
//    @Override
//    public void getLocalMusicList() {
//        List<String> list = new ArrayList<>();
//        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                int columnIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME);
//                int columnIndex1 = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
//                list.add(cursor.getString(columnIndex)+"-"+cursor.getString(columnIndex1));
//            }
//            cursor.close();
//        }
//        mView.getMusicList(list);
//    }

}
