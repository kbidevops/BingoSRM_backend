package kr.go.smplatform.itsm.infraopert.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.infraopert.dao.InfraOpertMapper;
import kr.go.smplatform.itsm.infraopert.vo.InfraOpertVO;

@Service("infraOpertService")
public class InfraOpertService {
	
	private final InfraOpertMapper infraOpertMapper;

	public InfraOpertService(InfraOpertMapper infraOpertMapper) {
		this.infraOpertMapper = infraOpertMapper;
	}
	
	/**
	 * 인프라 작업 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(InfraOpertVO vo) throws Exception{
		infraOpertMapper.create(vo);
	}
	
	/**
	 * 인프라 작업 수정
	 * @param vo
	 * @throws Exception
	 */
	public void update(InfraOpertVO vo) throws Exception{
		infraOpertMapper.update(vo);
	}
	
	/**
	 * 인프라 작업 삭제
	 * @param vo
	 * @throws Exception
	 */
	public void delete(InfraOpertVO vo) throws Exception{
		infraOpertMapper.delete(vo);
		infraOpertMapper.deleteInfraOpertNo(vo);
	}
	
	/**
	 * 인프라 작업 리스트 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<InfraOpertVO> retrieveList(InfraOpertVO vo) throws Exception{
		return infraOpertMapper.retrieveList(vo);
	}
	
	/**
	 * 인프라 작업 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public InfraOpertVO retrieve(InfraOpertVO vo) throws Exception{
		return infraOpertMapper.retrieve(vo);
	}
	
}
