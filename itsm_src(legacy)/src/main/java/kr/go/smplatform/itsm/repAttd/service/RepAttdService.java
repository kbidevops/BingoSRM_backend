package kr.go.smplatform.itsm.repAttd.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.repAttd.dao.RepAttdMapper;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

@Service("repAttdService")
public class RepAttdService {
	
	@Resource(name = "repAttdMapper")
	private RepAttdMapper repAttdMapper;
	
	/**
	 * 근태 목록 가져오기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepAttdVO> retrieveList(RepAttdVO vo) throws Exception{
		return repAttdMapper.retrieveList(vo);
	}
	
	/**
	 * 근태 가져오기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public RepAttdVO retrieve(RepAttdVO vo) throws Exception{
		return repAttdMapper.retrieve(vo);
	}
	
	/**
	 * 다중 근태 생성
	 * @param voList
	 * @throws Exception
	 */
	public void create(List<RepAttdVO> voList) throws Exception{
		
		for(RepAttdVO vo : voList) {
			if("-".equals(vo.getAttdCode())) continue;
			repAttdMapper.create(vo);
		}
		
	}
	
	/**
	 * 단일 근태 생성
	 * @param vo
	 * @throws Exception
	 */
	public void createOne(RepAttdVO vo) throws Exception{
		if(!"-".equals(vo.getAttdCode())) {
			repAttdMapper.create(vo);
		}
	}
	
	/**
	 * 근태 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(RepAttdVO vo) throws Exception {
		return repAttdMapper.update(vo);
	}
	
	/**
	 * 단일 근태 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int deleteOne(RepAttdVO vo) throws Exception {
		return repAttdMapper.delete(vo);
	}
	
	/**
	 * 다중 근태 삭제
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int delete(List<RepAttdVO> list) throws Exception {
		int cnt = 0;
		for(RepAttdVO vo : list) {
			cnt += repAttdMapper.delete(vo);
			
		}
		return cnt;
	}
}
