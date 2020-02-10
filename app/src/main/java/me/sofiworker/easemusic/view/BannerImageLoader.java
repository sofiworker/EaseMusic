package me.sofiworker.easemusic.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2020/2/8 15:32
 * @description banner图片加载器
 */
public class BannerImageLoader extends ImageLoader {
    private static final long serialVersionUID = -1678479275480190534L;


    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        RequestOptions requestOptions = new RequestOptions().centerInside();
        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .into(imageView);
    }
}
