package kr.go.smplatform.sms.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.sms.vo.TalkCodesVO;

@Mapper
public interface SmsMapper {
    TalkCodesVO selectCodes();
}
