package kr.go.smplatform.itsm.report.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.report.vo.RepAttachmentNameAndSizeVO;
import kr.go.smplatform.itsm.report.vo.RepDetailVO;
import kr.go.smplatform.itsm.report.vo.RepMasterVO;
import org.apache.ibatis.annotations.Param;

@Mapper("repMasterMapper")
public interface RepMasterMapper {
	/**
	 * 보고서 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<RepMasterVO> retrievePagingList(RepMasterVO vo) throws Exception;
	
	/**
	 * 보고서 생성
	 * @param vo
	 * @throws Execption
	 */
	void create(RepMasterVO vo) throws Exception;
	
	/**
	 * 보고서 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	RepMasterVO retrieve(RepMasterVO vo) throws Exception;
	
	/**
	 * 보고서 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(RepMasterVO vo) throws Exception;
	
	/**
	 * 보고서 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(RepMasterVO vo) throws Exception;
	
	/**
	 * 마지막 저장 보고서 id 가져오기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	Integer lastInsertedId (RepMasterVO vo) throws Exception;
	
	/**
	 * 보고서 총 개수
	 * @param vo
	 * @return 총 개수
	 * @throws Exception
	 */
	int totCnt(RepMasterVO vo) throws Exception;

	List<RepAttachmentNameAndSizeVO> selectAttachments(@Param("repSn") String repSn);
}
