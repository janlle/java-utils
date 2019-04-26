package com.leone.util.qiniuyun;

import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(
//    prefix = "xxx.module.qiniu"
//)
public class QiniuProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String linkAddress;

    public QiniuProperties() {
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getLinkAddress() {
        return this.linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
}
