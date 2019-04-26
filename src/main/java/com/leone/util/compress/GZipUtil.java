package com.leone.util.compress;


import com.leone.util.common.Constants;
import com.leone.util.common.IoUtil;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p> GZIP压缩工具类
 *
 * @author leone
 * @since 2019-04-26
 **/
public class GZipUtil {

    /**
     * 文件压缩
     *
     * @param source 待压缩文件路径
     * @param target
     */
    public static void compress(String source, String target) {
        compress(new File(source), target);
    }

    /**
     * 文件压缩
     *
     * @param source File 待压缩
     * @param target
     */
    public static void compress(File source, String target) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            File targetFile = new File(target);
            if (targetFile.isFile()) {
                System.err.println("target不能是文件，否则压缩失败");
                return;
            }
            target += (source.getName() + Constants.FILE_GZ);
            FileOutputStream fos = new FileOutputStream(target);
            compress(fis, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fis);
        }
    }

    /**
     * 文件解压缩
     *
     * @param source 待压缩文件路径
     * @param target
     */
    public static void uncompress(String source, String target) {
        uncompress(new File(source), target);
    }

    /**
     * 文件解压缩
     *
     * @param source File 待解压缩
     * @param target
     */
    public static void uncompress(File source, String target) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            target += source.getName().replace(Constants.FILE_GZ, "");
            FileOutputStream fos = new FileOutputStream(target);
            uncompress(fis, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fis);
        }
    }


    /**
     * 文件流压缩
     *
     * @param in
     * @param out
     */
    private static void compress(InputStream in, OutputStream out) {
        try (GZIPOutputStream gos = new GZIPOutputStream(out)) {
            int count;
            byte[] data = new byte[Constants.BUFFER_1024];
            while ((count = in.read(data, 0, data.length)) != -1) {
                gos.write(data, 0, count);
            }
            gos.finish();
            gos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件流解压缩
     *
     * @param in
     * @param out
     */
    private static void uncompress(InputStream in, OutputStream out) {
        try (GZIPInputStream gis = new GZIPInputStream(in)) {
            int count;
            byte[] data = new byte[Constants.BUFFER_1024];
            while ((count = gis.read(data, 0, Constants.BUFFER_1024)) != -1) {
                out.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
