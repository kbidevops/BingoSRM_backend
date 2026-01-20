package kr.go.smplatform.itsm.repAttd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.repAttd.dao.RepAttdMapper;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

@Service
public class RepAttdService {
    private final RepAttdMapper repAttdMapper;

    public RepAttdService(RepAttdMapper repAttdMapper) {
        this.repAttdMapper = repAttdMapper;
    }

    public List<RepAttdVO> retrieveList(RepAttdVO vo) throws Exception {
        return repAttdMapper.retrieveList(vo);
    }

    public RepAttdVO retrieve(RepAttdVO vo) throws Exception {
        return repAttdMapper.retrieve(vo);
    }

    public void create(List<RepAttdVO> voList) throws Exception {
        for (RepAttdVO vo : voList) {
            if ("-".equals(vo.getAttdCode())) {
                continue;
            }
            repAttdMapper.create(vo);
        }
    }

    public void createOne(RepAttdVO vo) throws Exception {
        if (!"-".equals(vo.getAttdCode())) {
            repAttdMapper.create(vo);
        }
    }

    public int update(RepAttdVO vo) throws Exception {
        return repAttdMapper.update(vo);
    }

    public int deleteOne(RepAttdVO vo) throws Exception {
        return repAttdMapper.delete(vo);
    }

    public int delete(List<RepAttdVO> list) throws Exception {
        int cnt = 0;
        for (RepAttdVO vo : list) {
            cnt += repAttdMapper.delete(vo);
        }
        return cnt;
    }
}
