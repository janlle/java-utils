package com.leone.util.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 *
 * @author leone
 * @since 2019-04-26
 **/
@Slf4j
public class ImgCodeUtil {

    /**
     * 生成二维码
     *
     * @param qrCodePath
     * @param content
     * @param filename
     */
    public static void createQRCode(String qrCodePath, String content, String filename) {

        int width = 300;
        int height = 300;
        String format = "png";
        Map<EncodeHintType, Object> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hashMap.put(EncodeHintType.MARGIN, 1);
        try {
            File qrCodeFile = new File(qrCodePath);
            if (!qrCodeFile.exists()) {
                // 创建二维码生成目录
                qrCodeFile.mkdirs();
            }
            File file = new File(qrCodePath + filename + ".png");
            if (!file.exists()) {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hashMap);
                Path path = file.toPath();
                MatrixToImageWriter.writeToPath(bitMatrix, format, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(" createQRCode error ");
        }
    }

    /**
     * 生成二维码并响应到浏览器
     *
     * @param content
     * @param response
     */
    public static void createQRCode(String content, HttpServletResponse response) {
        int width = 300;
        int height = 300;
        String format = "png";
        Map<EncodeHintType, Object> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hashMap.put(EncodeHintType.MARGIN, 1);
        try {
            response.setHeader("Cache-control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("content-type", "image/png");
            response.setCharacterEncoding("utf-8");
            response.setDateHeader("Expires", 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hashMap);
            BufferedImage img = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(img, format, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("createQRCode error");
        }
    }

    /**
     * 生成验证码
     *
     * @param response
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static String generate(HttpServletResponse response, int width, int height) throws Exception {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "JPG", response.getOutputStream());
        response.getOutputStream().close();
        return sRand.toString();
    }

    /**
     * 生成随机背景条纹
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
