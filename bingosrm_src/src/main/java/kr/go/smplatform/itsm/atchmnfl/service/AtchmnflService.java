package kr.go.smplatform.itsm.atchmnfl.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.go.smplatform.itsm.atchmnfl.dao.AtchmnflMapper;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.config.ITSMDefine;

@Service
public class AtchmnflService {
    private final AtchmnflMapper atchmnflMapper;

    public AtchmnflService(AtchmnflMapper atchmnflMapper) {
        this.atchmnflMapper = atchmnflMapper;
    }

    public AtchmnflVO saveFile(MultipartFile file) throws Exception {
        return saveFile(file, UUID.randomUUID().toString());
    }

    public AtchmnflVO saveFile(MultipartFile file, String atchfileId) throws Exception {
        String fileSaveName = UUID.randomUUID().toString();
        Date now = new Date();
        Path directory = Paths
                .get(ITSMDefine.rootPath)
                .resolve("file")
                .resolve(new SimpleDateFormat("yyyy").format(now))
                .resolve(new SimpleDateFormat("MM").format(now));

        if (!directory.toFile().exists()) {
            directory.toFile().mkdirs();
        }

        Path filePath = directory.resolve(fileSaveName);
        String streAllCours = filePath.toString();

        file.transferTo(filePath.toFile());

        AtchmnflVO atchmnflVO = new AtchmnflVO();
        atchmnflVO.setAtchmnflId(atchfileId);
        atchmnflVO.setStreAllCours(streAllCours);
        atchmnflVO.setOrginlFileNm(file.getOriginalFilename());
        atchmnflVO.setFileSize(file.getSize());

        create(atchmnflVO);

        return atchmnflVO;
    }

    public void create(AtchmnflVO vo) throws Exception {
        atchmnflMapper.create(vo);
    }

    public int delete(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.delete(vo);
    }

    public AtchmnflVO retrieve(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.retrieve(vo);
    }

    public List<AtchmnflVO> retrieveList(AtchmnflVO vo) throws Exception {
        return atchmnflMapper.retrieveList(vo);
    }
}
