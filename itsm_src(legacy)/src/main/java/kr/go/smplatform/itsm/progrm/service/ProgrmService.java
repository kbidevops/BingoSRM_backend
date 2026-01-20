package kr.go.smplatform.itsm.progrm.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.progrm.dao.ProgrmMapper;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

@Service("progrmService")
public class ProgrmService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrmService.class);
	
	@Resource(name="progrmMapper")
	private ProgrmMapper progrmMapper;
	
	/**
	 * 프로그램(메뉴)을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ProgrmVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public Long create(ProgrmVO vo) throws Exception {
    	Long progrmSn = progrmMapper.create(vo);
        return progrmSn;
    }

    /**
	 * 프로그램(메뉴)을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ProgrmVO
	 * @return void형
	 * @exception Exception
	 */
    public int update(ProgrmVO vo) throws Exception {
        return progrmMapper.update(vo);
    }

    /**
	 * 프로그램(메뉴)을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ProgrmVO
	 * @return void형 
	 * @exception Exception
	 */
    public int delete(ProgrmVO vo) throws Exception {
        return progrmMapper.delete(vo);
    }

    /**
	 * 프로그램(메뉴)을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ProgrmVO
	 * @return 조회한 프로그램(메뉴)
	 * @exception Exception
	 */
    public ProgrmVO retrieve(ProgrmVO vo) throws Exception {
        return progrmMapper.retrieve(vo);
    }

    /**
	 * 프로그램(메뉴) 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ProgrmVO
	 * @return 프로그램(메뉴) 목록
	 * @exception Exception
	 */
	public List<ProgrmVO> retrieveList(ProgrmVO vo) throws Exception {
        return progrmMapper.retrieveList(vo);
    }
}
