package com.example.cycleview.cache.utils;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StreamUtils {
	public static String InputStreamToString(InputStream is, Charset charset)
			throws IOException {
		// 创建一个内存流
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			// 将数据从InputStream拷贝到内存流中
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, len);
			}

			// 通过内存流的toByteArray()方法得到一个byte数组
			byte[] newBuffer = baos.toByteArray();
			// 根据byte数组创建字符串

			// 根据不同的编码解析成不同格式的字符串
			String temp = new String(newBuffer, 0, newBuffer.length, charset);

			return temp;
		} finally {
			// 关闭流
			if (baos != null)
				baos.close();

			if (is != null)
				is.close();
		}
	}
}
