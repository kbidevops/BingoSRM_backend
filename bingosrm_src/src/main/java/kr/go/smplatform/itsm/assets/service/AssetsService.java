package kr.go.smplatform.itsm.assets.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.assets.dao.AssetsMapper;
import kr.go.smplatform.itsm.assets.vo.AssetsVO;

@Service
public class AssetsService {
    private final AssetsMapper assetsMapper;

    public AssetsService(AssetsMapper assetsMapper) {
        this.assetsMapper = assetsMapper;
    }

    public void create(AssetsVO vo) throws Exception {
        assetsMapper.create(vo);
    }

    public void delete(AssetsVO vo) throws Exception {
        assetsMapper.delete(vo);
    }

    public void update(AssetsVO vo) throws Exception {
        assetsMapper.update(vo);
    }

    public List<AssetsVO> retrievePagingList(AssetsVO vo) throws Exception {
        return assetsMapper.retrievePagingList(vo);
    }

    public int retrieveListCnt(AssetsVO vo) throws Exception {
        return assetsMapper.retrieveListCnt(vo);
    }

    public AssetsVO retrieve(AssetsVO vo) throws Exception {
        return assetsMapper.retrieve(vo);
    }

    public List<AssetsVO> retrieveAllList(AssetsVO vo) throws Exception {
        return assetsMapper.retrieveAllList(vo);
    }
}
