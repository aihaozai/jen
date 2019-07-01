package com.example.cycleview.cache.utils;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;

public class ImageUtils {
	/**
	 * Decodes image and scales it to reduce memory consumption
	 *
	 * @param f
	 * @return
	 */
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, options);
			int inSampleSize = computeSampleSize(options, -1, 128 * 128);
			// decode with inSampleSize
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 根据实际需要，计算出合适的inSampleSize，以减少内存的开销
	 *
	 * @param options
	 * @param minSideLength 最小长度，不限制则为-1
	 * @param maxNumOfPixels 最大像素点，128 * 128
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
