package com.example.cycleview.cache.bitmap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.example.cycleview.cache.utils.ImageUtils;
import com.example.cycleview.cache.utils.MD5Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * 用户管理文件缓存的类
 * @author HSW
 *
 */
public class FileCacheUtils {
	private MemCacheUtils memCacheUtils;

	private static final String CACHE_DIR = "cache";

	private File mCacheFile;

	private Context mContext;

	public FileCacheUtils(Context context, MemCacheUtils memCacheUtils) {
		this.memCacheUtils = memCacheUtils;
		this.mContext = context;

		// 确定缓存位置（SD卡还是内部存储）
		File cachePos = null;
		String externalStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(externalStorageState)){
			cachePos = Environment.getExternalStorageDirectory();
		} else {
			cachePos = context.getCacheDir();
		}

		mCacheFile = new File(cachePos, CACHE_DIR);
		if (!mCacheFile.exists()){
			boolean mkdir = mCacheFile.mkdir();
			System.out.println(mkdir);
		}

	}

	/** 从本地加载图片文件 */
	public Bitmap loadBitmap(String url) {
		String filename = MD5Utils.md5Password(url);

		if (mCacheFile != null && mCacheFile.exists()){
			File cacheBitmapFile = new File(mCacheFile, filename);
			if (cacheBitmapFile.exists()){
				Bitmap decodeFile = ImageUtils.decodeFile(cacheBitmapFile);
				memCacheUtils.put(url, decodeFile);
				return decodeFile;
			}
		}

		return null;
	}

	public void put(String url, Bitmap bitmap){
		String filename = MD5Utils.md5Password(url);
		File cacheBitmapFile = new File(mCacheFile, filename);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(cacheBitmapFile);
			bitmap.compress(CompressFormat.WEBP, 100, fos);

			// 保存在内存中
			memCacheUtils.put(url, bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
