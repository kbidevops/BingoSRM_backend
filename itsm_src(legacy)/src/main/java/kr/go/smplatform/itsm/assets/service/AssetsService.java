package kr.go.smplatform.itsm.assets.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.assets.dao.AssetsMapper;
import kr.go.smplatform.itsm.assets.vo.AssetsVO;

@Service("assetsService")
public class AssetsService {

	@Resource(name = "assetsMapper")
	private AssetsMapper assetsMapper;
	
	/**
	 * 자산관리 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(AssetsVO vo) throws Exception{
		assetsMapper.create(vo);
	};
	
	/**
	 * 자산관리 삭제
	 * @param vo
	 * @throws Exception
	 */
	public void delete(AssetsVO vo) throws Exception{
		assetsMapper.delete(vo);
	};
	
	/**
	 * 자산관리 수정
	 * @param vo
	 * @throws Exception
	 */
	public void update(AssetsVO vo) throws Exception{
		assetsMapper.update(vo);
	};
	
	/**
	 * 자산관리 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<AssetsVO> retrievePagingList(AssetsVO vo) throws Exception{
		return assetsMapper.retrievePagingList(vo);
	}
	
	/**
	 * 자산관리 목록 수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int retrieveListCnt(AssetsVO vo) throws Exception{
		return assetsMapper.retrieveListCnt(vo);
	}
	
	/**
	 * 자산관리 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AssetsVO retrieve(AssetsVO vo) throws Exception{
		return assetsMapper.retrieve(vo);
	};
	
	/**
	 * 자산관리 전체 조회
	 * @throws Exception
	 */
	public List<AssetsVO> retrieveAllList(AssetsVO vo) throws Exception{
		return assetsMapper.retrieveAllList(vo);
	};
}
