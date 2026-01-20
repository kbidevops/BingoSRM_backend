package kr.go.smplatform.itsm.bsnsInfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;

@Mapper
public interface BsnsInfoMapper {
    int initBsnsInfo();
    List<ChargerUserInfoVO> selectChargerList(String userTyCode);
    BsnsInfoVO selectBsnsInfo();
    int updateBsnsInfo(BsnsInfoVO bsnsInfoVO);
}
