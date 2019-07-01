package com.example.cycleview.cache.bitmap;



import android.graphics.Bitmap;

/**
 * 用户管理内存缓存的类
 * @author HSW
 *
 */
public class MemCacheUtils {
	//	public LinkedHashMap<String, Bitmap> mBitmaps;
	public android.support.v4.util.LruCache<String, Bitmap> mBitmaps;

	public MemCacheUtils(){
//		mBitmaps = new LinkedHashMap<String, Bitmap>();
		long maxMemory = Runtime.getRuntime().maxMemory();
		int maxVal = (int) (maxMemory / 8);
		mBitmaps = new android.support.v4.util.LruCache<String, Bitmap>(maxVal){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public Bitmap loadBitmap(String url) {
		return get(url);
	}

	public Bitmap get(String url){
		return mBitmaps.get(url);
	}

	public void put(String url, Bitmap bitmap){
		mBitmaps.put(url, bitmap);
	}
}
