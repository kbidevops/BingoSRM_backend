package kr.go.smplatform.itsm.report.service;

import java.util.List;

import kr.go.smplatform.itsm.report.vo.RepAttachmentNameAndSizeVO;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.report.dao.RepMasterMapper;
import kr.go.smplatform.itsm.report.vo.RepMasterVO;

@Service("repMasterService")
public class RepMasterService {
	
	private final RepMasterMapper repMasterMapper;

	public RepMasterService(RepMasterMapper repMasterMapper) {
		this.repMasterMapper = repMasterMapper;
	}
	
	/**
	 * 보고서 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepMasterVO> retrievePagingList(RepMasterVO vo) throws Exception{
		return repMasterMapper.retrievePagingList(vo);
	}
	
	/**
	 * 보고서 생성
	 * @param vo
	 * @throws Exception
	 */
	public void create(RepMasterVO vo) throws Exception {
		repMasterMapper.create(vo);
	}
	
	/**
	 * 보고서 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public RepMasterVO retrieve(RepMasterVO vo) throws Exception{
		return repMasterMapper.retrieve(vo);
	}
	
	/**
	 * 보고서 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(RepMasterVO vo) throws Exception{
		return repMasterMapper.update(vo);
	}
	
	/**
	 * 보고서 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(RepMasterVO vo) throws Exception{
		return repMasterMapper.delete(vo);
	}
	
	/**
	 * 마지막 보고서 아이디
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Integer lastInsertedId(RepMasterVO vo) throws Exception{
		return repMasterMapper.lastInsertedId(vo);
	}
	
	/**
	 * 보고서 총개수 구하기
	 * @return 총개수
	 * @throws Exception
	 */
	public int totCnt(RepMasterVO vo) throws Exception{
		return repMasterMapper.totCnt(vo);
	}

	public List<RepAttachmentNameAndSizeVO> getAttachments(String repSn) {
		return repMasterMapper.selectAttachments(repSn);
	}
}
