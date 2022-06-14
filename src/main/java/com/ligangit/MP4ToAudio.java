package com.ligangit;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.File;
import java.util.UUID;

public class MP4ToAudio {

    public static void mp4ToAudio(String sourceFilePath) {
        System.out.println("提取音频文件");
        File file = new File(sourceFilePath);
        //抓取资源
        FFmpegFrameGrabber frameGrabber1 = new FFmpegFrameGrabber(sourceFilePath);
        Frame frame = null;
        FFmpegFrameRecorder recorder = null;
        String fileName = null;
        try {
            frameGrabber1.start();
            // 输出位置
            fileName = file.getAbsolutePath() + UUID.randomUUID() + ".mp3";
            System.out.println("--文件名-->>" + fileName);
            recorder = new FFmpegFrameRecorder(fileName, frameGrabber1.getAudioChannels());
            recorder.setFormat("mp3");
            recorder.setSampleRate(frameGrabber1.getSampleRate());
            recorder.setTimestamp(frameGrabber1.getTimestamp());
            recorder.setAudioQuality(0);

            recorder.start();
            int index = 0;
            while (true) {
                frame = frameGrabber1.grab();
                if (frame == null) {
                    System.out.println("视频处理完成");
                    break;
                }
                if (frame.samples != null) {
                    recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
//                System.out.println("帧值=" + index);
                index++;
            }
            recorder.stop();
            recorder.release();
            frameGrabber1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String sourceFilePath = "D:\\work\\files\\视频提取音频\\VID_20220614_161838.mp4";
//        long timeMillis = System.currentTimeMillis();
//        mp4ToAudio(sourceFilePath);
//        System.out.println(System.currentTimeMillis() - timeMillis);//2877

        String   sourceFilePath = "D:\\work\\files\\视频提取音频\\视频提取音频.mp4";
        long  timeMillis = System.currentTimeMillis();
        mp4ToAudio(sourceFilePath);
        System.out.println(System.currentTimeMillis() - timeMillis);//2

    }
}
