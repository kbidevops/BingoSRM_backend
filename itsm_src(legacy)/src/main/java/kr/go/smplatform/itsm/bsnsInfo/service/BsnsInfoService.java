package kr.go.smplatform.itsm.bsnsInfo.service;

import kr.go.smplatform.itsm.bsnsInfo.dao.BsnsInfoMapper;
import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BsnsInfoService {

    @Resource(name = "bsnsInfoMapper")
    private BsnsInfoMapper bsnsInfoMapper;

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
