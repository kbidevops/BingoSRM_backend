package kr.go.smplatform.itsm.hist.use.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;

@Mapper
public interface HistUseMapper {
    void create(HistUseVO vo) throws Exception;
}
