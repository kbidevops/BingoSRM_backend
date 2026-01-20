package kr.go.smplatform.itsm.repcharger.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Mapper("repChargerMapper")
public interface RepChargerMapper {
	
	/**
	 * 전체 보고서 시스템 목록
	 * @return
	 * @throws Exception
	 */
	List<RepChargerVO> retrieveList(RepChargerVO vo) throws Exception;
	
	/**
	 * 담당 보고서 시스템 목록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepChargerVO> retrieveAssignList(RepChargerVO vo) throws Exception;
	
	/**
	 * 담당자 목록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepChargerVO> retrieveUsers(RepChargerVO vo) throws Exception;
	
	/**
	 * 담당자 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(RepChargerVO vo) throws Exception;
	
	/**
	 * 담당자 생성
	 * @param vo
	 * @throws Exception
	 */
	void create (RepChargerVO vo) throws Exception;
	
	/**
	 * 시스템 담당 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(RepChargerVO vo) throws Exception;

	int updateUserLocat(UserVO userVO);
}
