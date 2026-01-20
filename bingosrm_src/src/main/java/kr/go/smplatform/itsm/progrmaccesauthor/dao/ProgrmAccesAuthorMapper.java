package kr.go.smplatform.itsm.progrmaccesauthor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@Mapper
public interface ProgrmAccesAuthorMapper {
    Long create(ProgrmAccesAuthorVO vo) throws Exception;
    int deleteList(ProgrmAccesAuthorVO vo) throws Exception;
    List<ProgrmAccesAuthorVO> retrieveList(ProgrmAccesAuthorVO vo) throws Exception;
    List<ProgrmAccesAuthorVO> retrieveAssignList(ProgrmAccesAuthorVO vo) throws Exception;
}
