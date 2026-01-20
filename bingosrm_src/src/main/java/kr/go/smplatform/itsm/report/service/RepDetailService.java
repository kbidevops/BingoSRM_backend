package kr.go.smplatform.itsm.report.service;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.report.dao.RepDetailMapper;
import kr.go.smplatform.itsm.report.vo.RepDetailVO;
import kr.go.smplatform.itsm.report.vo.RepMasterVO;

@Service("repDetailService")
public class RepDetailService {
	
	private final RepDetailMapper repDetailMapper;

	public RepDetailService(RepDetailMapper repDetailMapper) {
		this.repDetailMapper = repDetailMapper;
	}
	
	/**
	 * 보고서 페이징 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepDetailVO> retrievePagingList(RepDetailVO vo) throws Exception{
		return repDetailMapper.retrievePagingList(vo);
	}
	
	/**
	 * 보고서 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepDetailVO> retrieveList (RepDetailVO vo) throws Exception{
		return repDetailMapper.retrieveList(vo);
	}
	
	/**
	 * 보고서 총 갯수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int retrieveTotalCnt (RepDetailVO vo) throws Exception{
		return repDetailMapper.retrieveTotalCnt(vo);
	}
	
	/**
	 * 보고서 등록
	 * @param vo - 등록할 보고서 정보가 담긴 repDetailVO
	 * @throws Exception
	 */
	public void create(List<RepDetailVO> repDetailVOList) throws Exception{
		for(RepDetailVO vo: repDetailVOList) {
//			if(vo.getExecDesc().isEmpty() && vo.getPlanDesc().isEmpty()) continue;
			
			//textarea에 보이지않는 -- 입력되는 현상 조치
			vo.getExecDesc().replaceAll("­-", "-"); 
			vo.getPlanDesc().replaceAll("­-", "-");	
			repDetailMapper.create(vo);
			
		}
	}
	
	/**
	 * 보고서 수정
	 * @param repDetailVOList - 수정할 보고서 정보가 담긴 repDetailVO 리스트
	 * @return
	 * @throws Exception
	 */
	public int update(List<RepDetailVO> repDetailVOList) throws Exception{
		int updateCnt = 0;
		
		for(int i=0; i<repDetailVOList.size(); i++) {
			if(!repDetailVOList.get(i).getExecDesc().isEmpty() || !repDetailVOList.get(i).getPlanDesc().isEmpty()) {
				updateCnt += repDetailMapper.update(repDetailVOList.get(i));
			}
		}
		
		if(updateCnt == repDetailVOList.size())
			return 1;
		else
			return 0;
	}
	
	/**
	 * 보고서 조회
	 * @param vo - 조회할 보고서 정보가 담긴 RepDetailVO
	 * @return
	 * @throws Exception
	 */
	public RepDetailVO retrieve(RepDetailVO vo) throws Exception{
		return repDetailMapper.retrieve(vo);
	}
	
	/**
	 * 담당자 보고서 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<RepDetailVO> retrieveChargeList(RepDetailVO vo) throws Exception{
		return repDetailMapper.retrieveChargeList(vo);
	}
	
	/**
	 * 보고서 다중 삭제
	 * @param voList
	 * @return
	 * @throws Exception
	 */
	public int delete(List<RepDetailVO> voList) throws Exception{
		
		int deleteCnt = 0;
		
		for(RepDetailVO vo : voList) {
			deleteCnt += repDetailMapper.delete(vo);
		}
		
		if(deleteCnt == voList.size()) 
			deleteCnt = 1;
		else
			deleteCnt = 0;
		
		return deleteCnt;
	}
	
	/**
	 * 보고서 단일 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int deleteOne(RepDetailVO vo) throws Exception{
		return repDetailMapper.delete(vo);
	}
}
