package kr.go.smplatform.itsm.cmmncode.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeTyVO;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;

@Mapper("cmmnCodeTyMapper")
public interface CmmnCodeTyMapper {
	
	/**
	 * 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<CmmnCodeTyVO> retrieveList(CmmnCodeTyVO vo) throws Exception;
	
	/**
	 * 모든 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<CmmnCodeTyVO> retrieveAllList() throws Exception;
	
	/**
	 * 코드 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(CmmnCodeTyVO vo) throws Exception;
	
	/**
	 * 코드 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(CmmnCodeTyVO vo) throws Exception;
	
	/**
	 * 코드 삭제 여부 변경
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(CmmnCodeTyVO vo) throws Exception;
}
