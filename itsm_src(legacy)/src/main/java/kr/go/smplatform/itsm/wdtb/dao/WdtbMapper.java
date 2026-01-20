package kr.go.smplatform.itsm.wdtb.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;

@Mapper("wdtbMapper")
public interface WdtbMapper {
	
	/**
	 * 배포확인 페이징 리스트 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<WdtbVO> retrievePagingList(WdtbVO vo) throws Exception;
	
	/**
	 * 배포확인 리스트 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<WdtbVO> retrieveList(WdtbVO vo) throws Exception;
	
	/**
	 * 배포확인 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	WdtbVO retrieve(WdtbVO vo) throws Exception;
	
	/**
	 * 배포확인 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(WdtbVO vo) throws Exception;
	
	/**
	 * 배포확인 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(WdtbVO vo) throws Exception;
	
	/**
	 * 배포확인 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(WdtbVO vo) throws Exception;

}
