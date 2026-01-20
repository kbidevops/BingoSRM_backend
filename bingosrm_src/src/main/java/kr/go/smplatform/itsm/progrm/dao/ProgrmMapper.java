package kr.go.smplatform.itsm.progrm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

@Mapper
public interface ProgrmMapper {
    Long create(ProgrmVO vo) throws Exception;

    int update(ProgrmVO vo) throws Exception;

    int delete(ProgrmVO vo) throws Exception;

    ProgrmVO retrieve(ProgrmVO vo) throws Exception;

    List<ProgrmVO> retrieveList(ProgrmVO vo) throws Exception;
}
