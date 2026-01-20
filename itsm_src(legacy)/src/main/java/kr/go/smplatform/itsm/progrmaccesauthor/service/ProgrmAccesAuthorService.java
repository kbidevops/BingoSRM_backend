package kr.go.smplatform.itsm.progrmaccesauthor.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.progrmaccesauthor.dao.ProgrmAccesAuthorMapper;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorFormVO;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@Service("progrmAccesAuthorService")
public class ProgrmAccesAuthorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrmAccesAuthorService.class);
	
	@Resource(name="progrmAccesAuthorMapper")
	private ProgrmAccesAuthorMapper progrmAccesAuthorMapper;
	
	/**
	 * 프로그램접근권한을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ProgrmAccesAuthorVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public void createList(ProgrmAccesAuthorVO vo) throws Exception {
    	progrmAccesAuthorMapper.deleteList(vo);
    	for(String progrmSn:vo.getProgrmSns()){
    		vo.setProgrmSn(progrmSn);
    		progrmAccesAuthorMapper.create(vo);
    	}
    }
   

    /**
	 * 프로그램접근권한 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ProgrmAccesAuthorVO
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<ProgrmAccesAuthorVO> retrieveList(ProgrmAccesAuthorVO vo) throws Exception {
        return progrmAccesAuthorMapper.retrieveList(vo);
    }
	
	/**
	 * 프로그램접근권한 허용 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ProgrmAccesAuthorVO
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<ProgrmAccesAuthorVO> retrieveAssignList(ProgrmAccesAuthorVO vo) throws Exception {
        return progrmAccesAuthorMapper.retrieveAssignList(vo);
    }
}
