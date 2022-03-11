package com.ligangit;

import java.awt.*;
import java.io.File;
import java.io.IOException;
/**
 *  水印字体单例
 *  @author ligang
 *  @date 2022/3/11
 */
public class WatermarkFontSingleton {
    private static volatile WatermarkFontSingleton instance;

    private Font font;
    private WatermarkFontSingleton(){
    }
    private WatermarkFontSingleton(String fontFilePath) throws IOException, FontFormatException {
        File fontFile = new File(fontFilePath);
        font= Font.createFont(Font.TRUETYPE_FONT, fontFile);
    }

    public static WatermarkFontSingleton getInstance(String fontFilePath) throws IOException, FontFormatException {
        if(instance == null){
            synchronized (WatermarkFontSingleton.class) {
                if(instance == null){
                    instance = new WatermarkFontSingleton(fontFilePath);
                }
            }
        }
        return instance;
    }

    public Font getFont() {
        return font;
    }
}
