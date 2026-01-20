package kr.go.smplatform.itsm.hist.use.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.hist.use.dao.HistUseMapper;
import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;
import kr.go.smplatform.itsm.user.service.UserService;

@Service("histUseService")
public class HistUseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Resource(name="histUseMapper")
	private HistUseMapper histUseMapper;
	
	/**
	 * 사용이력을 기록 한다.
	 * @param vo
	 * @throws Exception
	 */
	public void create(HistUseVO vo) throws Exception{
		histUseMapper.create(vo);
	}
}
