package com.example.haozai.jen.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Created by haozai on 2018-11-09.
 */

@SuppressLint("NewApi")
public class ImageFileCache implements ImageCache{

    @Override
    public Bitmap getBitmap(String url) {
        return ImageFileCacheUtils.getInstance().getImage(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        ImageFileCacheUtils.getInstance().saveBitmap(bitmap, url);
    }

}
