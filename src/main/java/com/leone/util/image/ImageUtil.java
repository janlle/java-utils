package com.leone.util.image;

import com.leone.util.common.Constants;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * <p> opencv 图像处理工具类
 *
 * @author leone
 * @since 2019-04-26
 **/
public class ImageUtil {
    /**
     * 读取图片，获取BufferedImage对象
     *
     * @param fileName
     * @return
     */
    public static BufferedImage getImage(String fileName) {
        File picture = new File(fileName);
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(picture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceImg;
    }

    /**
     * 读取图片，获取ImageReader对象
     *
     * @param fileName
     * @return
     */
    public static ImageReader getImageReader(String fileName) {
        if (fileName != null) {
            String suffix = "";
            for (String str : ImageIO.getReaderFormatNames()) {
                if (fileName.lastIndexOf(Constants.POINT_STR + str) > 0) {
                    suffix = str;
                }
            }

            if (!"".equals(suffix)) {
                try {
                    // 将FileInputStream 转换为ImageInputStream
                    ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(fileName));
                    // 根据图片类型获取该种类型的ImageReader
                    Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(suffix);
                    ImageReader reader = readers.next();
                    reader.setInput(iis, true);
                    return reader;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }

    /**
     * 图片截取 Graphic通过画布绘画
     *
     * @param image 源图片
     * @param rect  待截取区域的坐标位置
     */
    public static BufferedImage cutImage(File image, Rectangle rect) {
        if (image.exists() && rect != null) {
            // 用ImageIO读取字节流
            BufferedImage bufferedImage = getImage(image.getAbsolutePath());
            int width = rect.width;
            int height = rect.height;
            int x = rect.x;
            int y = rect.y;
            BufferedImage dest = new BufferedImage(width, height, Transparency.TRANSLUCENT);
            Graphics g = dest.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, x, y, x + width, height + y, null);
            return dest;
        }
        return null;
    }

    /**
     * 图片截取 ImageReader截取 速度比Graphic通过画布绘画快很多
     *
     * @param image 源图片
     * @param rect  待截取区域的坐标位置
     * @return
     */
    public static BufferedImage cutImage2(File image, Rectangle rect) {
        if (image.exists() && rect != null) {
            BufferedImage bi = null;
            try {
                ImageReader reader = getImageReader(image.getAbsolutePath());
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                bi = reader.read(0, param);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bi;
        }
        return null;
    }


    /**
     * BufferedImage生成目标图片
     *
     * @param destImage
     * @param bufferedImage
     */
    public static void writeImage(File destImage, BufferedImage bufferedImage) {
        try {
            ImageIO.write(bufferedImage, Constants.IMAGE_PNG, destImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final float DEFAULT_QUALITY = 0.2125f;

    /**
     * 添加图片水印操作(物理存盘,使用默认格式)
     *
     * @param imgPath         待处理图片
     * @param markPath        水印图片
     * @param x               水印位于图片左上角的 x 坐标值
     * @param y               水印位于图片左上角的 y 坐标值
     * @param alpha           水印透明度 0.1f ~ 1.0f
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印异常");
        }
    }


    /**
     * 添加图片水印操作(物理存盘,自定义格式)
     *
     * @param imgPath         待处理图片
     * @param markPath        水印图片
     * @param x               水印位于图片左上角的 x 坐标值
     * @param y               水印位于图片左上角的 y 坐标值
     * @param alpha           水印透明度 0.1f ~ 1.0f
     * @param format          添加水印后存储的格式
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印异常");
        }
    }


    /**
     * 添加图片水印操作,返回BufferedImage对象
     *
     * @param imgPath  待处理图片
     * @param markPath 水印图片
     * @param x        水印位于图片左上角的 x 坐标值
     * @param y        水印位于图片左上角的 y 坐标值
     * @param alpha    水印透明度 0.1f ~ 1.0f
     * @return 处理后的图片对象
     * @throws Exception
     */
    public static BufferedImage addWaterMark(String imgPath, String markPath, int x, int y, float alpha) throws Exception {
        BufferedImage targetImage = null;
        try {
            // 加载待处理图片文件
            Image img = ImageIO.read(new File(imgPath));

            //创建目标图象文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);

            // 加载水印图片文件
            Image markImg = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(markImg, x, y, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印操作异常");
        }
        return targetImage;

    }

    /**
     * 添加文字水印操作(物理存盘,使用默认格式)
     *
     * @param imgPath         待处理图片
     * @param text            水印文字
     * @param font            水印字体信息    不写默认值为宋体
     * @param color           水印字体颜色
     * @param x               水印位于图片左上角的 x 坐标值
     * @param y               水印位于图片左上角的 y 坐标值
     * @param alpha           水印透明度 0.1f ~ 1.0f
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = addTextMark(imgPath, text, font, color, x, y, alpha);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片添加文字水印异常");
        }
    }

    /**
     * 添加文字水印操作(物理存盘,自定义格式)
     *
     * @param imgPath         待处理图片
     * @param text            水印文字
     * @param font            水印字体信息    不写默认值为宋体
     * @param color           水印字体颜色
     * @param x               水印位于图片左上角的 x 坐标值
     * @param y               水印位于图片左上角的 y 坐标值
     * @param alpha           水印透明度 0.1f ~ 1.0f
     * @param format          添加水印后存储的格式
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = addTextMark(imgPath, text, font, color, x, y, alpha);
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片添加文字水印异常");
        }
    }

    /**
     * 添加文字水印操作,返回BufferedImage对象
     *
     * @param imgPath 待处理图片
     * @param text    水印文字
     * @param font    水印字体信息    不写默认值为宋体
     * @param color   水印字体颜色
     * @param x       水印位于图片左上角的 x 坐标值
     * @param y       水印位于图片左上角的 y 坐标值
     * @param alpha   水印透明度 0.1f ~ 1.0f
     * @return 处理后的图片对象
     * @throws Exception
     */

    public static BufferedImage addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha) throws Exception {
        BufferedImage targetImage = null;
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;
            Image img = ImageIO.read(new File(imgPath));
            //创建目标图像文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(text, x, y);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加文字水印操作异常");
        }
        return targetImage;
    }


    /**
     * 压缩图片操作(文件物理存盘,使用默认格式)
     *
     * @param imgPath         待处理图片
     * @param quality         图片质量(0-1之間的float值)
     * @param width           输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height          输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize        是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片压缩异常");
        }

    }


    /**
     * 压缩图片操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath         待处理图片
     * @param quality         图片质量(0-1之間的float值)
     * @param width           输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height          输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize        是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @param format          压缩后存储的格式
     * @param destinationPath 文件存放路径
     * @throws Exception
     */
    public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片压缩异常");
        }
    }


    /**
     * 压缩图片操作,返回BufferedImage对象
     *
     * @param imgPath  待处理图片
     * @param quality  图片质量(0-1之間的float值)
     * @param width    输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height   输出图片的高度    输入负数参数表示用原来图片高
     * @param autoSize 是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @return 处理后的图片对象
     * @throws Exception
     */
    public static BufferedImage compressImage(String imgPath, float quality, int width, int height, boolean autoSize) throws Exception {
        BufferedImage targetImage = null;
        if (quality < 0F || quality > 1F) {
            quality = DEFAULT_QUALITY;
        }
        try {
            Image img = ImageIO.read(new File(imgPath));
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newwidth = (width > 0) ? width : img.getWidth(null);
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newheight = (height > 0) ? height : img.getHeight(null);
            //如果是自适应大小则进行比例缩放
            if (autoSize) {
                // 为等比缩放计算输出的图片宽度及高度
                double Widthrate = ((double) img.getWidth(null)) / (double) width + 0.1;
                double heightrate = ((double) img.getHeight(null)) / (double) height + 0.1;
                double rate = Widthrate > heightrate ? Widthrate : heightrate;
                newwidth = (int) (((double) img.getWidth(null)) / rate);
                newheight = (int) (((double) img.getHeight(null)) / rate);
            }
            //创建目标图像文件
            targetImage = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, newwidth, newheight, null);
            //如果添加水印或者文字则继续下面操作,不添加的话直接返回目标文件----------------------
            g.dispose();

        } catch (Exception e) {
            throw new RuntimeException("图片压缩操作异常");
        }
        return targetImage;
    }


    /**
     * 图片黑白化操作(文件物理存盘,使用默认格式)
     *
     * @param bufferedImage 处理的图片对象
     * @param destinationPath      目标文件地址
     * @throws Exception
     */
    /**
     * @param imgPath
     * @param destinationPath
     * @throws Exception
     */
    public static void imageGray(String imgPath, String destinationPath) throws Exception {
        imageGray(imgPath, imageFormat(imgPath), destinationPath);
    }


    /**
     * 图片黑白化操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath         处理的图片对象
     * @param format          图片格式
     * @param destinationPath 目标文件地址
     * @throws Exception
     */
    public static void imageGray(String imgPath, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            bufferedImage = op.filter(bufferedImage, null);
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片灰白化异常");
        }
    }


    /**
     * 图片透明化操作(文件物理存盘,使用默认格式)
     *
     * @param imgPath         图片路径
     * @param destinationPath 图片存放路径
     * @throws Exception
     */
    public static void imageLucency(String imgPath, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = imageLucency(imgPath);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片透明化异常");
        }
    }


    /**
     * 图片透明化操作(文件物理存盘,可自定义格式)
     *
     * @param imgPath         图片路径
     * @param format          图片格式
     * @param destinationPath 图片存放路径
     * @throws Exception
     */
    public static void imageLucency(String imgPath, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = imageLucency(imgPath);
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (Exception e) {
            throw new RuntimeException("图片透明化异常");
        }
    }

    /**
     * 图片透明化操作返回BufferedImage对象
     *
     * @param imgPath 图片路径
     * @return 透明化后的图片对象
     * @throws Exception
     */
    public static BufferedImage imageLucency(String imgPath) throws Exception {
        BufferedImage targetImage = null;
        try {
            //读取图片
            BufferedImage img = ImageIO.read(new FileInputStream(imgPath));
            //透明度
            int alpha = 0;
            //执行透明化
            executeRGB(img, alpha);
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("图片透明化执行异常");
        }
        return targetImage;
    }

    /**
     * 执行透明化的核心算法
     *
     * @param img   图片对象
     * @param alpha 透明度
     * @throws Exception
     */
    public static void executeRGB(BufferedImage img, int alpha) throws Exception {
        int rgb = 0;//RGB值
        //x表示BufferedImage的x坐标，y表示BufferedImage的y坐标
        for (int x = img.getMinX(); x < img.getWidth(); x++) {
            for (int y = img.getMinY(); y < img.getHeight(); y++) {
                //获取点位的RGB值进行比较重新设定
                rgb = img.getRGB(x, y);
                int R = (rgb & 0xff0000) >> 16;
                int G = (rgb & 0xff00) >> 8;
                int B = (rgb & 0xff);
                if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    img.setRGB(x, y, rgb);
                }
            }
        }
    }


    /**
     * 图片格式转化操作(文件物理存盘)
     *
     * @param imgPath         原始图片存放地址
     * @param format          待转换的格式 jpeg,gif,png,bmp等
     * @param destinationPath 目标文件地址
     * @throws Exception
     */
    public static void formatConvert(String imgPath, String format, String destinationPath) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (IOException e) {
            throw new RuntimeException("文件格式转换出错");
        }
    }


    /**
     * 图片格式转化操作返回BufferedImage对象
     *
     * @param bufferedImage   BufferedImage图片转换对象
     * @param format          待转换的格式 jpeg,gif,png,bmp等
     * @param destinationPath 目标文件地址
     * @throws Exception
     */
    public static void formatConvert(BufferedImage bufferedImage, String format, String destinationPath) throws Exception {
        try {
            ImageIO.write(bufferedImage, format, new File(destinationPath));
        } catch (IOException e) {
            throw new RuntimeException("文件格式转换出错");
        }
    }


    /**
     * 获取图片文件的真实格式信息
     *
     * @param imgPath 图片原文件存放地址
     * @return 图片格式
     * @throws Exception
     */
    public static String imageFormat(String imgPath) throws Exception {
        String[] filess = imgPath.split("\\\\");
        String[] formats = filess[filess.length - 1].split("\\.");
        return formats[formats.length - 1];
    }
}

