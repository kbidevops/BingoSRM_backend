package kr.go.smplatform.itsm.wdtb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.wdtb.dao.WdtbMapper;
import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;

@Service
public class WdtbService {
    private final WdtbMapper wdtbMapper;

    public WdtbService(WdtbMapper wdtbMapper) {
        this.wdtbMapper = wdtbMapper;
    }

    public List<WdtbVO> retrievePagingList(WdtbVO vo) throws Exception {
        return wdtbMapper.retrievePagingList(vo);
    }

    public List<WdtbVO> retrieveList(WdtbVO vo) throws Exception {
        return wdtbMapper.retrieveList(vo);
    }

    public WdtbVO retrieve(WdtbVO vo) throws Exception {
        return wdtbMapper.retrieve(vo);
    }

    public int update(WdtbVO vo) throws Exception {
        return wdtbMapper.update(vo);
    }

    public int delete(WdtbVO vo) throws Exception {
        return wdtbMapper.delete(vo);
    }

    public void create(WdtbVO vo) throws Exception {
        wdtbMapper.create(vo);
    }
}
