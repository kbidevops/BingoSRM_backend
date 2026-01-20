package kr.go.smplatform.itsm.funcimprvm.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.excel.ExcelView;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.service.FuncImprvmService;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmFormVO;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.report.web.RepMasterController;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FuncImprvmController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepMasterController.class);

    @Resource(name = "funcImprvmService")
    private FuncImprvmService funcImprvmService;

    @Resource(name = "sysChargerService")
    private SysChargerService sysChargerService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "srvcRsponsService")
    private SrvcRsponsService srvcRsponsService;

    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;

    /**
     * 기능개선 목록 조회
     * @param funcImprvmFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/funcimprvm/mngr/retrievePagingList.do")
    public String retrievePagingList(FuncImprvmFormVO funcImprvmFormVO, ModelMap model) throws Exception {
        funcImprvmFormVO.getSearchFuncImprvmVO().setDeleteYn("N");
        funcImprvmFormVO.getSearchFuncImprvmVO().setProgrmUpdtYn("Y");

        /** EgovPropertyService.sample */
        funcImprvmFormVO.getSearchFuncImprvmVO().setPageUnit(propertiesService.getInt("pageUnit"));
        funcImprvmFormVO.getSearchFuncImprvmVO().setPageSize(propertiesService.getInt("pageSize"));

        /** paging setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(funcImprvmFormVO.getSearchFuncImprvmVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(funcImprvmFormVO.getSearchFuncImprvmVO().getRecordCountPerPage());
        paginationInfo.setPageSize(funcImprvmFormVO.getSearchFuncImprvmVO().getPageSize());
        paginationInfo.setTotalRecordCount(
                srvcRsponsService.retrievePagingListCnt(funcImprvmFormVO.getSearchFuncImprvmVO())
        );

        funcImprvmFormVO.getSearchFuncImprvmVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        funcImprvmFormVO.getSearchFuncImprvmVO().setLastIndex(paginationInfo.getLastRecordIndex());
        funcImprvmFormVO.getSearchFuncImprvmVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        final List<SrvcRsponsVO> list = srvcRsponsService.retrievefnctImprvmPagingList(funcImprvmFormVO.getSearchFuncImprvmVO());

        // List<FuncImprvmVO> list = funcImprvmService.retrievePagingList(funcImprvmFormVO.getSearchFuncImprvmVO());

        final Map<String, FuncImprvmVO> fnctImprvmMap = new HashMap<String, FuncImprvmVO>();

        final FuncImprvmVO retrieveVO = new FuncImprvmVO();
        for (SrvcRsponsVO vo : list) {
            if (vo.getFnctImprvmNo() == null) {
                continue;
            }

            retrieveVO.setFnctImprvmNo(vo.getFnctImprvmNo());
            fnctImprvmMap.put(vo.getSrvcRsponsNo(), funcImprvmService.retrieve(retrieveVO));
        }

//		List<SysChargerVO> sysChargerList = sysChargerService.retrieveAssignList(vo)

        // 담당자 목록 조회
        final UserVO userVo = new UserVO();
        userVo.setUserTyCode(UserVO.USER_TY_CODE_CHARGER);

        final List<UserVO> sysChargerList = userService.retrieveList(userVo);

        retrieveCmmnCode(model);

        funcImprvmFormVO.setProcessMt(
                new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date())
        );

        model.addAttribute("sysChargerList", sysChargerList);
        model.addAttribute("resultList", list);
        model.addAttribute("resultMap", fnctImprvmMap);
        model.addAttribute("paginationInfo", paginationInfo);

        return "/itsm/funcimprvm/mngr/list";
    }

    /**
     * 기능개선 엑셀 다운로드
     * @param funcImprvmVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/itsm/funcimprvm/mngr/retrieveExcelList.do")
    public String retrieveExcelList(FuncImprvmVO funcImprvmVO, ModelMap model) throws Exception {
        LOGGER.info("ProcessMt: " + funcImprvmVO.getProcessMt());

        final List<FuncImprvmVO> list= funcImprvmService.retrieveList(funcImprvmVO);

        model.addAttribute("templateName", ExcelView.TEMPLATE_FI);
        model.addAttribute("excelName", ExcelView.EXCEL_NAME_FI);
        model.addAttribute("funcImprvmVO", list);
        return "excelView";
    }

    private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("trgetSrvcCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.TRGET_SRVC_CODE)
        ));

        model.addAttribute("processStdrCodeList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.PROCESS_STDR_CODE)
        ));

        model.addAttribute("fiClList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.FI_CL_CODE)
        ));
    }

    /**
     * 기능개선 작성 화면 조회
     * @param funcImprvmFormVO
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/createView.do")
    public String createView(FuncImprvmFormVO funcImprvmFormVO, HttpSession session, ModelMap model) throws Exception {
        final Date currentDate = new Date();

        final SrvcRsponsVO searchVO = new SrvcRsponsVO();
        searchVO.setSrvcRsponsNo(funcImprvmFormVO.getFuncImprvmVO().getSrvcRsponsNo());

        final SrvcRsponsVO retrieveVO = srvcRsponsService.retrieve(searchVO);

        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        funcImprvmFormVO.getFuncImprvmVO().setSaveToken(saveToken);

        funcImprvmFormVO.getFuncImprvmVO().setFiAtchmnflId(UUID.randomUUID().toString());
        funcImprvmFormVO.getFuncImprvmVO().setAsisAtchmnflId(UUID.randomUUID().toString());
        funcImprvmFormVO.getFuncImprvmVO().setTobeAtchmnflId(UUID.randomUUID().toString());

        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        funcImprvmFormVO.getFuncImprvmVO().setChargerId(loginUserVO.getUserId());
        funcImprvmFormVO.getFuncImprvmVO().setChargerUserNm(loginUserVO.getUserNm());
        funcImprvmFormVO.getFuncImprvmVO().setApplyPlanDt(currentDate);
        funcImprvmFormVO.getFuncImprvmVO().setApplyRDt(currentDate);

        retrieveCmmnCode(model);

        model.addAttribute("srvcRsponsVO", retrieveVO);

        // 확인 담당자
        final UserVO userVO = new UserVO();
        userVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
        userVO.setUserTyCode(userVO.USER_TY_CODE_CSTMR);
        model.addAttribute("cstmrList", userService.retrieveList(userVO));

        return "/itsm/funcimprvm/mngr/edit";
    }

    /**
     * 기능개선 작성
     * @param funcImprvmFormVO
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/create.do")
    public String create(FuncImprvmFormVO funcImprvmFormVO, HttpSession session) throws Exception {
        //중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, funcImprvmFormVO.getFuncImprvmVO().getSaveToken())) {
            return "forward:/itsm/funcimprvm/mngr/retrievePagingList.do";
        }

        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        funcImprvmFormVO.getFuncImprvmVO().setCreatId(loginUserVO.getUserId());

        funcImprvmFormVO.getFuncImprvmVO().makeApplyPlanDt();
        funcImprvmFormVO.getFuncImprvmVO().makeApplyRDt();

        funcImprvmService.create(funcImprvmFormVO.getFuncImprvmVO());
        LOGGER.debug("Inserted_VO_Info : {}", funcImprvmFormVO.getFuncImprvmVO());

        final FuncImprvmVO funcImprvmVO = new FuncImprvmVO();
        funcImprvmVO.setSrvcRsponsNo(funcImprvmFormVO.getFuncImprvmVO().getSrvcRsponsNo());

        final SrvcRsponsVO retrieveSRVO = srvcRsponsService.retrieve(funcImprvmVO);
        retrieveSRVO.setFnctImprvmNo(funcImprvmFormVO.getFuncImprvmVO().getFnctImprvmNo());

        srvcRsponsService.update(retrieveSRVO);

        return "forward:/itsm/funcimprvm/mngr/retrievePagingList.do";
    }

    /**
     * 기능개선 수정 화면 조회
     * @param funcImprvmFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/updateView.do")
    public String updateView(FuncImprvmFormVO funcImprvmFormVO, ModelMap model) throws Exception {
        final FuncImprvmVO retrieveVO = funcImprvmService.retrieve(funcImprvmFormVO.getFuncImprvmVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();

        if (GenericValidator.isBlankOrNull(retrieveVO.getFiAtchmnflId())) {
            retrieveVO.setFiAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(retrieveVO.getFiAtchmnflId());
            final List<AtchmnflVO> fiAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("fiAtchmnflList", fiAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(retrieveVO.getAsisAtchmnflId())) {
            retrieveVO.setAsisAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(retrieveVO.getAsisAtchmnflId());
            final List<AtchmnflVO> asisAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("asisAtchmnflList", asisAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(retrieveVO.getTobeAtchmnflId())) {
            retrieveVO.setTobeAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(retrieveVO.getTobeAtchmnflId());
            final List<AtchmnflVO> tobeAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("tobeAtchmnflList", tobeAtchmnflList);
        }

        funcImprvmFormVO.setFuncImprvmVO(retrieveVO);

        retrieveCmmnCode(model);

        // 확인 담당자
        final UserVO userVO = new UserVO();
        userVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
        userVO.setUserTyCode(userVO.USER_TY_CODE_CSTMR);
        model.addAttribute("cstmrList", userService.retrieveList(userVO));

        return "/itsm/funcimprvm/mngr/edit";
    }

    /**
     * 기능개선 수정
     * @param funcImprvmFormVO
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/update.do")
    public String update(FuncImprvmFormVO funcImprvmFormVO, HttpSession session) throws Exception {
        funcImprvmFormVO.getFuncImprvmVO().makeApplyPlanDt();
        funcImprvmFormVO.getFuncImprvmVO().makeApplyRDt();

        final String saveToken = ITSMDefine.generateSaveToken(session);
        funcImprvmFormVO.getFuncImprvmVO().setSaveToken(saveToken);

        final UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        funcImprvmFormVO.getFuncImprvmVO().setUpdtId(loginUserVO.getUserId());

        funcImprvmService.update(funcImprvmFormVO.getFuncImprvmVO());

        return "forward:/itsm/funcimprvm/mngr/retrievePagingList.do";
    }

    /**
     * 기능개선 삭제
     * @param funcImprvmFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/delete.do")
    public String delete(FuncImprvmFormVO funcImprvmFormVO) throws Exception {
        final FuncImprvmVO funcImprvmVO = new FuncImprvmVO();
        funcImprvmVO.setFnctImprvmNo(funcImprvmFormVO.getFuncImprvmVO().getFnctImprvmNo());

        final SrvcRsponsVO retrieveSRVO = srvcRsponsService.retrieve(funcImprvmVO);
        retrieveSRVO.setFnctImprvmNo(null);

        srvcRsponsService.update(retrieveSRVO);
        funcImprvmService.delete(funcImprvmFormVO.getFuncImprvmVO());

        return "forward:/itsm/funcimprvm/mngr/retrievePagingList.do";
    }

//    /**
//     * SR번호 리스트 조회
//     * @param session
//     * @param srvcRsponsVO
//     * @param model
//     * @return
//     * @throws Exception
//     */
    /*@RequestMapping("/itsm/funcimprvm/mngr/retrieveSrNoListAjax.do")
    public String retrieveSrNoListAjax(
    		HttpSession session
    		, SrvcRsponsVO srvcRsponsVO
    		, ModelMap model)
    throws Exception {
    	UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

    	if(!GenericValidator.isBlankOrNull(srvcRsponsVO.getSrvcRsponsNo())){
	    	LOGGER.debug("srvcRsponsVO.getSrvcRsponsNo(): "+srvcRsponsVO.getSrvcRsponsNo());
	    	try{
	    		srvcRsponsVO.setChargerId(loginUserVO.getUserId());
	    		srvcRsponsVO.setProgrmUpdtYn("Y");
		        List<SrvcRsponsVO> resultList = srvcRsponsService.retrieveSrvcRsponsNoList(srvcRsponsVO);
		        model.addAttribute("returnMessage", "success");
		        model.addAttribute("resultList", resultList);
	    	}catch(Exception e){
	    		model.addAttribute("returnMessage", "fail");
	    	}
    	}else{
    		model.addAttribute("returnMessage", "fail");
    	}

    	return "jsonView";
    }*/

    /**
     * 기능개선 유무 확인
     * @param funcImprvmVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/funcimprvm/mngr/retrieveFnctImprvmAjax.do")
    public String retrieveFnctImprvmAjax(FuncImprvmVO funcImprvmVO, ModelMap model) throws Exception {
        if (funcImprvmVO.getProcessMt() != null) {
            if (funcImprvmService.retrieveList(funcImprvmVO).size() > 0) {
                model.addAttribute("funcImprvmList", funcImprvmService.retrieveList(funcImprvmVO));
            }
            else {
                model.addAttribute("returnMessage", "empty");
            }

            return "jsonView";
        }
        else if (funcImprvmService.retrieve(funcImprvmVO) == null) {
            model.addAttribute("returnMessage", "empty");
        }

        return "jsonView";
    }
}
