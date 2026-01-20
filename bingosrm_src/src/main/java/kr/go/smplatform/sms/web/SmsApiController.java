package kr.go.smplatform.sms.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.sms.service.SmsService;
import kr.go.smplatform.sms.vo.SmsVO;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsApiController {
    private static final Logger logger = LoggerFactory.getLogger(SmsApiController.class);

    private final SmsService smsService;

    public SmsApiController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@RequestBody SmsVO request) throws Exception {
        if (request.getMsg() == null || request.getMsg().isEmpty()) {
            return ResponseEntity.badRequest().body(error("msg is required"));
        }
        if (request.getDestel() == null || request.getDestel().isEmpty()) {
            return ResponseEntity.badRequest().body(error("destel is required"));
        }
        if (request.getSendType() == null || request.getSendType().isEmpty()) {
            request.setSendType(msgSendType(request.getMsg()));
        }
        if (request.getSubject() == null || request.getSubject().isEmpty()) {
            request.setSubject("ITSM");
        }

        String result = smsService.create(request);
        logger.info("sms result = {}", result);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("sent", true);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    public static String msgSendType(String msg) {
        if (msg.getBytes().length > 80) {
            return "2";
        }
        return "1";
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("error", message);
        return response;
    }
}
