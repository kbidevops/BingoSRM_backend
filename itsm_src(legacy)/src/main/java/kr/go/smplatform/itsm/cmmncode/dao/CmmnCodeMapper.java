package kr.go.smplatform.itsm.cmmncode.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;

@Mapper("cmmnCodeMapper")
public interface CmmnCodeMapper {
	/**
	 * 공통코드를 등록한다.
	 * @param vo - 등록할 정보가 담긴 CmmnCodeVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void create(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드를 수정한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 삭제한 공통코드 복구한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int restore(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	CmmnCodeVO retrieve(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<CmmnCodeVO> retrieveList(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<CmmnCodeVO> retrievePagingList(CmmnCodeVO vo) throws Exception;
	
	/**
	 * 공통코드 총 갯수를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int retrievePagingListCnt(CmmnCodeVO vo) throws Exception;
}
