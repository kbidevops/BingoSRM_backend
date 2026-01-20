package kr.go.smplatform.itsm.progrmaccesauthor.dao;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@Mapper("progrmAccesAuthorMapper")
public interface ProgrmAccesAuthorMapper {
	/**
	 * 프로그램접근권한를 등록한다.
	 * @param vo - 등록할 정보가 담긴 ProgrmAccesAuthorVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	Long create(ProgrmAccesAuthorVO vo) throws Exception;
	/**
	 * 프로그램접근권한 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int deleteList(ProgrmAccesAuthorVO vo) throws Exception;
	
	/**
	 * 프로그램접근권한 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<ProgrmAccesAuthorVO> retrieveList(ProgrmAccesAuthorVO vo) throws Exception;
	
	/**
	 * 프로그램접근권한 허용 목록을 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<ProgrmAccesAuthorVO> retrieveAssignList(ProgrmAccesAuthorVO vo) throws Exception;
}
