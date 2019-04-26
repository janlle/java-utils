package com.leone.util.qiniuyun;


/**
 * @author: leone
 * @since: 2018-08-21
 **/
public class QiNiuTokenVo {

    private String token;

    private String linkAddress;

    public QiNiuTokenVo() {
    }

    public QiNiuTokenVo(String token, String linkAddress) {
        this.token = token;
        this.linkAddress = linkAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
}