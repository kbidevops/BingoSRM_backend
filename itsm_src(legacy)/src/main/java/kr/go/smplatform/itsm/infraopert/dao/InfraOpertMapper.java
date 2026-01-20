package kr.go.smplatform.itsm.infraopert.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.infraopert.vo.InfraOpertVO;

@Mapper("infraOpertMapper")
public interface InfraOpertMapper {
	
	/**
	 * 인프라작업리스트 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<InfraOpertVO> retrieveList(InfraOpertVO vo) throws Exception;
	
	/**
	 * 인프라작업 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	InfraOpertVO retrieve(InfraOpertVO vo) throws Exception;
	
	/**
	 * 인프라작업 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(InfraOpertVO vo) throws Exception;
	
	/**
	 * 인프라작업 수정
	 * @param vo
	 * @throws Exception
	 */
	void update(InfraOpertVO vo) throws Exception;
	
	/**
	 * 인프라번호 삭제번호 처리
	 * @param vo
	 * @throws Exception
	 */
	void deleteInfraOpertNo(InfraOpertVO vo) throws Exception;
	
	/**
	 * 인프라작업 삭제
	 * @param vo
	 * @throws Exception
	 */
	void delete(InfraOpertVO vo) throws Exception;
}
