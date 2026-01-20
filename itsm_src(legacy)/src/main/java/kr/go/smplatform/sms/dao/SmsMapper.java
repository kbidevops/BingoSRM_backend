package kr.go.smplatform.sms.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.sms.vo.TalkCodesVO;

@Mapper("smsMapper")
public interface SmsMapper {

    TalkCodesVO selectCodes();
}
