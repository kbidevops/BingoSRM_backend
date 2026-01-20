package kr.go.smplatform.itsm.cmmncode.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.cmmncode.dao.CmmnCodeTyMapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeTyVO;

@Service("cmmnCodeTyService")
public class CmmnCodeTyService {
	
	@Resource(name="cmmnCodeTyMapper")
	private CmmnCodeTyMapper cmmnCodeTyMapper;
	
	/**
	 * 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<CmmnCodeTyVO> retrieveList(CmmnCodeTyVO vo) throws Exception{
		return cmmnCodeTyMapper.retrieveList(vo);
	}
	
	/**
	 * 모든 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<CmmnCodeTyVO> retrieveAllList() throws Exception{
		return cmmnCodeTyMapper.retrieveAllList();
	}
	
	
	/**
	 * 코드 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(CmmnCodeTyVO vo) throws Exception{
		cmmnCodeTyMapper.create(vo);
	}
	
	/**
	 * 코드 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(CmmnCodeTyVO vo) throws Exception{
		return cmmnCodeTyMapper.update(vo);
	}
	
	/**
	 * 코드 삭제여부 변경
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(CmmnCodeTyVO vo) throws Exception{
		return cmmnCodeTyMapper.delete(vo);
	}
}
