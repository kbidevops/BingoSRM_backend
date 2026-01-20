package kr.go.smplatform.itsm.syscharger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.syscharger.dao.SysChargerMapper;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

@Service
public class SysChargerService {
    private final SysChargerMapper sysChargerMapper;

    public SysChargerService(SysChargerMapper sysChargerMapper) {
        this.sysChargerMapper = sysChargerMapper;
    }

    public void createList(SysChargerVO vo) throws Exception {
        sysChargerMapper.deleteList(vo);
        for (String sysCode : vo.getSysCodes()) {
            vo.setSysCode(sysCode);
            sysChargerMapper.create(vo);
        }
    }

    public List<SysChargerVO> retrieveList(SysChargerVO vo) throws Exception {
        return sysChargerMapper.retrieveList(vo);
    }

    public List<SysChargerVO> retrieveAssignList(SysChargerVO vo) throws Exception {
        return sysChargerMapper.retrieveAssignList(vo);
    }

    public List<SysChargerVO> retrieveChargerList(SysChargerVO vo) throws Exception {
        return sysChargerMapper.retrieveChargerList(vo);
    }
}
