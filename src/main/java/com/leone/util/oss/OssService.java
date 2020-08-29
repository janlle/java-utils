package com.leone.util.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *
 * @author leone
 **/
@Service
public class OssService {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(OssService.class);

    @Value("${oss.access-id}")
    private String accessId;

    @Value("${oss.access-key}")
    private String accessKey;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.upload-endpoint}")
    private String uploadEndpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.callback-url}")
    private String callbackUrl;

    private OSS ossClient;

    private OSS outerOssClient;

    @Value("${oss.qy-bucket-name}")
    private String bucketName;

    @Value("${oss.qy-zone-name}")
    private String zoneName;

    @Value("${oss.qy-access-key}")
    private String qyAccessKey;

    @Value("${oss.qy-access-secret}")
    private String qyAccessSecret;

    private EnvContext env;

    private Bucket qyBucket;

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(uploadEndpoint, accessId, accessKey);
        outerOssClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        env = new EnvContext(qyAccessKey, qyAccessSecret);
        qyBucket = new Bucket(env, zoneName, bucketName);
    }

    public String host() {
        // host的格式为 bucketname.endpoint
        return "http://" + bucket + "." + endpoint;
    }

    /**
     * 获取零时访问URL时长10分钟
     *
     * @param fileUrl
     * @return
     */
    public String getTemporaryAccessUrl(String fileUrl) {
        return getTemporaryAccessUrl(fileUrl, 600);
    }


    /**
     * @param fileUrl  文件URL
     * @param duration 时长秒
     * @return
     */
    public String getTemporaryAccessUrl(String fileUrl, int duration) {
        URL url = outerOssClient.generatePresignedUrl(bucket, fileUrl, new Date(System.currentTimeMillis() + duration * 1000L));
        return url.toString();
    }

    /**
     * 获取文件列表
     *
     * @return
     */
    public List<String> list() {
        List<String> urlList = new ArrayList<>();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        // 列举文件。 如果不设置KeyPrefix，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
        ObjectListing objectListing = ossClient.listObjects(bucket);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        sums.forEach(a -> {
            urlList.add(a.getKey());
        });
        // 关闭OSSClient。
        ossClient.shutdown();
        return urlList;
    }

    /**
     * 下载青云文件
     * test examples param 20200828/20200723081600_P020072113493220_00230000_13758582109.mp3
     *
     * @param objectKey
     * @return
     */
    public File getObjectFromQy(String objectKey) {
        try {
            Bucket.GetObjectInput input = new Bucket.GetObjectInput();
            Bucket.GetObjectOutput output = qyBucket.getObject(objectKey, input);
            InputStream inputStream = output.getBodyInputStream();
            if (output.getStatueCode() == 200) {
                if (inputStream != null) {
                    File file = File.createTempFile(UUID.randomUUID().toString(), objectKey.substring(objectKey.lastIndexOf(".")));
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    byte[] bytes = new byte[4096];
                    while ((len = inputStream.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    return file;
                } else {
                    logger.error("Get object status code == 200, but inputStream is null, skipped.");
                }
            }
            if (inputStream != null) inputStream.close();
        } catch (QSException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
