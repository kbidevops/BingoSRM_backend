package kr.go.smplatform.itsm.assets.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.assets.service.AssetsService;
import kr.go.smplatform.itsm.assets.vo.AssetsFormVO;
import kr.go.smplatform.itsm.assets.vo.AssetsVO;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.excel.ExcelView;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class AssetsController extends BaseController {
    
    @Resource(name = "assetsService")
    private AssetsService assetsService;
    
    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;

    /**
     * 자산관리 목록 화면 조회
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/retrievePagingList.do")
    public String retrievePagingList(AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        assetsFormVO.getSearchAssetsVO().setDeleteYn("N");
        
        if (assetsFormVO.getSearchAssetsVO().getAssetsSe1() == null) {
            assetsFormVO.getSearchAssetsVO().setAssetsSe1(AssetsVO.ASSETS_SE_HW);
        }
        
        /** EgovPropertyService.sample */
        assetsFormVO.getSearchAssetsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        assetsFormVO.getSearchAssetsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** paging setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(assetsFormVO.getSearchAssetsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(assetsFormVO.getSearchAssetsVO().getRecordCountPerPage());
        paginationInfo.setPageSize(assetsFormVO.getSearchAssetsVO().getPageSize());
        paginationInfo.setTotalRecordCount(
                assetsService.retrieveListCnt(assetsFormVO.getSearchAssetsVO())
        );
        
        assetsFormVO.getSearchAssetsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        assetsFormVO.getSearchAssetsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        assetsFormVO.getSearchAssetsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
        final List<AssetsVO> list = assetsService.retrievePagingList(assetsFormVO.getSearchAssetsVO());
        
        final Map<String, FuncImprvmVO> fnctImprvmMap = new HashMap();

        retrieveCmmnCode(model);
        
        model.addAttribute("resultList", list);
        model.addAttribute("resultMap", fnctImprvmMap);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/assets/mngr/list";
    }

    @RequestMapping(value = "/api/assets", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> retrievePagingListApi(AssetsFormVO assetsFormVO) throws Exception {
        assetsFormVO.getSearchAssetsVO().setDeleteYn("N");

        if (assetsFormVO.getSearchAssetsVO().getAssetsSe1() == null) {
            assetsFormVO.getSearchAssetsVO().setAssetsSe1(AssetsVO.ASSETS_SE_HW);
        }

        assetsFormVO.getSearchAssetsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        assetsFormVO.getSearchAssetsVO().setPageSize(propertiesService.getInt("pageSize"));

        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(assetsFormVO.getSearchAssetsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(assetsFormVO.getSearchAssetsVO().getRecordCountPerPage());
        paginationInfo.setPageSize(assetsFormVO.getSearchAssetsVO().getPageSize());
        paginationInfo.setTotalRecordCount(
                assetsService.retrieveListCnt(assetsFormVO.getSearchAssetsVO())
        );

        assetsFormVO.getSearchAssetsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        assetsFormVO.getSearchAssetsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        assetsFormVO.getSearchAssetsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        final List<AssetsVO> list = assetsService.retrievePagingList(assetsFormVO.getSearchAssetsVO());

        final Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("paginationInfo", paginationInfo);
        return response;
    }

    @RequestMapping(value = "/itsm/assets/mngr/retrievePagingListAjax.do")
    public String retrievePagingListAjax(AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        assetsFormVO.getSearchAssetsVO().setDeleteYn("N");

        if (assetsFormVO.getSearchAssetsVO().getAssetsSe1() == null) {
            assetsFormVO.getSearchAssetsVO().setAssetsSe1(AssetsVO.ASSETS_SE_HW);
        }

        assetsFormVO.getSearchAssetsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        assetsFormVO.getSearchAssetsVO().setPageSize(propertiesService.getInt("pageSize"));

        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(assetsFormVO.getSearchAssetsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(assetsFormVO.getSearchAssetsVO().getRecordCountPerPage());
        paginationInfo.setPageSize(assetsFormVO.getSearchAssetsVO().getPageSize());
        paginationInfo.setTotalRecordCount(
                assetsService.retrieveListCnt(assetsFormVO.getSearchAssetsVO())
        );

        assetsFormVO.getSearchAssetsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        assetsFormVO.getSearchAssetsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        assetsFormVO.getSearchAssetsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        final List<AssetsVO> list = assetsService.retrievePagingList(assetsFormVO.getSearchAssetsVO());

        model.addAttribute("resultList", list);
        model.addAttribute("paginationInfo", paginationInfo);

        return "jsonView";
    }

    private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("assetsSe1List", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.ASSET_SE_CODE)
        ));
        
        model.addAttribute("locateList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.LOCATE_CODE)
        ));
        
        model.addAttribute("hwList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.ASSET_HW_CODE)
        ));
        
        model.addAttribute("swList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.ASSET_SW_CODE)
        ));
    } 
    
    /**
     * 자산관리 작성 화면 조회
     * @param session
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/createView.do")
    public String createView(HttpSession session, AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        retrieveCmmnCode(model);

        assetsFormVO.getAssetsVO().setAssetsSn(0);
        assetsFormVO.getAssetsVO().setLicenseAtchmnflId(UUID.randomUUID().toString());
        assetsFormVO.getAssetsVO().setManualAtchmnflId(UUID.randomUUID().toString());
        
        //중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        assetsFormVO.getAssetsVO().setSaveToken(saveToken);
        
        return "/itsm/assets/mngr/edit";
    }

    /**
     * 자산관리 작성
     * @param session
     * @param assetsFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/create.do")
    public String create(HttpSession session, AssetsFormVO assetsFormVO) throws Exception {
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, assetsFormVO.getAssetsVO().getSaveToken())) {
            return "forward:/itsm/assets/mngr/retrievePagingList.do";
        }
        
        assetsFormVO.getAssetsVO().makeIndcDtDisplay();
        assetsService.create(assetsFormVO.getAssetsVO());
        
        return "forward:/itsm/assets/mngr/retrievePagingList.do";
    }
    
    /**
     * 자산관리 수정화면
     * @param session
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/updateView.do")
    public String updateView(HttpSession session, AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        retrieveCmmnCode(model);
        
        final AssetsVO retrieveVO = assetsService.retrieve(assetsFormVO.getAssetsVO());
        assetsFormVO.setAssetsVO(retrieveVO);
        
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
        if (GenericValidator.isBlankOrNull(assetsFormVO.getAssetsVO().getLicenseAtchmnflId())) {
            assetsFormVO.getAssetsVO().setLicenseAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(assetsFormVO.getAssetsVO().getLicenseAtchmnflId());
            final List<AtchmnflVO> licenseAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("licenseAtchmnflList", licenseAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(assetsFormVO.getAssetsVO().getManualAtchmnflId())) {
            assetsFormVO.getAssetsVO().setManualAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(assetsFormVO.getAssetsVO().getManualAtchmnflId());
            final List<AtchmnflVO> manualAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("manualAtchmnflList", manualAtchmnflList);
        }
        
        //중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        assetsFormVO.getAssetsVO().setSaveToken(saveToken);
        
        return "/itsm/assets/mngr/edit";
    }
    
    /**
     * 자산관리 수정
     * @param session
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/update.do")
    public String update(HttpSession session, AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        //중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, assetsFormVO.getAssetsVO().getSaveToken())) {
            return "forward:/itsm/assets/mngr/retrievePagingList.do";
        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        assetsFormVO.getAssetsVO().setUpdtId(loginUserVO.getUserId());
        assetsFormVO.getAssetsVO().makeIndcDtDisplay();
        
        assetsService.update(assetsFormVO.getAssetsVO());
        
        retrieveCmmnCode(model);
        
        return "redirect:/itsm/assets/mngr/retrievePagingList.do";
    }
    
    /**
     * 자산관리 삭제
     * @param session
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/assets/mngr/delete.do")
    public String delete(HttpSession session, AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        assetsFormVO.getAssetsVO().setUpdtId(loginUserVO.getUserId());
        
        assetsService.delete(assetsFormVO.getAssetsVO());
        
        retrieveCmmnCode(model);
        
        return "forward:/itsm/assets/mngr/retrievePagingList.do";
    }
    
    /**
     * 엑셀 다운로드
     * @param assetsFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/itsm/assets/mngr/retrieveExcelList.do")
    public String retrieveExcelList(AssetsFormVO assetsFormVO, ModelMap model) throws Exception {
        assetsFormVO.getAssetsVO().setAssetsSe1(AssetsVO.ASSETS_SE_HW);

        final List<AssetsVO> list_HW = assetsService.retrieveAllList(assetsFormVO.getAssetsVO());
        
        assetsFormVO.getAssetsVO().setAssetsSe1(AssetsVO.ASSETS_SE_SW);
        final List<AssetsVO> list_SW = assetsService.retrieveAllList(assetsFormVO.getAssetsVO());
        
        model.addAttribute("templateName", ExcelView.TEMPLATE_ASSET);
        model.addAttribute("excelName", ExcelView.EXCEL_NAME_ASSET);
        model.addAttribute("list_HW", list_HW);
        model.addAttribute("list_SW", list_SW);

        return "excelView";
     }
}
