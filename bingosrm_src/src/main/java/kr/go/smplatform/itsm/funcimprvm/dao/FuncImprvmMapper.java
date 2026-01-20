package kr.go.smplatform.itsm.funcimprvm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;

@Mapper
public interface FuncImprvmMapper {
    List<FuncImprvmVO> retrievePagingList(FuncImprvmVO vo) throws Exception;

    List<FuncImprvmVO> retrieveList(FuncImprvmVO vo) throws Exception;

    FuncImprvmVO retrieve(FuncImprvmVO vo) throws Exception;

    void create(FuncImprvmVO vo) throws Exception;

    void update(FuncImprvmVO vo) throws Exception;

    void delete(FuncImprvmVO vo) throws Exception;
}
