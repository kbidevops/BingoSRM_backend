package kr.go.smplatform.itsm.atchmnfl.service;

import kr.go.smplatform.itsm.atchmnfl.dao.AtchmnflMapper;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeService;
import kr.go.smplatform.itsm.config.ITSMDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("atchmnflService")
public class AtchmnflService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmmnCodeService.class);
    @Resource(name="atchmnflMapper")
    private AtchmnflMapper atchmnflMapper;

    /**
     * 파일 저장 및 등록
     * @param file
     * @return
     * @throws Exception
     */
    public AtchmnflVO saveFile(MultipartFile file) throws Exception {
        return saveFile(file, UUID.randomUUID().toString());
    }

    /**
     * 파일 저장 및 등록
     * @param file
     * @return
     * @throws Exception
     */
    public AtchmnflVO saveFile(MultipartFile file, String atchfileId) throws Exception {
        final String fileSaveName = UUID.randomUUID().toString();
        final Date now = new Date();
        final Path directory = Paths
                .get(ITSMDefine.rootPath)
                .resolve("file")
                .resolve(new SimpleDateFormat("yyyy").format(now))
                .resolve(new SimpleDateFormat("MM").format(now));

        if (!directory.toFile().exists()) {
            directory.toFile().mkdirs();
        }

        final Path filePath = directory.resolve(fileSaveName);
        final String streAllCours = filePath.toString();

        file.transferTo(filePath.toFile());

        final AtchmnflVO atchmnflVO = new AtchmnflVO();

        atchmnflVO.setAtchmnflId(atchfileId);
        atchmnflVO.setStreAllCours(streAllCours);
        atchmnflVO.setOrginlFileNm(file.getOriginalFilename());
        atchmnflVO.setFileSize(file.getSize());

        create(atchmnflVO);

        return atchmnflVO;
    }
    
    /**
     * 첨부파일을 등록한다.
     * @param vo - 등록할 정보가 담긴 /**
     * @return 등록 결과
     * @exception Exception
     */
    public void create(AtchmnflVO vo) throws Exception {
        atchmnflMapper.create(vo);
    }
    
    /**
     * 첨부파일을 삭제한다.
     * @param vo
     * @return
     * @throws Exception
     */
    public int delete(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.delete(vo);
    }
    
    /**
     * 첨부파일을 조회한다.
     * @param vo
     * @return
     * @throws Exception
     */
    public AtchmnflVO retrieve(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.retrieve(vo);
    }
    
    /**
     * 공통코드 목록을 조회한다.
     * @param vo
     * @return
     * @throws Exception
     */
    public List<AtchmnflVO> retrieveList(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.retrieveList(vo);
    }
}
