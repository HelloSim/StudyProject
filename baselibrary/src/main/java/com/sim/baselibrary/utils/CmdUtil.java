package com.sim.baselibrary.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @Author: Sim
 * @Time�� 2021/1/28 11:07
 * @Description�� cmd�����й���
 */
public class CmdUtil {

    private PrintWriter stdin;    //cmd��������


    /**
     * ����cmd��������
     */
    public CmdUtil cmd(RanListener rl) {
        String[] command = {"cmd"};
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err, null)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out, rl)).start();
            stdin = new PrintWriter(p.getOutputStream());
            /**���¿��������Լ��������cmd����*/

        } catch (Exception e) {
            throw new RuntimeException("������ִ���" + e.getMessage());
        }
        return this;
    }

    /**
     * cd��ָ�������µ�Ŀ¼·��
     *
     * @param pan
     * @param path
     * @return
     */
    public CmdUtil pack(String pan, String path) {
        stdin.println(pan);//��λ��D�̸�Ŀ¼
        stdin.println("cd " + path);//cd��·��
        return this;
    }

    public CmdUtil getADB() {
        stdin.println("adb devices");
        return this;
    }

    /**
     * ��װapk�ļ�
     *
     * @param FilePath
     * @return
     */
    public CmdUtil install(String FilePath) {
        stdin.println("adb install " + FilePath);
        return this;
    }

    /**
     * ��ȡip��ַ��Ϣ
     *
     * @return
     */
    public CmdUtil getIP() {
        stdin.println("ipconfig/all");
        return this;
    }

    /**
     * �����루ע��Ŀ¼�´�����Ҫapktool�������ߣ�
     *
     * @param path apk�ļ�������·����ַ
     * @return
     */
    public CmdUtil deCompiling(String path) {
        stdin.println("apktool d -f " + path);
        return this;
    }

    /**
     * �ر��루ע��Ŀ¼�´�����Ҫapktool�������ߣ�
     *
     * @param path    apk�ļ�������·����ַ
     * @param newPath �ر����apk�ļ�������·����ַ
     * @return
     */
    public CmdUtil BackCompile(String path, String newPath) {
        stdin.println("apktool b -f " + path + " -o " + newPath);
        return this;
    }

    /**
     * ��Դ���գ�ע��ǵû��գ�
     *
     * @return
     */
    public CmdUtil close() {
        stdin.close();
        return this;
    }

    /**
     * �ص�����
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
                throw new RuntimeException("����������ִ���" + e.getMessage());
            }
            System.out.print("����");
            if (rl != null) {
                rl.end();
            }
        }
    }

    /**
     * �ص������Ľӿ�
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
