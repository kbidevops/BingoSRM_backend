package kr.go.smplatform.itsm.bsnsInfo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.bsnsInfo.dao.BsnsInfoMapper;
import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;

@Service
public class BsnsInfoService {
    private final BsnsInfoMapper bsnsInfoMapper;

    public BsnsInfoService(BsnsInfoMapper bsnsInfoMapper) {
        this.bsnsInfoMapper = bsnsInfoMapper;
    }

    public List<ChargerUserInfoVO> selectChargerList(String userTyCode) {
        return bsnsInfoMapper.selectChargerList(userTyCode);
    }

    public BsnsInfoVO selectBsnsInfo() {
        bsnsInfoMapper.initBsnsInfo();
        return bsnsInfoMapper.selectBsnsInfo();
    }

    public int updateBsnsInfo(BsnsInfoVO bsnsInfoVO) {
        return bsnsInfoMapper.updateBsnsInfo(bsnsInfoVO);
    }
}
