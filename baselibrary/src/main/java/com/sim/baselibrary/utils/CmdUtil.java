package com.sim.baselibrary.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Sim --- cmd命令行工具
 */
public class CmdUtil {

    private PrintWriter stdin;    //cmd操作对象


    /**
     * 创建cmd公共方法
     */
    public CmdUtil cmd(RanListener rl) {
        String[] command = {"cmd"};
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err, null)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out, rl)).start();
            stdin = new PrintWriter(p.getOutputStream());
            /**以下可以输入自己想输入的cmd命令*/

        } catch (Exception e) {
            throw new RuntimeException("编译出现错误：" + e.getMessage());
        }
        return this;
    }

    /**
     * cd到指定的盘下的目录路径
     *
     * @param pan
     * @param path
     * @return
     */
    public CmdUtil pack(String pan, String path) {
        stdin.println(pan);//定位到D盘根目录
        stdin.println("cd " + path);//cd到路径
        return this;
    }

    public CmdUtil getADB() {
        stdin.println("adb devices");
        return this;
    }

    /**
     * 安装apk文件
     *
     * @param FilePath
     * @return
     */
    public CmdUtil install(String FilePath) {
        stdin.println("adb install " + FilePath);
        return this;
    }

    /**
     * 获取ip地址信息
     *
     * @return
     */
    public CmdUtil getIP() {
        stdin.println("ipconfig/all");
        return this;
    }

    /**
     * 反编译（注意目录下存在需要apktool操作工具）
     *
     * @param path apk文件的完整路径地址
     * @return
     */
    public CmdUtil deCompiling(String path) {
        stdin.println("apktool d -f " + path);
        return this;
    }

    /**
     * 回编译（注意目录下存在需要apktool操作工具）
     *
     * @param path    apk文件的完整路径地址
     * @param newPath 回编译的apk文件的完整路径地址
     * @return
     */
    public CmdUtil BackCompile(String path, String newPath) {
        stdin.println("apktool b -f " + path + " -o " + newPath);
        return this;
    }

    /**
     * 资源回收（注意记得回收）
     *
     * @return
     */
    public CmdUtil close() {
        stdin.close();
        return this;
    }

    /**
     * 回调监听
     */
    public class SyncPipe implements Runnable {
        private final OutputStream ostrm;
        private final InputStream istrm;
        private RanListener rl;

        public SyncPipe(InputStream istrm, OutputStream ostrm, RanListener rl) {
            this.istrm = istrm;
            this.ostrm = ostrm;
            this.rl = rl;
        }

        public void run() {
            try {
                final byte[] buffer = new byte[1024];
                for (int length = 0; (length = istrm.read(buffer)) != -1; ) {
                    ostrm.write(buffer, 0, length);
                }
            } catch (Exception e) {
                throw new RuntimeException("处理命令出现错误：" + e.getMessage());
            }
            System.out.print("结束");
            if (rl != null) {
                rl.end();
            }
        }
    }

    /**
     * 回调监听的接口
     */
    public interface RanListener {
        void end();
    }

    public static void main(String[] args) {
        new CmdUtil().cmd(new RanListener() {
            @Override
            public void end() {

            }
        }).getADB().getIP().pack("D","qytx_pack\\Games").close();
    }

}
