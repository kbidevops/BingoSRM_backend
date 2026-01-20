package kr.go.smplatform.itsm.hist.use.service;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.hist.use.dao.HistUseMapper;
import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;

@Service
public class HistUseService {
    private final HistUseMapper histUseMapper;

    public HistUseService(HistUseMapper histUseMapper) {
        this.histUseMapper = histUseMapper;
    }

    public void create(HistUseVO vo) throws Exception {
        histUseMapper.create(vo);
    }
}
