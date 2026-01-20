package kr.go.smplatform.sms.service;

import com.google.gson.Gson;
import kr.go.smplatform.sms.vo.TalkVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class SmsUtil {
    private static final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger("SMS-UTIL");

    public static String sendTalk(final TalkVO talkVO) {
        logger.info("sms vo = {}", talkVO);

        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Authorization", "Bearer " + talkVO.getApiKey());

        final Map<String, String> map = new HashMap<>();
        map.put("custMsgSn", talkVO.getUserId());
        map.put("senderKey", talkVO.getProfileKey());
        map.put("phoneNum", talkVO.getReceiver());
        map.put("templateCode", talkVO.getTemplateCode());
        map.put("message", talkVO.getMessage());

        final HttpEntity<String> entity = new HttpEntity<>("[" + gson.toJson(map) + "]", headers);
        final ResponseEntity<String> response = restTemplate.exchange(talkVO.getUri(), HttpMethod.POST, entity, String.class);

        logger.info("sms api status = {}", response.getStatusCode());
        logger.info("sms api body = {}", response.getBody());

        return response.getBody();
    }
}
