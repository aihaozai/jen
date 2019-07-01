package com.example.haozai.jen.Utils;

/**
 * Created by haozai on 2018-11-09.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class ImageFileCacheUtils {
        private static final String CHCHEDIR = "ImageChace";//缓存目录
        private static final String WHOLESALE_CONV = ".cache";//缓存文件后缀名
        private static final int MB = 1024 * 1024;
        private static final int CACHE_SIZE = 80;//缓存最大容量（超过就会利用lru算法删除最近最少使用的缓存文件）
        private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 100;//缓存所需SD卡所剩的最小容量

        private static ImageFileCacheUtils instance = null;
        //单例模式
        public static ImageFileCacheUtils getInstance(){
            if (instance == null) {
                synchronized (ImageFileCacheUtils.class) {
                    if (instance == null) {
                        instance = new ImageFileCacheUtils();
                    }
                }
            }
            return instance;
        }

        public ImageFileCacheUtils(){
            removeCache(getDirectory());
        }

        /**
         * 从文件缓存中获取图片
         * @param url
         * @return
         */
        public Bitmap getImage(final String url){
            final String path = getDirectory() + "/" +convertUrlToFileName(url);
            File file = new File(path);
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if(bitmap == null){
                    file.delete();
                    return null;
                }else {
                    updateFileTime(path);//更新文件最新访问时间
                    return bitmap;
                }
            }else {
                return null;
            }
        }

        /**
         * 获得缓存目录
         * @return
         */
        private String getDirectory() {
            String dir = getSDPath() + "/" + CHCHEDIR;
            return dir;
        }

        private String getSDPath(){
            File sdDir = null;
            boolean adCardExit = Environment.getExternalStorageState()
                    .endsWith(Environment.MEDIA_MOUNTED);//判断SD卡是否挂载
            if (adCardExit) {
                sdDir = Environment.getExternalStorageDirectory();//获取根目录
            }
            if (sdDir != null) {
                return sdDir.toString();
            }else {
                return "";
            }
        }

        /**
         * 修改文件的最后修改时间
         * @param path
         */
        private void updateFileTime(String path) {
            File file = new File(path);
            long newModeifyTime = System.currentTimeMillis();
            file.setLastModified(newModeifyTime);
        }

        public void saveBitmap(Bitmap bitmap,String url){
            if (bitmap == null) {
                return ;
            }
            //判断SD卡上的空间
            if(FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()){
                return;
            }
            String fileName = convertUrlToFileName(url);
            String dir = getDirectory();
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(dir + "/" +fileName);
            try {
                file.createNewFile();
                OutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 计算sd卡上的剩余空间
         * @return
         */
        private int freeSpaceOnSd() {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            double sdFreeMB = ((double)statFs.getAvailableBlocks() * (double)statFs.getBlockSize()) / MB;
            Log.i("test", "剩余空间为："+sdFreeMB);
            return (int)sdFreeMB;
        }

        /**
         * 将url转成文件名
         * @param url
         * @return
         */
        private String convertUrlToFileName(String url) {
            String[] strs = url.split("/");
            return strs[strs.length - 1] + WHOLESALE_CONV;
        }

        /**
         * 计算存储目录下的文件大小
         * 当文件总大小大于规定的大小或者sd卡剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定时
         * ，那么删除40%最近没有被使用的文件
         * @param dirPath
         * @return
         */
        private boolean removeCache(String dirPath){
            File dirFile = new File(dirPath);
            File[] files = dirFile.listFiles();
            if (files == null || files.length <= 0) {
                return true;
            }
            //如果sd卡没有挂载
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return false;
            }
            int dirSize = 0;
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    dirSize += files[i].length();
                }
            }
            if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
                int removeFactor = (int)((0.4 * files.length) + 1);
                Arrays.sort(files, new FileLastModifySoft());
                for (int i = 0; i < removeFactor; i++) {
                    if (files[i].getName().contains(WHOLESALE_CONV)) {
                        files[i].delete();
                    }
                }
            }
            if (freeSpaceOnSd() <= CACHE_SIZE) {
                return false;
            }
            return true;
        }

        //比较器类
        private class FileLastModifySoft implements Comparator<File> {
            @Override
            public int compare(File arg0, File arg1) {
                if (arg0.lastModified() > arg1.lastModified()) {
                    return 1;
                }else if (arg0.lastModified() == arg1.lastModified()) {
                    return 0;
                }else {
                    return -1;
                }
            }
        }
    }


