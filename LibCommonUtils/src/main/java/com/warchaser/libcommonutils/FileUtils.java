package com.warchaser.libcommonutils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private FileUtils(){

    }

    /**
     * 清空制定文件夹下的文件
     * 不考虑嵌套文件夹
     * */
    public static void clearDir(File file){
        if(file.isDirectory() && file.exists()){
            File[] files = file.listFiles();

            for(File bean : files){
                bean.delete();
            }
        }
    }

    public static boolean isFileExist(String path){
        return new File(path).exists();
    }

    public static void deleteOnExist(String path){
        if(TextUtils.isEmpty(path)){
            return;
        }

        final File file = new File(path);
        if(file != null && file.exists()){
            file.delete();
        }
    }

    /**
     * 图片转Base64
     * */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
            result = "data:image/png;base64," + result;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 获取安卓自带File文件夹目录路径
     * */
    public static String getExternalFilePath(Context context, String dirName){
        String path;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + File.separator + dirName + File.separator;
        } else {
            path = context.getFilesDir().getAbsolutePath() + File.separator + dirName + File.separator;
        }

        final File targetFilePath = new File(path);

        boolean mkdirsSuccess;

        if(!targetFilePath.exists()){
            mkdirsSuccess = targetFilePath.mkdirs();
        } else {
            mkdirsSuccess = true;
        }

        return mkdirsSuccess ? path : "";
    }

}
