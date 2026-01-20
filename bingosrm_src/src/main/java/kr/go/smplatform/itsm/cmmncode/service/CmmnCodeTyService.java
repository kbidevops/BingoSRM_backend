package kr.go.smplatform.itsm.cmmncode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.cmmncode.dao.CmmnCodeTyMapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeTyVO;

@Service
public class CmmnCodeTyService {
    private final CmmnCodeTyMapper cmmnCodeTyMapper;

    public CmmnCodeTyService(CmmnCodeTyMapper cmmnCodeTyMapper) {
        this.cmmnCodeTyMapper = cmmnCodeTyMapper;
    }

    public List<CmmnCodeTyVO> retrieveList(CmmnCodeTyVO vo) throws Exception {
        return cmmnCodeTyMapper.retrieveList(vo);
    }

    public List<CmmnCodeTyVO> retrieveAllList() throws Exception {
        return cmmnCodeTyMapper.retrieveAllList();
    }

    public void create(CmmnCodeTyVO vo) throws Exception {
        cmmnCodeTyMapper.create(vo);
    }

    public int update(CmmnCodeTyVO vo) throws Exception {
        return cmmnCodeTyMapper.update(vo);
    }

    public int delete(CmmnCodeTyVO vo) throws Exception {
        return cmmnCodeTyMapper.delete(vo);
    }
}
