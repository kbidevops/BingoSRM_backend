package kr.go.smplatform.itsm.repcharger.service;

import java.util.List;

import javax.annotation.Resource;

import kr.go.smplatform.itsm.user.vo.UserVO;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.repcharger.dao.RepChargerMapper;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;

@Service("repChargerService")
public class RepChargerService {
	
	@Resource(name = "repChargerMapper")
	private RepChargerMapper repChargerMapper;
	
	/**
	 * 전체 보고서 시스템 목록
	 * @return
	 * @throws Exception
	 */
	public List<RepChargerVO> retrieveList(RepChargerVO vo) throws Exception {
		return repChargerMapper.retrieveList(vo);
	}
	
	/**
	 * 담당 보고서 시스템 목록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepChargerVO> retrieveAssignList(RepChargerVO vo) throws Exception {
		return repChargerMapper.retrieveAssignList(vo);
	}
	
	/**
	 * 담당자 목록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepChargerVO> retrieveUsers(RepChargerVO vo) throws Exception {
		return repChargerMapper.retrieveUsers(vo);
	}
	
	/**
	 * 담당자 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(RepChargerVO vo) throws Exception {
		return repChargerMapper.delete(vo);
	}
	
	/**
	 * 담당 시스템 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(RepChargerVO vo) throws Exception {
		return repChargerMapper.update(vo);
	}

	public int updateUserLocat(UserVO userVO) {
		return repChargerMapper.updateUserLocat(userVO);
	}
	
	/**
	 * 담당자 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(RepChargerVO vo) throws Exception {
		final List<RepChargerVO> list = repChargerMapper.retrieveAssignList(vo);

		for (RepChargerVO chargerVO : list) {
			chargerVO.setDeleteYn("Y");
			repChargerMapper.delete(chargerVO);
		}

		for (String sysCode : vo.getSysCodes()) {
			final RepChargerVO repChargerVO = new RepChargerVO();
			repChargerVO.setUserId(vo.getUserId());
			repChargerVO.setSysCode(sysCode);
			repChargerMapper.create(repChargerVO);
		}
	}
	
}
