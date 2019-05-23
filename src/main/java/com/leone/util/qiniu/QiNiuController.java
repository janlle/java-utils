package com.leone.util.qiniu;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * @author leone
 **/
@RestController
@RequestMapping("/api/qn")
public class QiNiuController {

    @Resource
    private QiNiuService qiNiuService;

    @GetMapping("/token")
    public Map<String, String> getToken(@RequestParam(required = false) String key) {
        return qiNiuService.getToken(key);
    }

    @PostMapping("/single")
    public List<String> single(@NotNull MultipartFile file) {
        return qiNiuService.upload(file);
    }

    @PostMapping("/batch")
    public List<String> batch(@NotNull MultipartFile[] files) {
        return qiNiuService.uploadBatch(files);
    }

    @PostMapping("/stream")
    public List<String> uploadByStream(InputStream inputStream) {
        return qiNiuService.uploadStream(inputStream);
    }

}