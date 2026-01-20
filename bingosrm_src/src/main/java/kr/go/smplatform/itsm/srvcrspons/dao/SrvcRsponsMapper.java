package kr.go.smplatform.itsm.srvcrspons.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

/**
 * @author CEPARK
 *
 */
@Mapper
public interface SrvcRsponsMapper {
	/**
	 * SR정보를 등록한다. 요청 정보만 등록. 관리자에 의해 전체내용 등록시 update 호출하여 처리
	 * @param vo - 등록할 정보가 담긴 SrvcRsponsVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void create(SrvcRsponsVO vo) throws Exception;	
	
	
	/**
	 * SR요청정보를 수정한다. 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateRequst(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR요청 접수처리 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateReceive(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 1차응답 기록한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateRspons1st(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 처리내역을 등록한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateProcess(SrvcRsponsVO vo) throws Exception;
	int updateSrProcess(SrvcRsponsVO vo) throws Exception;
	int updateSrVerify(SrvcRsponsVO vo) throws Exception;
	int updateSrFinish(SrvcRsponsVO vo) throws Exception;
	int updateSrEv(SrvcRsponsVO vo) throws Exception;
	int updateSrEvReRequest(SrvcRsponsVO vo) throws Exception;
	int createSrReRequest(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 확인자의 확인 처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateCnfrmr(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR배포확인서 등록 처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateWdtbCnfirm(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 인프라작업 등록 처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInfraOpert(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * sms확인처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateSmsChk(SrvcRsponsVO vo) throws Exception;
	
	
	/**
	 * SR배포확인 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int deleteWdtbCnfirm(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 인프라작업 삭제
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int deleteInfraOpert(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보를 수정한다. 전체 데이터 갱신용(관리목적)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보를 삭제한다.(soft delete)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	SrvcRsponsVO retrieve(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrievePagingList(SrvcRsponsVO vo) throws Exception;
	
	List<SrvcRsponsVO> retrieveSrReqList(SrvcRsponsVO vo) throws Exception;
	
	List<SrvcRsponsVO> retrieveSrRcvList(SrvcRsponsVO vo) throws Exception;

	List<SrvcRsponsVO> retrieveSrProcList(SrvcRsponsVO vo) throws Exception;

	List<SrvcRsponsVO> retrieveSrVrList(SrvcRsponsVO vo) throws Exception;

	List<SrvcRsponsVO> retrieveSrFnList(SrvcRsponsVO vo) throws Exception;

	List<SrvcRsponsVO> retrieveSrEvList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 배포가 필요한  SR정보 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveWdtbPagingList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 인프라작업 체크된 SR정보 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveInfraOpertPagingList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 총 갯수를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int retrievePagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrReqPagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrRcvPagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrProcPagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrVrPagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrFnPagingListCnt(SrvcRsponsVO vo) throws Exception;
	int retrieveSrEvPagingListCnt(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 목록을 조회한다. 9건 제한
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 목록을 조회한다. 무제한
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveAllList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * SR정보 중 배포확인서가있는 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveAllwdtbList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 응용기능개선 체크된  SR페이징 목록조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrievefnctImprvmPagingList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 요청자 정보 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveRqesterNmList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * 1차요청자 정보 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveRqester1stNmList(SrvcRsponsVO vo) throws Exception;
	
	/**
	 * sr 번호 정보 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SrvcRsponsVO> retrieveSrvcRsponsNoList(SrvcRsponsVO vo) throws Exception;
	
}
