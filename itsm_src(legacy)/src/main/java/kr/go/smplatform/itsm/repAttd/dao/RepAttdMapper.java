package kr.go.smplatform.itsm.repAttd.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

@Mapper("repAttdMapper")
public interface RepAttdMapper {
	
	/**
	 * 근태 목록 가져오기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepAttdVO> retrieveList(RepAttdVO vo) throws Exception;
	
	RepAttdVO retrieve(RepAttdVO vo) throws Exception;
	
	/**
	 * 근태 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(RepAttdVO vo) throws Exception;
	
	/**
	 * 근태 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(RepAttdVO vo) throws Exception;
	
	/**
	 * 근태 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(RepAttdVO vo) throws Exception;
}
