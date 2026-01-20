package kr.go.smplatform.itsm.wdtb.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.srvcrspons.dao.SrvcRsponsMapper;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.wdtb.dao.WdtbMapper;
import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;

@Service("wdtbService")
public class WdtbService {
	
	@Resource(name="wdtbMapper")
	WdtbMapper wdtbMapper;
	
	@Resource(name="srvcRsponsMapper")
	SrvcRsponsMapper srvcRsponsMapper;
	
	/**
	 * 배포확인서 페이징 리스트를 가져온다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<WdtbVO> retrievePagingList(WdtbVO vo) throws Exception{
		return wdtbMapper.retrievePagingList(vo);
	}
	
	/**
	 * 배포확인서 리스트를 가져온다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<WdtbVO> retrieveList(WdtbVO vo) throws Exception{
		return wdtbMapper.retrieveList(vo);
	}
	
	/**
	 * 배포확인서를 가져온다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public WdtbVO retrieve(WdtbVO vo) throws Exception{
		return wdtbMapper.retrieve(vo);
	}
	
	/**
	 * 배포확인서 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(WdtbVO vo) throws Exception{
		return wdtbMapper.update(vo);
	}
	
	/**
	 * 배포확인서 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(WdtbVO vo) throws Exception{
		return wdtbMapper.delete(vo);
	}
	
	/**배포확인서 생성
	 * @param vo
	 * @param srvcRsponsNos
	 * @throws Exception
	 */
	public void create(WdtbVO vo) throws Exception{
		wdtbMapper.create(vo);
	}
	
}
