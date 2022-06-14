package com.ligangit;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * 生成水印
 *
 */
@Slf4j
public class ImageMarkUtil {

    /**
     * 图片添加水印
     *
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     * @param fontSize         文字大小
     * @param waterMarkContent 水印的文字
     * @param srcImgPath       需要添加水印的图片的路径
     */
    public static boolean waterPress(String srcImgPath, String outImgPath, Color markContentColor, int fontSize, String waterMarkContent, Font font) {
        // 读取原图片信息
        File srcImgFile = new File(srcImgPath);
        File outImgFile = new File(outImgPath);
        return waterPress(srcImgFile, outImgFile, markContentColor, fontSize, waterMarkContent, font);
    }

    /**
     * 图片添加水印
     *
     * @param srcImgFile       需要添加水印的图片
     * @param outImgFile       添加水印后输入图片
     * @param markContentColor 水印文字的颜色
     * @param fontSize         文字大小
     * @param waterMarkContent 水印的文字
     * @param font             字体
     * @return
     */
    public static boolean waterPress(File srcImgFile, File outImgFile, Color markContentColor, int fontSize, String waterMarkContent, Font font) {
        try {
            Image srcImg = ImageIO.read(srcImgFile);
            // 原图宽度
            int srcImgWidth = srcImg.getWidth(null);
            // 原图高度
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // 根据图片的背景设置水印颜色
            g.setColor(markContentColor);
            // 设置水印文字字体
            g.setFont(font);
            // 设置水印旋转
//            g.rotate(Math.toRadians(30), (double) bufImg.getWidth() / 2, (double) bufImg.getHeight() / 2);
            // 设置水印旋转，圆形为坐标轴原点
            g.rotate(Math.toRadians(30), 0, 0);

            // 透明度
            float alpha = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 获取其中最长的文字水印的大小
            int maxLen = getWatermarkLength(waterMarkContent, g);
            int maxHigh = fontSize + 10;
            int move = Math.min(srcImgWidth, srcImgHeight) / 100 * 10;
            // 最小移动间隔为120
            move = Math.max(move + 100, 120);
            // 文字长度相对于图片宽度应该有多少行
            int line = srcImgWidth * 2 / maxLen;
            int co = srcImgHeight * 2 / maxHigh;
            boolean smallWidth = false;
            boolean smallHight = false;
            // 处理图片过宽、过窄情况
            if (line < 3) {
                line = 3;
                smallWidth = true;
            }
            if (co < 3) {
                co = 3;
                smallHight = true;
            }
            int yz = 0;
            // 填充Y轴方向
            for (int a = 0; a < co; a++) {
                int tempX = 0;
                // 图片加30度旋转后计算的图片Y轴长度，便于正向旋转30度后，图片右上角可以有水印
                int tempY = (int) (maxHigh + yz - (srcImgWidth * 1.732 / 4));
                // 填充X轴
                for (int z = 0; z < line; z++) {
                    int resultX = tempX;
                    // 图片过窄，需要设置偏移，保证图片的正面可以展示文案
                    if (smallWidth || smallHight) {
                        // 每列需要设置偏移
                        resultX = (int) ((srcImgWidth - maxLen) / 2 + yz * 1.732 / 4);
                    }
                    g.drawString(waterMarkContent, resultX, tempY);
                    tempX = tempX + maxLen + move;
                    // 图片过矮时，适当缩小水印间距
                    if (smallHight) {
                        tempX = tempX - 20;
                    }
                }
                yz = yz + maxHigh + move;
                // 图片过小时，适当缩小水印间距
                if (smallHight) {
                    yz = yz - 20;
                }
            }
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgFile);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return true;
        } catch (Exception e) {
            log.info("", e);
        }
        return false;
    }

    public static boolean waterPress(String srcImgPath, String outImgPath, Color markContentColor, String waterMarkContent,String fontFilePath) {
        // 读取原图片信息
        File srcImgFile = new File(srcImgPath);
        File outImgFile = new File(outImgPath);
        return waterPress(srcImgFile, outImgFile, markContentColor, waterMarkContent, fontFilePath);
    }
    /**
     * 图片添加水印
     * @param srcImgFile 需要添加水印的图片
     * @param outImgFile 添加水印后输入图片
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     * @param fontFilePath  字体文件地址
     * @return
     */
    public static boolean waterPress(File srcImgFile, File outImgFile, Color markContentColor, String waterMarkContent, String fontFilePath) {

        try {
            Image srcImg = ImageIO.read(srcImgFile);
            // 原图宽度
            int srcImgWidth = srcImg.getWidth(null);
            // 原图高度
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // 根据图片的背景设置水印颜色
            g.setColor(markContentColor);
            Font font = WatermarkFontSingleton.getInstance(fontFilePath).getFont();

            // 字体大小比例为120/5，其中使用98，是将98像素做为一个梯度，10是一个基础字体大小
            int fontSize = Math.min(srcImgWidth, srcImgHeight) / 98 * 4 + 10;
            font = font.deriveFont(Font.PLAIN, fontSize);
            // 设置水印文字字体
            g.setFont(font);
            // 设置水印旋转
//            g.rotate(Math.toRadians(30), (double) bufImg.getWidth() / 2, (double) bufImg.getHeight() / 2);
            // 设置水印旋转，圆形为坐标轴原点
            g.rotate(Math.toRadians(30), 0, 0);
            // 透明度
            float alpha = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 获取其中最长的文字水印的大小
            int maxLen = getWatermarkLength(waterMarkContent, g);
            int maxHigh = fontSize + 10;
            int move = Math.min(srcImgWidth, srcImgHeight) / 100 * 10;
            move = Math.max(move + 100, 120);
            // 文字长度相对于图片宽度应该有多少行
            int line = srcImgWidth * 2 / maxLen;
            int co = srcImgHeight * 2 / maxHigh;
            boolean smallWidth = false;
            boolean smallHight = false;
            // 处理图片过宽、过窄情况
            if (line < 3) {
                line = 3;
                smallWidth = true;
            }
            if (co < 3) {
                co = 3;
                smallHight = true;
            }
            int yz = 0;
            // 填充Y轴方向
            for (int a = 0; a < co; a++) {
                int tempX = 0;
                // 图片加30度旋转后计算的图片Y轴长度，便于正向旋转30度后，图片右上角可以有水印
                int tempY = (int) (maxHigh + yz - (srcImgWidth * 1.732 / 4));
                // 填充X轴
                for (int z = 0; z < line; z++) {
                    int resultX = tempX;
                    // 图片过窄，需要设置偏移，保证图片的正面可以展示文案
                    if (smallWidth || smallHight) {
                        // 每列需要设置偏移
                        resultX = (int) ((srcImgWidth - maxLen) / 2 + yz * 1.732 / 4);
                    }
                    g.drawString(waterMarkContent, resultX, tempY);
                    tempX = tempX + maxLen + move;
                    // 图片过矮时，适当缩小水印间距
                    if (smallHight) {
                        tempX = tempX - 20;
                    }
                }
                yz = yz + maxHigh + move;
                // 图片过小时，适当缩小水印间距
                if (smallHight) {
                    yz = yz - 20;
                }
            }
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgFile);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return true;
        } catch (Exception e) {
            log.info("", e);
        }
        return false;
    }



    /**
     * 获取水印文字总长度
     *
     * @paramwaterMarkContent水印的文字
     * @paramg
     * @return水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
    public static void main(String[] args) throws IOException, FontFormatException {
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字
        String content = "仅供睿远基金资料审核使用";
//        String inPath = "D:\\work\\files\\iphone.jpg";
//        String outPath = "D:\\work\\files\\baiduabdddd.jpg";
//        int move = 120;
//        int fontSize = 20;
        //srcImgWidth:271
        //srcImgHeight:543
        //  XMOVE:120
//        String inPath = "D:\\work\\files\\idcard.jpg";
//        String outPath = "D:\\work\\files\\baiduabdddd.jpg";
//        int move = 200;
//        int fontSize = 35;
        //srcImgWidth:1000
        //srcImgHeight:750
//        String inPath = "D:\\work\\files\\blue.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        int move = 140;
//        int fontSize = 24;
        //srcImgWidth:803
        //srcImgHeight:446
        //  fontsize:22
        //  XMOVE:120
//        String inPath = "D:\\work\\files\\longimage.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        int move = 120;
//        int fontSize = 20;
        //srcImgWidth:230
        //srcImgHeight:886
        //  fontsize:20
        //  XMOVE:120
//        String inPath = "D:\\work\\files\\idcard.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        int move = 120;
//        int fontSize = 20;
        //srcImgWidth:376
        //srcImgHeight:238
        //  fontsize:22
        //  XMOVE:120
//        String inPath = "D:\\work\\files\\abc12.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        int move = 130;
//        int fontSize = 22;
        //srcImgWidth:407
        //srcImgHeight:358
        //  fontsize:22
        //  XMOVE:120
//        String inPath = "D:\\work\\files\\abc13.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
        int move = 180;
        int fontSize = 34;
        //srcImgWidth:1882
        //srcImgHeight:829
        //  fontsize:42
        //  XMOVE:250
//        String inPath = "D:\\work\\files\\abc14.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        int move = 180;
//        int fontSize = 30;
        //srcImgWidth:1279
        //srcImgHeight:511
        //  fontsize:21
        //  XMOVE:150
//        String inPath = "D:\\work\\files\\abc15.jpg";
//        String outPath = "D:\\work\\files\\baiduabdddd.jpg";
        //srcImgWidth:960
        //srcImgHeight:1280
        //fontSize:46
        //move:190
        String inPath = "D:\\work\\files\\abc19.jpg";
        String outPath = "D:\\work\\files\\baiduabdddd.jpg";
//        String inPath = "D:\\work\\files\\abc17.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        String inPath = "D:\\work\\files\\abc18.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
        //通过当前类的类加载器获取到字体文件流
        //通过字体流创建字体并重新创建一个字体大小为(22.0F)的字体对象
//        WatermarkFontSingleton instance = WatermarkFontSingleton.getInstance("D:\\work\\files\\simsun.ttc");
//
//        Font font = instance.getFont();
//        font = font.deriveFont(Font.PLAIN, fontSize);
//        ImageMarkUtil.waterPress(inPath,
//                outPath, Color.red, fontSize, content, font, move);
        String fontFilePath = "D:\\work\\files\\simsun.ttc";
        ImageMarkUtil.waterPress(inPath,
                outPath, Color.blue, content,fontFilePath );
        //fontSize11111------34
        //fontSize22222------34
        //move11111-------:80
        //move22222-------:180
        //srcImgWidth:1882
        //srcImgHeight:829
        //fontSize:34
        //move:180
        //maxLen:408
    }

}