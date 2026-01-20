package kr.go.smplatform.itsm.syscharger.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.syscharger.dao.SysChargerMapper;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

@Service("sysChargerService")
public class SysChargerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysChargerService.class);
	
	@Resource(name="sysChargerMapper")
	private SysChargerMapper sysChargerMapper;
	
	/**
	 * 프로그램접근권한을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SysChargerVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public void createList(SysChargerVO vo) throws Exception {
    	sysChargerMapper.deleteList(vo);
    	for(String sysCode:vo.getSysCodes()){
    		vo.setSysCode(sysCode);
    		sysChargerMapper.create(vo);
    	}
    }
   

    /**
	 * 프로그램접근권한 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SysChargerVO
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<SysChargerVO> retrieveList(SysChargerVO vo) throws Exception {
        return sysChargerMapper.retrieveList(vo);
    }
	
	 /**
	 * 배정된 시스템담당자 시스템 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SysChargerVO
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<SysChargerVO> retrieveAssignList(SysChargerVO vo) throws Exception {
        return sysChargerMapper.retrieveAssignList(vo);
    }
	
	/**
	 * 배정된 시스템의 담당자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<SysChargerVO> retrieveChargerList(SysChargerVO vo) throws Exception {
		return sysChargerMapper.retrieveChargerList(vo);
	}
}
