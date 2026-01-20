package kr.go.smplatform.sms.service;

import org.springframework.stereotype.Service;

import kr.go.smplatform.sms.dao.SmsMapper;
import kr.go.smplatform.sms.vo.SmsVO;
import kr.go.smplatform.sms.vo.TalkCodesVO;
import kr.go.smplatform.sms.vo.TalkVO;

@Service
public class SmsService {
    private final SmsMapper smsMapper;

    public SmsService(SmsMapper smsMapper) {
        this.smsMapper = smsMapper;
    }

    public String create(SmsVO vo) throws Exception {
        TalkCodesVO codes = smsMapper.selectCodes();

        TalkVO talkVO = TalkVO.Builder
                .aTalkVO()
                .uri(codes.getUri())
                .apiKey(codes.getApiKey())
                .userId(codes.getUserId())
                .profileKey(codes.getProfileKey())
                .templateCode(codes.getTemplateCode())
                .receiver(vo.getDestel())
                .message(vo.getMsg())
                .build();

        return SmsUtil.sendTalk(talkVO);
    }
}
