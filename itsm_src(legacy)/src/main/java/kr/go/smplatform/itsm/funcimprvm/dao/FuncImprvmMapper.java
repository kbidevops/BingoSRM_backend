package kr.go.smplatform.itsm.funcimprvm.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;

@Mapper("funcImprvmMapper")
public interface FuncImprvmMapper {
	
	/**
	 * 기능개선 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<FuncImprvmVO> retrievePagingList(FuncImprvmVO vo) throws Exception;
	
	/**
	 * 기능개선 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<FuncImprvmVO> retrieveList(FuncImprvmVO vo) throws Exception;
	
	/**
	 * 기능개선 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	FuncImprvmVO retrieve(FuncImprvmVO vo) throws Exception;
	
	/**
	 * 기능개선 생성
	 * @param vo
	 * @throws Exception
	 */
	void create(FuncImprvmVO vo) throws Exception;
	
	/**
	 * 기능개선 수정
	 * @param vo
	 * @throws Exception
	 */
	void update(FuncImprvmVO vo) throws Exception;
	
	
	/**
	 * 기능개선 삭제
	 * @param vo
	 * @throws Exception
	 */
	void delete(FuncImprvmVO vo) throws Exception;
}
