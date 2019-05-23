package com.leone.util.qiniuyun;

import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Leone
 * @since: 2018-08-06
 **/
@Slf4j
@RestController
@RequestMapping("/api/qn")
public class QiNiuController {

    @Resource
    private QiNiuService qiNiuService;

    @GetMapping("/token")
    public String token(@RequestParam(value = "key", required = false, defaultValue = "") String key,
                        @RequestParam(value = "expires", required = false, defaultValue = "3600") long expires,
                        @RequestParam(value = "policy", required = false) StringMap policy,
                        @RequestParam(value = "strict", required = false, defaultValue = "true") boolean strict) {
        return policy != null ? qiNiuService.getCmsToken(key, expires, policy, strict) : qiNiuService.getCmsToken(key, expires, null, strict);
    }

    @ApiOperation(value = "获取Token")
    @GetMapping(value = "/tokenAndDomain")
    public QiNiuTokenVo tokenAndDomain(@RequestParam(value = "key", required = false, defaultValue = "") String key,
                                       @RequestParam(value = "expires", required = false, defaultValue = "3600") long expires,
                                       @RequestParam(value = "policy", required = false) StringMap policy,
                                       @RequestParam(value = "strict", required = false, defaultValue = "true") boolean strict) {
        String token = policy != null ? qiNiuService.getCmsToken(key, expires, policy, strict) :
                qiNiuService.getCmsToken(key, expires, null, strict);
        return new QiNiuTokenVo(token, qiNiuService.getAddress());
    }

    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传图片")
    public Map<String, String> upload(MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap();
        if (file != null) {
            map.put("imageUrl", this.qiNiuService.upload(file));
            return map;
        } else {
            map.put("imageUrl", "");
            return map;
        }
    }

}
