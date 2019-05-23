package com.leone.util.qiniu;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 *
 * @author leone
 **/
@Service
public class QiNiuService {

    private final Logger logger = LoggerFactory.getLogger(QiNiuService.class);

    private Configuration configuration;

    private UploadManager uploadManager;

    private Auth auth;

    @Resource
    private QiNiuProperties properties;

    @PostConstruct
    public void init() {
        Zone zone;
        switch (properties.getZone()) {
            case BEI_MEI:
                zone = Zone.zoneNa0();
                break;
            case HUA_BEI:
                zone = Zone.zone1();
                break;
            case HUA_NAN:
                zone = Zone.zone2();
                break;
            case DONG_NAN_YA:
                zone = Zone.zoneAs0();
                break;
            default:
                zone = Zone.zone0();
                break;
        }
        configuration = new Configuration(zone);
        uploadManager = new UploadManager(configuration);
        auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {
        return auth.uploadToken(properties.getBucket());
    }

    /**
     * 获取token
     *
     * @param bucket
     * @return
     */
    public Map<String, String> getToken(String bucket) {
        String token = auth.uploadToken(properties.getBucket(), bucket);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    /**
     * 上传单个文件
     *
     * @param file
     * @return
     */
    public List<String> upload(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String token = getToken();
            Response res = uploadManager.put(fileBytes, null, token);
            QiNiu qiniu = res.jsonToObject(QiNiu.class);
            return Collections.singletonList(properties.getLinkAddress() + qiniu.getKey());
        } catch (IOException e) {
            logger.error("message:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 批量上传文件
     *
     * @param files
     * @return
     */
    public List<String> uploadBatch(MultipartFile[] files) {
        if (Objects.isNull(files)) {
            throw new RuntimeException("file array is empty");
        }
        List<String> list = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            try {
                byte[] fileBytes = files[i].getBytes();
                Response res = uploadManager.put(fileBytes, null, getToken());
                QiNiu qiniu = res.jsonToObject(QiNiu.class);
                list.add(properties.getLinkAddress() + qiniu.getKey());
            } catch (IOException e) {
                logger.error("message:{}", e.getMessage());
            }
        }
        return list;
    }

    /**
     * 上传流文件
     *
     * @param inputStream
     * @return
     */
    public List<String> uploadStream(InputStream inputStream) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            byte[] byteData = outputStream.toByteArray();
            Response res = uploadManager.put(byteData, null, getToken());
            QiNiu qiniu = res.jsonToObject(QiNiu.class);
            return Collections.singletonList(properties.getLinkAddress() + qiniu.getKey());
        } catch (IOException e) {
            logger.error("message:{}", e.getMessage());
            return null;
        }
    }

}
