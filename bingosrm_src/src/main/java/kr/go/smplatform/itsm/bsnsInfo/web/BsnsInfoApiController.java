package kr.go.smplatform.itsm.bsnsInfo.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.bsnsInfo.service.BsnsInfoService;
import kr.go.smplatform.itsm.bsnsInfo.vo.BsnsInfoVO;
import kr.go.smplatform.itsm.bsnsInfo.vo.ChargerUserInfoVO;

@RestController
@RequestMapping("/api/v1/bsns-info")
public class BsnsInfoApiController {
    private final BsnsInfoService bsnsInfoService;
    private final AtchmnflService atchmnflService;

    public BsnsInfoApiController(BsnsInfoService bsnsInfoService, AtchmnflService atchmnflService) {
        this.bsnsInfoService = bsnsInfoService;
        this.atchmnflService = atchmnflService;
    }

    @GetMapping
    public BsnsInfoVO getInfo() {
        return bsnsInfoService.selectBsnsInfo();
    }

    @GetMapping("/chargers")
    public List<ChargerUserInfoVO> getChargers(@RequestParam("userTyCodes") String userTyCodes) {
        if (userTyCodes == null || userTyCodes.trim().isEmpty()) {
            return new ArrayList<ChargerUserInfoVO>();
        }
        return List.of(userTyCodes.split(","))
                .stream()
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .map(bsnsInfoService::selectChargerList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> updateInfo(
            BsnsInfoVO bsnsInfoVO,
            @RequestParam(value = "logoA", required = false) MultipartFile logoA,
            @RequestParam(value = "logoB", required = false) MultipartFile logoB) throws Exception {
        BsnsInfoVO originalInfo = bsnsInfoService.selectBsnsInfo();

        if (logoA == null || logoA.isEmpty()) {
            bsnsInfoVO.setInfoLogoA(originalInfo.getInfoLogoA());
        } else {
            AtchmnflVO atchmnflVO = atchmnflService.saveFile(logoA);
            bsnsInfoVO.setInfoLogoA(atchmnflVO.getAtchmnflId());
        }

        if (logoB == null || logoB.isEmpty()) {
            bsnsInfoVO.setInfoLogoB(originalInfo.getInfoLogoB());
        } else {
            AtchmnflVO atchmnflVO = atchmnflService.saveFile(logoB);
            bsnsInfoVO.setInfoLogoB(atchmnflVO.getAtchmnflId());
        }

        int result = bsnsInfoService.updateBsnsInfo(bsnsInfoVO);
        return Map.of("result", result, "data", bsnsInfoVO);
    }
}
