package kr.go.smplatform.itsm.hist.login.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;

@Mapper("histLoginMapper")
public interface HistLoginMapper {
	/**
	 * 로그인 이력을 등록한다.
	 * @param vo - 등록할 정보가 담긴 HistLoginVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void create(HistLoginVO vo) throws Exception;
	
	/**
	 * 로그아웃 이력을 등록한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int update(HistLoginVO vo) throws Exception;
}
