package kr.go.smplatform.sms.service;

import kr.go.smplatform.sms.dao.SmsMapper;
import kr.go.smplatform.sms.vo.SmsVO;
import kr.go.smplatform.sms.vo.TalkCodesVO;
import kr.go.smplatform.sms.vo.TalkVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("smsService")
public class SmsService {
    
    @Resource(name = "smsMapper")
    private SmsMapper smsMapper;

    /**
     * 알림톡전송
     * @param vo
     * @return
     * @throws Exception
     */
    public String create(SmsVO vo) throws Exception{
        final TalkCodesVO codes = smsMapper.selectCodes();

        final TalkVO talkVO = TalkVO.Builder
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
