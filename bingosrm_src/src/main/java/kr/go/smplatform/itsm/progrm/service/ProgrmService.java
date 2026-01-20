package kr.go.smplatform.itsm.progrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.progrm.dao.ProgrmMapper;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

@Service
public class ProgrmService {
    private final ProgrmMapper progrmMapper;

    public ProgrmService(ProgrmMapper progrmMapper) {
        this.progrmMapper = progrmMapper;
    }

    public Long create(ProgrmVO vo) throws Exception {
        return progrmMapper.create(vo);
    }

    public int update(ProgrmVO vo) throws Exception {
        return progrmMapper.update(vo);
    }

    public int delete(ProgrmVO vo) throws Exception {
        return progrmMapper.delete(vo);
    }

    public ProgrmVO retrieve(ProgrmVO vo) throws Exception {
        return progrmMapper.retrieve(vo);
    }

    public List<ProgrmVO> retrieveList(ProgrmVO vo) throws Exception {
        return progrmMapper.retrieveList(vo);
    }
}
