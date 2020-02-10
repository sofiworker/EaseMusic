package me.sofiworker.easemusic.fragment.single;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import me.sofiworker.easemusic.R;
import me.sofiworker.easemusic.core.PlayActivity;
import me.sofiworker.easemusic.adapter.SingleRvAdapter;
import me.sofiworker.easemusic.base.BaseFragment;
import me.sofiworker.easemusic.bean.LocalSongBean;
import me.sofiworker.easemusic.util.ToastUtil;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/12/1 19:34
 * @description 本地音乐中单曲碎片
 */
public class SingleFragment extends BaseFragment<SinglePresenter> implements SingleContract.View {

    private static final String TAG = "SingleFragment";


    @BindView(R.id.rv_single)
    RecyclerView mSingleRv;
    private SingleRvAdapter mAdapter;
    private List<LocalSongBean> mSongList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_single;
    }

    @Override
    protected void initEvent() {
        mPresenter = new SinglePresenter();
        mPresenter.attachView(this, mProvider);

        mPresenter.setContext(getContext());

        String[] per = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), per)) {
            mPresenter.getLocalMusicList();
        }else {
            showEmpty();
            ToastUtil.showShort("请授予应用读写储存权限！");
        }

        initRecycler();
    }

    private void initRecycler() {
        mSingleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SingleRvAdapter(R.layout.item_single_rv);
        mSingleRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                playMusic(position);
            }
        });
    }

    @Override
    public void getMusicList(List<LocalSongBean> songList) {
        if (songList == null || songList.isEmpty()) {
            showEmpty();
        }else {
            mSongList = songList;
            mAdapter.setNewData(mSongList);
        }
    }

    private void playMusic(int pos){
        LocalSongBean song = mSongList.get(pos);
        Intent intent = new Intent(getContext(), PlayActivity.class);
        intent.putExtra("SongUri", song.getUri());
        startActivity(intent);
    }
}
