package kr.go.smplatform.itsm.syscharger.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

@Mapper("sysChargerMapper")
public interface SysChargerMapper {
	/**
	 * 시스템담당자를 등록한다.
	 * @param vo - 등록할 정보가 담긴 SysChargerVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	Long create(SysChargerVO vo) throws Exception;
	/**
	 * 시스템담당자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int deleteList(SysChargerVO vo) throws Exception;
	
	/**
	 * 시스템담당자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SysChargerVO> retrieveList(SysChargerVO vo) throws Exception;
	
	/**
	 * 배정된 시스템담당자 시스템 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SysChargerVO> retrieveAssignList(SysChargerVO vo) throws Exception;
	
	/**
	 * 배정된 시스템의 담당자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SysChargerVO> retrieveChargerList(SysChargerVO vo) throws Exception;
}
