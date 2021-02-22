package com.sim.baselibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;

/**
 * @author Sim --- File工具类
 */
public class FileUtil {

    /**
     * 创建文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {

            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            LogUtil.e(FileUtil.class,"创建文件夹error:"+e );
        }
    }

    /**
     * 文件类型bean类
     */
    public enum FileType {
        storage, usb, directory, txt, zip, video, music, image, apk, other, pdf, ppt, word, excel
    }

    /**
     * 获取文件类型
     *
     * @param fileName
     * @return
     */
    public static FileType getFileType(String fileName) {
        if (fileName.endsWith(".mp3") || fileName.endsWith(".cda") || fileName.endsWith(".wav") || fileName.endsWith(".wma")
                || fileName.endsWith(".amr") || fileName.endsWith(".ape") || fileName.endsWith(".flac") || fileName.endsWith(".m4r")
                || fileName.endsWith(".mmf") || fileName.endsWith(".mp2") || fileName.endsWith(".ogg") || fileName.endsWith(".aac")
                || fileName.endsWith(".wv")) {
            return FileType.music;
        }

        if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".3gp") || fileName.endsWith(".mov")
                || fileName.endsWith(".rmvb") || fileName.endsWith(".mkv") || fileName.endsWith(".flv") || fileName.endsWith(".rm")
                || fileName.endsWith(".swf") || fileName.endsWith(".wmv")) {
            return FileType.video;
        }

        if (fileName.endsWith(".txt") || fileName.endsWith(".log") || fileName.endsWith(".xml")) {
            return FileType.txt;
        }

        if (fileName.endsWith(".zip") || fileName.endsWith(".rar")) {
            return FileType.zip;
        }

        if (fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".jpg") || fileName.endsWith(".bmp")) {
            return FileType.image;
        }

        if (fileName.endsWith(".apk")) {
            return FileType.apk;
        }
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return FileType.word;
        }
        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return FileType.excel;
        }
        if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return FileType.ppt;
        }
        if (fileName.endsWith(".pdf")) {
            return FileType.pdf;
        }
        return FileType.other;
    }

    /**
     * 判断文件夹是否存在,如果不存在则创建文件夹
     *
     * @param path
     */
    public static void isExist(String path) {
        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 获取文件的子文件个数
     *
     * @param file
     * @return
     */
    public static int getFileChildCount(File file) {
        int count = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isHidden()) continue;
                count++;
            }
        }
        return count;
    }

    /**
     * 文件按照名字排序
     */
    public static Comparator comparator = new Comparator<File>() {
        @Override
        public int compare(File file1, File file2) {
            if (file1.isDirectory() && file2.isFile()) {
                return -1;
            } else if (file1.isFile() && file2.isDirectory()) {
                return 1;
            } else {
                return file1.getName().compareTo(file2.getName());
            }
        }
    };

    /**
     * 文件大小转换
     *
     * @param size
     * @return
     */
    public static String sizeToChange(long size) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  //字符格式化，为保留小数做准备

        double G = size * 1.0 / 1024 / 1024 / 1024;
        if (G >= 1) {
            return df.format(G) + " GB";
        }

        double M = size * 1.0 / 1024 / 1024;
        if (M >= 1) {
            return df.format(M) + " MB";
        }

        double K = size * 1.0 / 1024;
        if (K >= 1) {
            return df.format(K) + " KB";
        }

        return size + " B";
    }

    /**
     * 删除文件夹以及文件夹下面的东西
     *
     * @param dir 要删除的文件夹目录
     * @return 是否删除成功
     */
    public static boolean deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists())
            return false;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    deleteDirWihtFile(file);
                }
            }
            dir.delete();// 删除目录本身
            return true;
        } else {
            dir.delete();
            return true;
        }
    }

    /**
     * 文件转化为Object
     *
     * @param fis
     * @return byte[]
     */
    public static Object file2Object(FileInputStream fis/*String fileName*/) {
        //FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            //fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * object转化为文件
     *
     * @param obj
     * @param fos
     */
    public static void object2File(Object obj, FileOutputStream fos/*String outputFile*/) {
        ObjectOutputStream oos = null;
        //FileOutputStream fos = null;
        try {
            //fos = new FileOutputStream(new File(outputFile));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    public static boolean copyFile(String fromFile, String toFile) {

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosto.getFD().sync();//同步，不同步经常发现拔出u盘后文件为0KB
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
