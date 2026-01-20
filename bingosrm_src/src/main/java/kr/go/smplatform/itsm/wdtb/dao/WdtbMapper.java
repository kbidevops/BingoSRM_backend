package kr.go.smplatform.itsm.wdtb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;

@Mapper
public interface WdtbMapper {
    List<WdtbVO> retrievePagingList(WdtbVO vo) throws Exception;

    List<WdtbVO> retrieveList(WdtbVO vo) throws Exception;

    WdtbVO retrieve(WdtbVO vo) throws Exception;

    int update(WdtbVO vo) throws Exception;

    int delete(WdtbVO vo) throws Exception;

    void create(WdtbVO vo) throws Exception;
}
