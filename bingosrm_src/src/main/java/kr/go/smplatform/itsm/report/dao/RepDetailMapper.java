package kr.go.smplatform.itsm.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import kr.go.smplatform.itsm.report.vo.RepDetailVO;

@Mapper
public interface RepDetailMapper {
	/**
	 * 보고서 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepDetailVO> retrievePagingList(RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepDetailVO> retrieveList (RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서 총 갯수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int retrieveTotalCnt (RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서를 등록한다.
	 * @param vo - 등록할 정보가 담긴 RepDetailVO
	 * @throws Exception
	 */
	void create(RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서를 수정한다.
	 * @param vo - 수정할 정보가 담긴 RepDetailVO
	 * @return
	 * @throws Exception
	 */
	int update(RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	RepDetailVO retrieve(RepDetailVO vo) throws Exception;
	
	/**
	 * 담당자 보고서 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepDetailVO> retrieveChargeList (RepDetailVO vo) throws Exception;
	
	/**
	 * 보고서 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(RepDetailVO vo) throws Exception;
}
