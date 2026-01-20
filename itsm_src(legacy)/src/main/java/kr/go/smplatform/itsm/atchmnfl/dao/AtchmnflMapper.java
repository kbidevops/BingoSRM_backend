package kr.go.smplatform.itsm.atchmnfl.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;

@Mapper("atchmnflMapper")
public interface AtchmnflMapper {
	/**
	 * 첨부파일을 등록한다.
	 * @param vo - 등록할 정보가 담긴 /**
	 * @return 등록 결과
	 * @exception Exception
	 */
	void create(AtchmnflVO vo) throws Exception;
	
	/**
	 * 첨부파일을 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(AtchmnflVO vo) throws Exception;
	
	/**
	 * 첨부파일을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	AtchmnflVO retrieve(AtchmnflVO vo) throws Exception;
	
	/**
	 * 공통코드 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<AtchmnflVO> retrieveList(AtchmnflVO vo) throws Exception;
}
