package kr.go.smplatform.itsm.funcimprvm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.funcimprvm.dao.FuncImprvmMapper;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;

@Service
public class FuncImprvmService {
    private final FuncImprvmMapper funcImprvmMapper;

    public FuncImprvmService(FuncImprvmMapper funcImprvmMapper) {
        this.funcImprvmMapper = funcImprvmMapper;
    }

    public List<FuncImprvmVO> retrievePagingList(FuncImprvmVO vo) throws Exception {
        return funcImprvmMapper.retrievePagingList(vo);
    }

    public FuncImprvmVO retrieve(FuncImprvmVO vo) throws Exception {
        return funcImprvmMapper.retrieve(vo);
    }

    public List<FuncImprvmVO> retrieveList(FuncImprvmVO vo) throws Exception {
        return funcImprvmMapper.retrieveList(vo);
    }

    public void create(FuncImprvmVO vo) throws Exception {
        funcImprvmMapper.create(vo);
    }

    public void update(FuncImprvmVO vo) throws Exception {
        funcImprvmMapper.update(vo);
    }

    public void delete(FuncImprvmVO vo) throws Exception {
        funcImprvmMapper.delete(vo);
    }
}
