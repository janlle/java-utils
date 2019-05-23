package com.leone.util.qiniu;

import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author leone
 **/
@Component
//@ConfigurationProperties(prefix = "module.qiniu")
public class QiNiuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String linkAddress;

    private ZoneEnum zone = ZoneEnum.HUA_DONG;

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

    public ZoneEnum getZone() {
        return zone;
    }

    public void setZone(ZoneEnum zone) {
        this.zone = zone;
    }
}
