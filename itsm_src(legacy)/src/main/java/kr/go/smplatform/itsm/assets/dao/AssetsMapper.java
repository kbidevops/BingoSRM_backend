package kr.go.smplatform.itsm.assets.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.assets.vo.AssetsVO;

@Mapper("assetsMapper")
public interface AssetsMapper {
	
	
	/**
	 * 자산관리 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 삭제
	 * @param vo
	 * @throws Exception
	 */
	void delete(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 수정
	 * @param vo
	 * @throws Exception
	 */
	void update(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<AssetsVO> retrievePagingList(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 목록 수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int retrieveListCnt(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	AssetsVO retrieve(AssetsVO vo) throws Exception;
	
	/**
	 * 자산관리 전체 조회
	 * @param vo
	 * @throws Exception
	 */
	List<AssetsVO> retrieveAllList(AssetsVO vo) throws Exception;
	
}
