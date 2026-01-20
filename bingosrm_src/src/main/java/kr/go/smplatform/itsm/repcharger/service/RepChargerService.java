package kr.go.smplatform.itsm.repcharger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.repcharger.dao.RepChargerMapper;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Service
public class RepChargerService {
    private final RepChargerMapper repChargerMapper;

    public RepChargerService(RepChargerMapper repChargerMapper) {
        this.repChargerMapper = repChargerMapper;
    }

    public List<RepChargerVO> retrieveList(RepChargerVO vo) throws Exception {
        return repChargerMapper.retrieveList(vo);
    }

    public List<RepChargerVO> retrieveAssignList(RepChargerVO vo) throws Exception {
        return repChargerMapper.retrieveAssignList(vo);
    }

    public List<RepChargerVO> retrieveUsers(RepChargerVO vo) throws Exception {
        return repChargerMapper.retrieveUsers(vo);
    }

    public int delete(RepChargerVO vo) throws Exception {
        return repChargerMapper.delete(vo);
    }

    public int update(RepChargerVO vo) throws Exception {
        return repChargerMapper.update(vo);
    }

    public int updateUserLocat(UserVO userVO) {
        return repChargerMapper.updateUserLocat(userVO);
    }

    public void create(RepChargerVO vo) throws Exception {
        List<RepChargerVO> list = repChargerMapper.retrieveAssignList(vo);

        for (RepChargerVO chargerVO : list) {
            chargerVO.setDeleteYn("Y");
            repChargerMapper.delete(chargerVO);
        }

        for (String sysCode : vo.getSysCodes()) {
            RepChargerVO repChargerVO = new RepChargerVO();
            repChargerVO.setUserId(vo.getUserId());
            repChargerVO.setSysCode(sysCode);
            repChargerMapper.create(repChargerVO);
        }
    }
}
