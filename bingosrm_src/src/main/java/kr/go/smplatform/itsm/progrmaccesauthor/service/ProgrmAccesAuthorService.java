package kr.go.smplatform.itsm.progrmaccesauthor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.progrmaccesauthor.dao.ProgrmAccesAuthorMapper;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@Service
public class ProgrmAccesAuthorService {
    private final ProgrmAccesAuthorMapper progrmAccesAuthorMapper;

    public ProgrmAccesAuthorService(ProgrmAccesAuthorMapper progrmAccesAuthorMapper) {
        this.progrmAccesAuthorMapper = progrmAccesAuthorMapper;
    }

    public void createList(ProgrmAccesAuthorVO vo) throws Exception {
        progrmAccesAuthorMapper.deleteList(vo);
        for (String progrmSn : vo.getProgrmSns()) {
            vo.setProgrmSn(progrmSn);
            progrmAccesAuthorMapper.create(vo);
        }
    }

    public List<ProgrmAccesAuthorVO> retrieveList(ProgrmAccesAuthorVO vo) throws Exception {
        return progrmAccesAuthorMapper.retrieveList(vo);
    }

    public List<ProgrmAccesAuthorVO> retrieveAssignList(ProgrmAccesAuthorVO vo) throws Exception {
        return progrmAccesAuthorMapper.retrieveAssignList(vo);
    }
}
