package kr.go.smplatform.itsm.hist.login.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;

@Mapper
public interface HistLoginMapper {
    void create(HistLoginVO vo) throws Exception;

    int update(HistLoginVO vo) throws Exception;
}
