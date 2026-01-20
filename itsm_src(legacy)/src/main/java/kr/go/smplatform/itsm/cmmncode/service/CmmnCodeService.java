package kr.go.smplatform.itsm.cmmncode.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.cmmncode.dao.CmmnCodeMapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;

@Service("cmmnCodeService")
public class CmmnCodeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CmmnCodeService.class);
	@Resource(name="cmmnCodeMapper")
	private CmmnCodeMapper cmmnCodeMapper;
	
	/**
	 * 공통코드를 등록한다.
	 * @param vo - 등록할 정보가 담긴 CmmnCodeVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public void create(CmmnCodeVO vo) throws Exception{
		cmmnCodeMapper.create(vo);
	}
	
	
	/**
	 * 공통코드를 수정한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.update(vo);
	}
	
	/**
	 * 삭제한 공통코드를 복구한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int restore(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.restore(vo);
	}
	
	/**
	 * 공통코드를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.delete(vo);
	}
	
	/**
	 * 공통코드를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public CmmnCodeVO retrieve(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.retrieve(vo);
	}
	
	/**
	 * 공통코드 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<CmmnCodeVO> retrieveList(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.retrieveList(vo);
	}
	
	/**
	 * 공통코드 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<CmmnCodeVO> retrievePagingList(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.retrievePagingList(vo);
	}
	
	/**
	 * 공통코드 총 갯수를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int retrievePagingListCnt(CmmnCodeVO vo) throws Exception{
		return cmmnCodeMapper.retrievePagingListCnt(vo);
	}
}
