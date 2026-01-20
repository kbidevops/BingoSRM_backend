package kr.go.smplatform.itsm.hist.use.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;

@Mapper("histUseMapper")
public interface HistUseMapper {
	/**
	 * 사용이력을 기록 한다.
	 * @param vo
	 * @throws Exception
	 */
	void create(HistUseVO vo) throws Exception;
}
