package kr.go.smplatform.itsm.funcimprvm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.funcimprvm.dao.FuncImprvmMapper;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

@Service("funcImprvmService")
public class FuncImprvmService {
	
	@Resource(name = "funcImprvmMapper")
	private FuncImprvmMapper funcImprvmMapper;
	
	/**
	 * 기능개선 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<FuncImprvmVO> retrievePagingList(FuncImprvmVO vo) throws Exception{
		return funcImprvmMapper.retrievePagingList(vo);
	}
	
	/**
	 * 기능개선 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public FuncImprvmVO retrieve(FuncImprvmVO vo) throws Exception{
		return funcImprvmMapper.retrieve(vo);
	}
	
	/**
	 * 기능개선 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<FuncImprvmVO> retrieveList(FuncImprvmVO vo) throws Exception{
		return funcImprvmMapper.retrieveList(vo);
	}
	
	/**
	 * 기능개선 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(FuncImprvmVO vo) throws Exception{
		funcImprvmMapper.create(vo);
	}
	
	/**
	 * 기능개선 수정
	 * @param vo
	 * @throws Exception
	 */
	public void update(FuncImprvmVO vo) throws Exception{
		funcImprvmMapper.update(vo);
	}
	
	/**
	 * 기능개선 삭제
	 * @param vo
	 * @throws Exception
	 */
	public void delete(FuncImprvmVO vo) throws Exception{
		funcImprvmMapper.delete(vo);
	}
}
