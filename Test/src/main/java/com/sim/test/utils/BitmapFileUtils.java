package com.sim.test.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: Sim
 * @Time: 2019/10/11 0:41
 * @Description : Bitmap与File类型转换的工具类
 */
public class BitmapFileUtils {

    /**
     * 保存Bitmap为文件;没有配置权限可能报空指针异常
     *
     * @param bmp
     * @param filename
     * @return
     */
    public static void saveBitmapToFile(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + filename + ".jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件为Bitmap
     *
     * @param filename
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapFromFile(String filename) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(Environment
                    .getExternalStorageDirectory().getPath() + filename + ".jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
