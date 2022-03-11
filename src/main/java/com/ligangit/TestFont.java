package com.ligangit;

import cn.hutool.core.img.ImgUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestFont {

//    public static void main(String[] args) throws IOException, FontFormatException {
//        //通过当前类的类加载器获取到字体文件流
//        File fontFile=new File("/usr/local/test-font/simsun.ttc");
//        //通过字体流创建字体并重新创建一个字体大小为(25.0F)的字体对象
//        Font font = null;
//        File file = new File("/usr/local/test-font/color.png");
//        BufferedImage sourceImg = ImageIO.read(file);
//        font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
//        ImgUtil.pressText(//
//                file,
//                new File("/usr/local/test-font/bw_ab.png"),
//                "测试文字水印", Color.RED , //文字
//                font.deriveFont(Font.ITALIC,sourceImg.getWidth()/6) , //字体
//                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
//                0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
//                0.6f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
//        );
//        System.out.println("结束");
//    }


    public static void main(String[] args) throws IOException, FontFormatException {
        long start = System.currentTimeMillis();
        //通过当前类的类加载器获取到字体文件流
        //通过字体流创建字体并重新创建一个字体大小为(25.0F)的字体对象
        WatermarkFontSingleton instance = WatermarkFontSingleton.getInstance("D:\\work\\files\\simsun.ttc");
        Font font = instance.getFont();
        File file = new File("D:\\work\\files\\color.png");
        BufferedImage sourceImg = ImageIO.read(file);
        ImgUtil.pressText(//
                file,
                new File("D:\\work\\files\\bw_ab.png"),
                "测试文字水印", Color.RED , //文字
                font.deriveFont(Font.ITALIC,sourceImg.getWidth()/6) , //字体
                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                0.6f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
        );
        long end1 = System.currentTimeMillis();
        System.out.println("结束，耗时：" + (end1 - start));

        WatermarkFontSingleton instance222 = WatermarkFontSingleton.getInstance("D:\\work\\files\\simsun.ttc");
        Font font222 = instance.getFont();
        if (instance222 == instance) {
            System.out.println("11111");
        }
        if (font222 == font) {
            System.out.println("11111");
        }
        File file22 = new File("D:\\work\\files\\color.png");
        BufferedImage sourceImg22 = ImageIO.read(file);
        ImgUtil.pressText(//
                file22,
                new File("D:\\work\\files\\bw_abcc.png"),
                "测试文字水印", Color.RED , //文字
                font.deriveFont(Font.ITALIC,sourceImg22.getWidth()/6) , //字体
                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                0.6f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
        );
        System.out.println("结束222，耗时：" + ( System.currentTimeMillis() - end1));

    }

}
