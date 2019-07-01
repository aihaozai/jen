package com.example.cycleview.cache.utils;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;

public class HttpUtils {

	/**
	 * 发起网络请求
	 * @param url
	 * @param file
	 * @return
	 */
	public static String send(String url) {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			URL imageUrl = new URL(url);
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
//			conn.setInstanceFollowRedirects(true);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200){
				is = conn.getInputStream();
				String res = StreamUtils.InputStreamToString(is, Charset.forName("GBK"));
				return res;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if(os != null)
					os.close();
				if(is != null)
					is.close();
				if(conn != null)
					conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发起网络请求
	 * @param url
	 * @param file
	 * @return
	 */
	public static Bitmap loadBitmap(String url) {

		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200){
				is = conn.getInputStream();

				// 在下载时，改变图片的清晰度
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				bitmap = BitmapFactory.decodeStream(is, null, options);
				return bitmap;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if(os != null)
					os.close();
				if(is != null)
					is.close();
				if(conn != null)
					conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
