package kr.go.smplatform.itsm.bsnsInfo.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;

import java.util.List;

@Mapper("bsnsInfoMapper")
public interface BsnsInfoMapper {

    int initBsnsInfo();

    List<ChargerUserInfoVO> selectChargerList(String userTyCode);

    BsnsInfoVO selectBsnsInfo();

    int updateBsnsInfo(BsnsInfoVO bsnsInfoVO);
}
