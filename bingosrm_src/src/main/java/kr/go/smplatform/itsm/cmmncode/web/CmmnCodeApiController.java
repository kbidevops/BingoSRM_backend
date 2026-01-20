package kr.go.smplatform.itsm.cmmncode.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeService;
import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeTyService;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeTyVO;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;

@RestController
@RequestMapping("/api/v1")
public class CmmnCodeApiController {
    private final CmmnCodeService cmmnCodeService;
    private final CmmnCodeTyService cmmnCodeTyService;

    public CmmnCodeApiController(CmmnCodeService cmmnCodeService, CmmnCodeTyService cmmnCodeTyService) {
        this.cmmnCodeService = cmmnCodeService;
        this.cmmnCodeTyService = cmmnCodeTyService;
    }

    @GetMapping("/code-types")
    public List<CmmnCodeTyVO> listCodeTypes(@RequestParam(value = "deleteYn", required = false) String deleteYn)
            throws Exception {
        CmmnCodeTyVO vo = new CmmnCodeTyVO();
        vo.setDeleteYn(deleteYn);
        return cmmnCodeTyService.retrieveList(vo);
    }

    @GetMapping("/code-types/{codeType}/codes")
    public List<CmmnCodeVO> listCodesByType(
            @PathVariable("codeType") String codeType,
            @RequestParam(value = "deleteYn", required = false) String deleteYn) throws Exception {
        CmmnCodeVO vo = new CmmnCodeVO();
        vo.setCmmnCodeTy(codeType);
        vo.setDeleteYn(deleteYn);
        return cmmnCodeService.retrieveList(vo);
    }
}
