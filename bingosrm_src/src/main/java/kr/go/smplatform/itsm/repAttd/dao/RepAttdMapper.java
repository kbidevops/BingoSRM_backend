package kr.go.smplatform.itsm.repAttd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

@Mapper
public interface RepAttdMapper {
    List<RepAttdVO> retrieveList(RepAttdVO vo) throws Exception;

    RepAttdVO retrieve(RepAttdVO vo) throws Exception;

    void create(RepAttdVO vo) throws Exception;

    int update(RepAttdVO vo) throws Exception;

    int delete(RepAttdVO vo) throws Exception;
}
