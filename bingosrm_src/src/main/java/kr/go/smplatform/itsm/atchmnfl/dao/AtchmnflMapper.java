package kr.go.smplatform.itsm.atchmnfl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;

@Mapper
public interface AtchmnflMapper {
    void create(AtchmnflVO vo) throws Exception;
    int delete(AtchmnflVO vo) throws Exception;
    AtchmnflVO retrieve(AtchmnflVO vo) throws Exception;
    List<AtchmnflVO> retrieveList(AtchmnflVO vo) throws Exception;
}
