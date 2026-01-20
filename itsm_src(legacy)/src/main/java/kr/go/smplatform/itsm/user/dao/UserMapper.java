package kr.go.smplatform.itsm.user.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Mapper("userMapper")
public interface UserMapper {
	/**
	 * 사용자를 등록한다.
	 * @param vo - 등록할 정보가 담긴 UserVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void create(UserVO vo) throws Exception;
	
	/**
	 * 사용자를 수정한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(UserVO vo) throws Exception;
	
	/**
	 * 사용자 서명을 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int deleteSignature(String userId) throws Exception;

	/**
	 * 사용자를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int delete(UserVO vo) throws Exception;
	
	/**
	 * 사용자를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	UserVO retrieve(UserVO vo) throws Exception;
	
	/**
	 * 사용자 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<UserVO> retrievePagingList(UserVO vo) throws Exception;
	
	/**
	 * 사용자 총 갯수를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int retrievePagingListCnt(UserVO vo) throws Exception;
	
	/**
	 * 사용자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<UserVO> retrieveList(UserVO vo) throws Exception;
	
	/**
	 * 사용자 비밀번호 실패 카운트 증가
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updatePasswordFailCntAdd(UserVO vo) throws Exception;
	
	/**
	 * 사용자 비밀번호 실패 카운트 초기화
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updatePasswordFailCntReset(UserVO vo) throws Exception;
	
	/**
	 * 사용자 비밀번호 5회 이상 불일치시 계정 잠금 처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updatePasswordFailCntOverProcess(UserVO vo) throws Exception;
	
	/**
	 * 3개월에 1번 비밀번호 변경 유도 대상 사용자 확인 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	UserVO retrieveOverPasswordPeriod(UserVO vo) throws Exception;
}
