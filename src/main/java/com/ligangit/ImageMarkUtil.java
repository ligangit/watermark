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
     * 水印之间的横向间隔
     */
    private static final int XMOVE = 120;

    /**
     * 水印之间的纵向间隔
     */
    private static final int YMOVE = 120;

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
     * @param srcImgFile 需要添加水印的图片
     * @param outImgFile 添加水印后输入图片
     * @param markContentColor 水印文字的颜色
     * @param fontSize 文字大小
     * @param waterMarkContent 水印的文字
     * @param font  字体
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

            // 获取其中最长的文字水印的大小
            int maxLen = getWatermarkLength(waterMarkContent, g);
            int maxHigh = fontSize + 10;
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
                    tempX = tempX + maxLen + XMOVE;
                    // 图片过矮时，适当缩小水印间距
                    if (smallHight) {
                        tempX = tempX - 20;
                    }
                }
                yz = yz + maxHigh + YMOVE;
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
        String content = "测试文字水印";
//        String inPath = "D:\\work\\files\\iphone.jpg";
//        String outPath = "D:\\work\\files\\baiduabdddd.jpg";
        String inPath = "D:\\work\\files\\blue.png";
        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        String inPath = "D:\\work\\files\\longimage.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
//        String inPath = "D:\\work\\files\\idcard.png";
//        String outPath = "D:\\work\\files\\baiduabdddd.png";
        //通过当前类的类加载器获取到字体文件流
        //通过字体流创建字体并重新创建一个字体大小为(22.0F)的字体对象
        WatermarkFontSingleton instance = WatermarkFontSingleton.getInstance("D:\\work\\files\\simsun.ttc");
        Font font = instance.getFont();
        font = font.deriveFont(Font.PLAIN, 22f);
        ImageMarkUtil.waterPress(inPath,
                outPath, Color.red, 24, content,font);
    }

}