package com.leone.util.oss;

/**
 * <p>
 *
 * @author leone
 * @since 2020-06-23
 **/
public class OssVO {

    private String fileUrl;

    private String objectName;

    private Long duration;

    private String originName;

    public OssVO() {
    }

    public OssVO(String fileUrl, String objectName) {
        this.fileUrl = fileUrl;
        this.objectName = objectName;
    }

    public OssVO(String fileUrl, String objectName, Long duration) {
        this.fileUrl = fileUrl;
        this.objectName = objectName;
        this.duration = duration;
    }

    public OssVO(String fileUrl, String objectName, Long duration, String originName) {
        this.fileUrl = fileUrl;
        this.objectName = objectName;
        this.duration = duration;
        this.originName = originName;
    }

    public OssVO(String originName) {
        this.originName = originName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    @Override
    public String toString() {
        return "OssVO{" +
                "fileUrl='" + fileUrl + '\'' +
                ", objectName='" + objectName + '\'' +
                ", duration=" + duration +
                ", originName='" + originName + '\'' +
                '}';
    }
}
