package com.leone.util.qiniuyun;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leone.util.common.RandomValue;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author: lyon
 * @since: 2018-08-21
 **/
@Service
public class QiNiuService {

    private final Logger logger = LoggerFactory.getLogger(QiNiuService.class);

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private QiniuProperties qiNiuProperties;


    public String getToken() {
        return Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey()).uploadToken(qiNiuProperties.getBucket());
    }

    public String getCmsToken(String key, long expires, StringMap policy, boolean strict) {
        return Auth.create(qiNiuProperties.getAccessKey(),
                qiNiuProperties.getSecretKey()).uploadToken(qiNiuProperties.getBucket(), key, expires, policy, strict);
    }

    public String getAddress() {
        return qiNiuProperties.getLinkAddress();
    }

    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return upload(file.getInputStream(), suffix);
    }

    /**
     * 上传文件
     *
     * @param inputStream
     * @param suffix
     * @return
     */
    public String upload(InputStream inputStream, String suffix) {
        logger.info("____________文件上传开始____________");
        String token = getToken();
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone2()));
        try {
            Response response = uploadManager.put(inputStream, RandomValue.randomStr(16) + "." + suffix, token, null, null);
            if (StringUtils.isEmpty(response.bodyString())) {
                logger.info("____________文件上传错误____________");
                return null;
            }
            logger.info("____________文件上传结束____________");
            String a = response.bodyString();
            Map<String, String> result = objectMapper.readValue(response.bodyString(), new TypeReference<Map<String, String>>() {
            });
            return result.get("key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组
     *
     * @param data
     * @param suffix
     * @return
     */
    public String upload(byte[] data, String suffix) {
        logger.info("____________文件上传开始(byte)____________");
        String token = getToken();
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone2()));
        try {
            Response response = uploadManager.put(data, RandomValue.randomStr(16) + "." + suffix, token);
            if (StringUtils.isEmpty(response.bodyString())) {
                logger.info("____________文件上传错误(byte)____________");
                return null;
            }
            logger.info("____________文件上传结束(byte)____________");
            Map<String, String> result = objectMapper.readValue(response.bodyString(), new TypeReference<Map<String, String>>() {
            });
            return result.get("key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
