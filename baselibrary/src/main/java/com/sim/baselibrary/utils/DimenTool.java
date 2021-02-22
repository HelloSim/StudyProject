package com.sim.baselibrary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * @author Sim --- 生成不同分辨率下的dimens.xml文件
 */
public class DimenTool {

    public static void main(String[] args) {
        gen();
    }

    public static void gen() {
        File file = new File("./TravelTool/src/main/res/values/dimens.xml");//以此文件夹下的dimens.xml文件内容为初始值参照
        BufferedReader reader = null;
        StringBuilder sw240 = new StringBuilder();
        StringBuilder sw320 = new StringBuilder();
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder sw820 = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {// 一次读入一行，直到读入null为文件结束
                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw240.append(start).append(getNum(num * 0.667)).append(end).append("\r\n");
                    sw320.append(start).append(getNum(num * 0.089)).append(end).append("\r\n");
                    sw360.append(start).append(getNum(num * 1)).append(end).append("\r\n");
                    sw480.append(start).append(getNum(num * 1.333)).append(end).append("\r\n");
                    sw600.append(start).append(getNum(num * 1.667)).append(end).append("\r\n");
                    sw720.append(start).append(getNum(num * 2)).append(end).append("\r\n");
                    sw800.append(start).append(getNum(num * 2.222)).append(end).append("\r\n");
                    sw820.append(start).append(getNum(num * 2.278)).append(end).append("\r\n");
                } else {
                    sw240.append(tempString).append("");
                    sw320.append(tempString).append("");
                    sw360.append(tempString).append("");
                    sw480.append(tempString).append("");
                    sw600.append(tempString).append("");
                    sw720.append(tempString).append("");
                    sw800.append(tempString).append("");
                    sw820.append(tempString).append("");
                }
            }
            reader.close();
            //将新的内容，写入到指定的文件中去
            writeFile("./TravelTool/src/main/res/values-sw240dp/dimens.xml", sw240.toString());
            writeFile("./TravelTool/src/main/res/values-sw320dp/dimens.xml", sw320.toString());
            writeFile("./TravelTool/src/main/res/values-sw360dp/dimens.xml", sw360.toString());
            writeFile("./TravelTool/src/main/res/values-sw480dp/dimens.xml", sw480.toString());
            writeFile("./TravelTool/src/main/res/values-sw600dp/dimens.xml", sw600.toString());
            writeFile("./TravelTool/src/main/res/values-sw720dp/dimens.xml", sw720.toString());
            writeFile("./TravelTool/src/main/res/values-sw800dp/dimens.xml", sw800.toString());
            writeFile("./TravelTool/src/main/res/values-sw820dp/dimens.xml", sw820.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 保留指定小数
     *
     * @param num
     * @return
     */
    private static double getNum(double num) {
        DecimalFormat df = new DecimalFormat("#####0.0");
        return Double.parseDouble(df.format(num));
    }

    /**
     * 写入方法
     *
     * @param file
     * @param text
     */
    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

}
