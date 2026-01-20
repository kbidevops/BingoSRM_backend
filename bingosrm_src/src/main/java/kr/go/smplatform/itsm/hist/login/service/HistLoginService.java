package kr.go.smplatform.itsm.hist.login.service;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.hist.login.dao.HistLoginMapper;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;

@Service
public class HistLoginService {
    private final HistLoginMapper histLoginMapper;

    public HistLoginService(HistLoginMapper histLoginMapper) {
        this.histLoginMapper = histLoginMapper;
    }

    public void create(HistLoginVO vo) throws Exception {
        histLoginMapper.create(vo);
    }

    public int update(HistLoginVO vo) throws Exception {
        return histLoginMapper.update(vo);
    }
}
