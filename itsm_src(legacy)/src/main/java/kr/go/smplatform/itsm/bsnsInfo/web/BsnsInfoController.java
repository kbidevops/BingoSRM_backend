package kr.go.smplatform.itsm.bsnsInfo.web;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.bsnsInfo.service.BsnsInfoService;
import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BsnsInfoController {

    @Resource(name="bsnsInfoService")
    private BsnsInfoService bsnsInfoService;

    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;

    @RequestMapping("/itsm/bsnsInfo/mngr/list.do")
    public String list() {
        return "/itsm/bsnsInfo/mngr/list";
    }

    @RequestMapping(value = "/itsm/bsnsInfo/mngr/getChargersAjax.do", method = RequestMethod.GET)
    public String getUsersAjax(Model model, String userTyCodes) {
        List<ChargerUserInfoVO> list = Arrays
                .stream(userTyCodes.split(","))
                .map(userTyCode -> bsnsInfoService.selectChargerList(userTyCode))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        model.addAttribute("list", list);

        return "jsonView";
    }

    @RequestMapping(value = "/itsm/bsnsInfo/mngr/getInfoAjax.do", method = RequestMethod.GET)
    public String getInfoAjax(Model model) {
        model.addAttribute("data", bsnsInfoService.selectBsnsInfo());

        return "jsonView";
    }

    @RequestMapping(value = "/itsm/bsnsInfo/mngr/testAjax.do", method = RequestMethod.GET)
    public String testAjax(HttpServletRequest request) {

        return "jsonView";
    }

    @RequestMapping(value = "/itsm/bsnsInfo/mngr/setInfoAjax.do", method = RequestMethod.POST)
    public String setInfoAjax(Model model, BsnsInfoVO bsnsInfoVO, @RequestParam(value = "logoA", required = false) MultipartFile logoA, @RequestParam(value = "logoB", required = false) MultipartFile logoB) throws Exception {
        final BsnsInfoVO originalInfo = bsnsInfoService.selectBsnsInfo();

        if (logoA.isEmpty()) {
            bsnsInfoVO.setInfoLogoA(originalInfo.getInfoLogoA());
        }
        else {
            final AtchmnflVO atchmnflVO = saveFile(logoA);
            bsnsInfoVO.setInfoLogoA(atchmnflVO.getAtchmnflId());
        }

        if (logoB.isEmpty()) {
            bsnsInfoVO.setInfoLogoB(originalInfo.getInfoLogoB());
        }
        else {
            final AtchmnflVO atchmnflVO = saveFile(logoB);
            bsnsInfoVO.setInfoLogoB(atchmnflVO.getAtchmnflId());
        }

        final int result = bsnsInfoService.updateBsnsInfo(bsnsInfoVO);

        model.addAttribute("data", bsnsInfoVO);
        model.addAttribute("result", result);

        return "jsonView";
    }

    private AtchmnflVO saveFile(MultipartFile file) throws Exception {
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

        atchmnflVO.setAtchmnflId(UUID.randomUUID().toString());
        atchmnflVO.setStreAllCours(streAllCours);
        atchmnflVO.setOrginlFileNm(file.getOriginalFilename());
        atchmnflVO.setFileSize(file.getSize());

        atchmnflService.create(atchmnflVO);

        return atchmnflVO;
    }
}
