package com.leone.util.kd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leone.util.kd.dto.Traces;
import com.leone.util.web.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 *
 * @author leone
 * @since 2018-09-18
 **/
@Slf4j
@Service
public class AliExpressService {

    private static final String appCode = "ee2aced563ba47219e8c80b1b92efb22";

    private static String url = "http://wuliu.market.alicloudapi.com/kdi?no=%s&type=%s";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        System.out.println(findKd("3366041775146", "STO").toString());
    }

    public static KdVO findKd(String expressNumber, String type) {
        KdVO vo = new KdVO();
        url = String.format(url, expressNumber, type);
        String stateStr = "暂无物流信息";
        Map<String, Object> headers = Collections.singletonMap("Authorization", "APPCODE " + appCode);
        Traces trace;
        try {
            String result = HttpUtil.sendGet(url, null, headers);
            trace = objectMapper.readValue(result, Traces.class);
            log.info("trace:{}", trace);
            if (trace.getStatus().equals("0")) {
                String status = trace.getResult().getDeliverystatus();
                switch (status) {
                    case "1":
                        stateStr = "在途中";
                        break;
                    case "2":
                        stateStr = "正在派件";
                        break;
                    case "3":
                        stateStr = "签收";
                        break;
                    case "4":
                        stateStr = "派送失败";
                        break;
                }
                vo.setDetail(trace.getResult().getList());
                vo.setShipper(trace.getResult().getExpName());
                vo.setStatus(stateStr);
                vo.setShipperCode(trace.getResult().getNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

}
