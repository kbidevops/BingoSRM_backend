package kr.go.smplatform.itsm.progrm.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

@Mapper("progrmMapper")
public interface ProgrmMapper {
	/**
	 * 프로그램(메뉴)를 등록한다.
	 * @param vo - 등록할 정보가 담긴 ProgrmVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	Long create(ProgrmVO vo) throws Exception;
	
	/**
	 * 프로그램(메뉴)를 수정한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(ProgrmVO vo) throws Exception;
	
	/**
	 * 프로그램(메뉴)를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(ProgrmVO vo) throws Exception;
	
	/**
	 * 프로그램(메뉴)를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	ProgrmVO retrieve(ProgrmVO vo) throws Exception;
	
	/**
	 * 프로그램(메뉴) 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<ProgrmVO> retrieveList(ProgrmVO vo) throws Exception;
}
