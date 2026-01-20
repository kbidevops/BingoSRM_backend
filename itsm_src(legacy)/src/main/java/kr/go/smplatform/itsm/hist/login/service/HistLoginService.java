package kr.go.smplatform.itsm.hist.login.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.hist.login.dao.HistLoginMapper;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;
import kr.go.smplatform.itsm.user.service.UserService;

@Service("histLoginService")
public class HistLoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Resource(name="histLoginMapper")
	private HistLoginMapper histLoginMapper;
	
	/**
	 * 로그인 이력을 등록한다.
	 * @param vo - 등록할 정보가 담긴 HistLoginVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public void create(HistLoginVO vo) throws Exception{
		histLoginMapper.create(vo);
	}
	/**
	 * 로그아웃 이력을 등록한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int update(HistLoginVO vo) throws Exception{
		return histLoginMapper.update(vo);
	}
}
