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
//    private static final String DOMAIN = "https://wt-api.carrym.com:8443";
////    private static final String ENDPOINT = "/v3/A/{고객사 ID}/messages";
//    private static final String ENDPOINT = "/v3/A/klic1/messages";
//
//    /**
//     * 고객사 KEY
//     */
//    private static final String API_KEY = "F47232A18913A7A5090E157F72FCC712";
//
//    /**
//     * 고객사발송요청 ID (고객사에서 부여한 Unique Key)
//     */
//    private static final String USER_ID = "klic1";
//
//    /**
//     * 발신프로필키
//     */
//    private static final String PROFILE_KEY = "klic1";
//
//    /**
//     * 림톡 템플릿 코드
//     */
//    private static final String TEMPLATE_CODE = "A";
//
    private static final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger("SMS-UTIL");
//
//    public static String sendTalk(String receiver, String message) {
//        final RestTemplate restTemplate = new RestTemplate();
//
//        final HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        headers.add("Authorization", "Bearer " + API_KEY);
//
//        final Map<String, String> map = new HashMap<>();
//        map.put("custMsgSn", USER_ID);
//        map.put("senderKey", PROFILE_KEY);
//        map.put("phoneNum", receiver);
//        map.put("templateCode", TEMPLATE_CODE);
//        map.put("message", message);
//
//        final HttpEntity<String> entity = new HttpEntity<>("[" + GSON.toJson(map) + "]", headers);
//        final ResponseEntity<String> response = restTemplate.exchange(DOMAIN + ENDPOINT, HttpMethod.POST, entity, String.class);
//
//        logger.info("sms api status = {}", response.getStatusCode());
//        logger.info("sms api body = {}", response.getBody());
//
//        return response.getBody();
//    }

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
