package kr.go.smplatform.itsm.config;

import kr.go.smplatform.itsm.config.dao.ConfigMapper;
import kr.go.smplatform.sms.service.SmsService;
import kr.go.smplatform.sms.vo.SmsVO;
import kr.go.smplatform.sms.web.SmsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Scheduler {
    @Resource(name = "configMapper")
    private ConfigMapper configMapper;

    @Resource(name = "smsService")
    private SmsService smsService;
    private final Logger logger = LoggerFactory.getLogger("SMS-Scheduler");

    @Scheduled(cron = "0 0/5 * * * *")
    public void job() {
        configMapper
                .selectResponseRequired()
                .forEach(vo -> {
                    SmsVO smsVo = new SmsVO();
                    smsVo.setSubject("ITSM");
                    smsVo.setMsg("SR 요청을 확인해 주세요.");
                    smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
                    smsVo.setDestel(vo.getPhone());
                    smsVo.setSrctel("0443000762");

                    try {
                        final String result = smsService.create(smsVo);
                        logger.info("sms vo = {}", smsVo);
                        logger.info("sms result = {}", result);
                    } catch (Exception e) {
                        logger.error("sms failed = {}", smsVo);
                        e.printStackTrace();
                    }

                    configMapper.updateChkY(vo.getUserId());
                });
    }
}
