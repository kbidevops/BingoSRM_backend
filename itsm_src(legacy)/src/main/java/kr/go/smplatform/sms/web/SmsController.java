package kr.go.smplatform.sms.web;

import kr.go.smplatform.sms.service.SmsService;
import kr.go.smplatform.sms.vo.SmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SmsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Resource(name = "smsService")
    SmsService smsService;
    
    @RequestMapping("/itsm/srvcrspons/mngr/smsTest.do")
    public String create() throws Exception {
        final SmsVO vo = new SmsVO();
        vo.setSubject("ITSM");
        vo.setMsg("테스트문자전송.");
        vo.setDestel("01033103930");
        vo.setSrctel("0443000762");

        try {
            final String result = smsService.create(vo);
            logger.info("sms vo = {}", vo);
            logger.info("sms result = {}", result);
        } catch (Exception e) {
            logger.error("sms failed = {}", vo);
            e.printStackTrace();
        }
        
        return "";
    }
    
    public static String msgSendType(String msg) {
        if (msg.getBytes().length > 80) {
            return "2";
        }
        
        return "1";
    }
}
