package kr.go.smplatform.itsm.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.user.dao.UserMapper;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Service("userService")
public class UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	/**
	 * 사용자를 등록한다.
	 * @param vo - 등록할 정보가 담긴 UserVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public boolean create(UserVO vo) throws Exception {
		boolean isSave = false;
		vo.encodeUserPassword();
		
		if (userMapper.retrieve(vo) == null) {
			userMapper.create(vo);
    		isSave = true;
    	}
    	
    	return isSave;
	}

	
	
	/**
	 * 사용자를 수정한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(UserVO vo) throws Exception {
		vo.encodeUserPassword();
		return userMapper.update(vo);
	}
	
	/**
	 * 사용자 서명을 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int deleteSignature(String userId) throws Exception {
		return userMapper.deleteSignature(userId);
	}

	/**
	 * 사용자를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int delete(UserVO vo) throws Exception {
		return userMapper.delete(vo);
	}
	
	/**
	 * 사용자를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public UserVO retrieve(UserVO vo) throws Exception {
		return userMapper.retrieve(vo);
	}
	
	/**
	 * 사용자 페이징 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> retrievePagingList(UserVO vo) throws Exception {
		return userMapper.retrievePagingList(vo);
	}
	
	/**
	 * 사용자 총 갯수를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int retrievePagingListCnt(UserVO vo) throws Exception {
		return userMapper.retrievePagingListCnt(vo);
	}
	
	/**
	 * 사용자 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> retrieveList(UserVO vo) throws Exception {
		return userMapper.retrieveList(vo);
	}
	
	/**
	 * 사용자 비밀번호 실패 카운트 증가
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePasswordFailCntAdd(UserVO vo) throws Exception {
		return userMapper.updatePasswordFailCntAdd(vo);
	}
	
	/**
	 * 사용자 비밀번호 실패 카운트 초기화
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePasswordFailCntReset(UserVO vo) throws Exception {
		return userMapper.updatePasswordFailCntReset(vo);
	}
	/**
	 * 사용자 비밀번호 5회 이상 불일치시 계정 잠금 처리
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePasswordFailCntOverProcess(UserVO vo) throws Exception {
		return userMapper.updatePasswordFailCntOverProcess(vo);
	}
	
	/**
	 * 3개월에 1번 비밀번호 변경 유도 대상 사용자 확인 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public UserVO retrieveOverPasswordPeriod(UserVO vo) throws Exception {
		return userMapper.retrieveOverPasswordPeriod(vo);
	}
}
