package com.leone.util.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * <p> 视频处理
 *
 * @author leone
 * @since 2019-04-26
 **/
public class VideoUtil {

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videoFile 源视频文件路径
     * @param photoFile 截取帧的图片存放路径
     * @throws Exception
     */
    public static void cutImage(String videoFile, String photoFile) {
        File targetFile = new File(photoFile);
        try (FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(videoFile)) {
            ff.start();
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame frame = null;
            while (i < length) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                frame = ff.grabFrame();
                if ((i > 5) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            if (frame != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bi = converter.getBufferedImage(frame);
                if (bi != null) {
                    int oWidth = bi.getWidth();
                    int oHeight = bi.getHeight();
                    // 对截取的帧进行等比例缩放
                    int width = 800;
                    int height = (int) (((double) width / oWidth) * oHeight);
                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    bufferedImage.getGraphics().drawImage(image, 0, 0, null);
                    ImageIO.write(bufferedImage, "jpg", targetFile);
                    ff.stop();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
