package kr.go.smplatform.itsm.assets.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.assets.vo.AssetsVO;

@Mapper
public interface AssetsMapper {
    void create(AssetsVO vo) throws Exception;
    void delete(AssetsVO vo) throws Exception;
    void update(AssetsVO vo) throws Exception;
    List<AssetsVO> retrievePagingList(AssetsVO vo) throws Exception;
    int retrieveListCnt(AssetsVO vo) throws Exception;
    AssetsVO retrieve(AssetsVO vo) throws Exception;
    List<AssetsVO> retrieveAllList(AssetsVO vo) throws Exception;
}
