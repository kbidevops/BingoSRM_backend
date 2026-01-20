package kr.go.smplatform.itsm.wdtb.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.excel.ExcelView;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.service.FuncImprvmService;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.user.web.UserMngrController;
import kr.go.smplatform.itsm.wdtb.service.WdtbService;
import kr.go.smplatform.itsm.wdtb.vo.WdtbFormVO;
import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class WdtbController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);
	
	@Resource(name = "wdtbService")
	private WdtbService wdtbService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "sysChargerService")
	private SysChargerService sysChargerService;
	
	@Resource(name = "srvcRsponsService")
	private SrvcRsponsService srvcRsponsService;
	
	@Resource(name = "funcImprvmService")
	private FuncImprvmService funcImprvmService;
	
	@Resource(name = "atchmnflService")
	private AtchmnflService atchmnflService;
	
	@RequestMapping(value = "/itsm/wdtb/mngr/retrievePagingList.do")
	public String retrievePagingList(WdtbFormVO wdtbFormVO, ModelMap model) throws Exception {
		/** EgovPropertyService.sample */
		wdtbFormVO.getSearchWdtbVO().setPageUnit(propertiesService.getInt("pageUnit"));
		wdtbFormVO.getSearchWdtbVO().setPageSize(propertiesService.getInt("pageSize"));
		
		/** paging setting */
		final PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(wdtbFormVO.getSearchWdtbVO().getPageIndex());
		paginationInfo.setRecordCountPerPage(wdtbFormVO.getSearchWdtbVO().getRecordCountPerPage());
		paginationInfo.setPageSize(wdtbFormVO.getSearchWdtbVO().getPageSize());
			
		wdtbFormVO.getSearchWdtbVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
		wdtbFormVO.getSearchWdtbVO().setLastIndex(paginationInfo.getLastRecordIndex());
		wdtbFormVO.getSearchWdtbVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		wdtbFormVO.getSearchWdtbVO().setDeleteYn("N");
		
		final int totalCount = srvcRsponsService.retrievePagingListCnt(wdtbFormVO.getSearchWdtbVO());
		paginationInfo.setTotalRecordCount(totalCount);
		
		if (wdtbFormVO.getSearchWdtbVO().getWdtbDtDateDisplay() != null) {
			if (!wdtbFormVO.getSearchWdtbVO().getWdtbDtDateDisplay().isEmpty()) {
				wdtbFormVO.getSearchWdtbVO().makeWdtbDt();
			}
		}
		
		final List<SrvcRsponsVO> list = srvcRsponsService.retrieveWdtbPagingList(wdtbFormVO.getSearchWdtbVO());
		
		// 담당자 목록 조회
		final UserVO userVo = new UserVO();
		userVo.setUserTyCode(UserVO.USER_TY_CODE_CHARGER);

		final List<UserVO> sysChargerList = userService.retrieveList(userVo);
		
		retrieveCmmnCode(model);
		
		wdtbFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
		
		LOGGER.debug("processMt : {}", wdtbFormVO.getProcessMt());

		model.addAttribute("sysChargerList", sysChargerList);
		model.addAttribute("resultList", list);
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "/itsm/wdtb/mngr/list";
	}
	
	private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("trgetSrvcCodeVOList", cmmnCodeService.retrieveList(
				new CmmnCodeVO(CmmnCodeVO.TRGET_SRVC_CODE)
		));
        
        model.addAttribute("processStdrCodeList", cmmnCodeService.retrieveList(
				new CmmnCodeVO(CmmnCodeVO.PROCESS_STDR_CODE)
		));
        
        model.addAttribute("wdtbSeList", cmmnCodeService.retrieveList(
				new CmmnCodeVO(CmmnCodeVO.WDTB_SE_CODE)
		));
	}
	
	/**
	 * 배포확인 작성 화면 조회
	 * @param session
	 * @param wdtbFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/wdtb/mngr/createView.do")
	public String createView(HttpSession session, WdtbFormVO wdtbFormVO, ModelMap model) throws Exception {
		retrieveCmmnCode(model);

		final Date currentDate = new Date();
		wdtbFormVO.getWdtbVO().setWdtbDt(currentDate);
		wdtbFormVO.getWdtbVO().setWdtbDtOne(currentDate);
		wdtbFormVO.getWdtbVO().setWdtbDtTwo(currentDate);
		
		// 파일 ID 생성
		wdtbFormVO.getWdtbVO().setSolutConectflId(UUID.randomUUID().toString());
		wdtbFormVO.getWdtbVO().setOpertResultflId(UUID.randomUUID().toString());
		wdtbFormVO.getWdtbVO().setLoginResultflId(UUID.randomUUID().toString());
		wdtbFormVO.getWdtbVO().setServerOneLogflId(UUID.randomUUID().toString());
		wdtbFormVO.getWdtbVO().setServerTwoLogflId(UUID.randomUUID().toString());
		
		SrvcRsponsVO retrieveSrvcRsponsVO = new SrvcRsponsVO();

		for (String srvcNo : wdtbFormVO.getSrvcRsponsNos()) {
			SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
			srvcRsponsVO.setSrvcRsponsNo(srvcNo);
			
			if (retrieveSrvcRsponsVO.getSrvcRsponsSj() == null) {
				retrieveSrvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsVO);
				wdtbFormVO.setSrvcRsponsVO(retrieveSrvcRsponsVO);
			}
			else {
				retrieveSrvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsVO);
				wdtbFormVO.getSrvcRsponsVO().setSrvcRsponsSj(
						wdtbFormVO.getSrvcRsponsVO().getSrvcRsponsSj() + ", "+ retrieveSrvcRsponsVO.getSrvcRsponsSj()
				);
			}
		}
		
		if (!retrieveSrvcRsponsVO.getFnctImprvmNo().isEmpty()) {
			final FuncImprvmVO funcImprvmVO = new FuncImprvmVO();
			funcImprvmVO.setFnctImprvmNo(retrieveSrvcRsponsVO.getFnctImprvmNo());

			wdtbFormVO.setFuncImprvmVO(funcImprvmService.retrieve(funcImprvmVO));
		}
		
		LOGGER.debug("srvcList : {} ", wdtbFormVO.getSrvcRsponsNos());
		
		// 중복등록 방지 처리
    	final String saveToken = ITSMDefine.generateSaveToken(session);
    	wdtbFormVO.getWdtbVO().setSaveToken(saveToken);

		// 확인 담당자
		final UserVO userVO = new UserVO();
		userVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
		userVO.setUserTyCode(userVO.USER_TY_CODE_CSTMR);
		model.addAttribute("cstmrList", userService.retrieveList(userVO));
		
		return "/itsm/wdtb/mngr/edit";
	}
	
	/**
	 * 배포확인서 작성
	 * @param session
	 * @param wdtbFormVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/wdtb/mngr/create.do")
	public String create(HttpSession session, WdtbFormVO wdtbFormVO) throws Exception {
		// 중복등록 방지 처리
    	if (!ITSMDefine.checkSaveToken(session, wdtbFormVO.getWdtbVO().getSaveToken())) {
    		return "forward:/itsm/wdtb/mngr/retrievePagingList.do";
    	}
		
		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
		wdtbFormVO.getWdtbVO().setCreatId(loginUserVO.getUserId());
		wdtbFormVO.getWdtbVO().makeWdtbDt();
		wdtbFormVO.getWdtbVO().makeWdtbDtOne();
		wdtbFormVO.getWdtbVO().makeWdtbDtTwo();
		
		wdtbService.create(wdtbFormVO.getWdtbVO());
		
		for (String srvcRsponsNo : wdtbFormVO.getSrvcRsponsNos()) {
			final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
			srvcRsponsVO.setSrvcRsponsNo(srvcRsponsNo);
			srvcRsponsVO.setWdtbCnfirmNo(wdtbFormVO.getWdtbVO().getWdtbCnfirmNo());
			srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
			
			srvcRsponsService.updateWdtbCnfirm(srvcRsponsVO);
		}
		
		return "forward:/itsm/wdtb/mngr/retrievePagingList.do";
	}
	
	/**
	 * 배포확인서 수정 화면 조회
	 * @param wdtbFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/wdtb/mngr/updateView.do")
	public String updateView(WdtbFormVO wdtbFormVO, ModelMap model) throws Exception {
		retrieveCmmnCode(model);
		
		wdtbFormVO.setSrvcRsponsVO(srvcRsponsService.retrieve(wdtbFormVO.getSrvcRsponsVO()));
		wdtbFormVO.getWdtbVO().setWdtbCnfirmNo(wdtbFormVO.getSrvcRsponsVO().getWdtbCnfirmNo());
		wdtbFormVO.setWdtbVO(wdtbService.retrieve(wdtbFormVO.getWdtbVO()));
		wdtbFormVO.getFuncImprvmVO().setFnctImprvmNo(wdtbFormVO.getSrvcRsponsVO().getFnctImprvmNo());
		wdtbFormVO.setFuncImprvmVO(funcImprvmService.retrieve(wdtbFormVO.getFuncImprvmVO()));
		
		if (wdtbFormVO.getWdtbVO() != null) {
			final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
			srvcRsponsVO.setWdtbCnfirmNo(wdtbFormVO.getWdtbVO().getWdtbCnfirmNo());

			final List<SrvcRsponsVO> srvcRsponsVOlist = srvcRsponsService.retrieveAllList(srvcRsponsVO);
			
			for (SrvcRsponsVO vo : srvcRsponsVOlist) {
				wdtbFormVO.getSrvcRsponsNos().add(vo.getSrvcRsponsNo());

				if (!wdtbFormVO.getSrvcRsponsVO().getSrvcRsponsSj().equals(vo.getSrvcRsponsSj())) {
					wdtbFormVO.getSrvcRsponsVO().setSrvcRsponsSj(
							wdtbFormVO.getSrvcRsponsVO().getSrvcRsponsSj() + ", "+ vo.getSrvcRsponsSj()
					);
				}
			}
		}
		else {
			wdtbFormVO.getSrvcRsponsNos().add(wdtbFormVO.getSrvcRsponsVO().getSrvcRsponsNo());
		}
		
		final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
    	if (GenericValidator.isBlankOrNull(wdtbFormVO.getWdtbVO().getSolutConectflId())) {
    		wdtbFormVO.getWdtbVO().setSolutConectflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(wdtbFormVO.getWdtbVO().getSolutConectflId());

    		final List<AtchmnflVO> solutConectflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("solutConectflList", solutConectflList);
    	}

    	if (GenericValidator.isBlankOrNull(wdtbFormVO.getWdtbVO().getOpertResultflId())) {
    		wdtbFormVO.getWdtbVO().setOpertResultflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(wdtbFormVO.getWdtbVO().getOpertResultflId());

			final List<AtchmnflVO> opertResultflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("opertResultflList", opertResultflList);
    	}

    	if (GenericValidator.isBlankOrNull(wdtbFormVO.getWdtbVO().getLoginResultflId())) {
    		wdtbFormVO.getWdtbVO().setLoginResultflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(wdtbFormVO.getWdtbVO().getLoginResultflId());

			final List<AtchmnflVO> loginResultflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("loginResultflList", loginResultflList);
    	}

    	if (GenericValidator.isBlankOrNull(wdtbFormVO.getWdtbVO().getServerOneLogflId())) {
    		wdtbFormVO.getWdtbVO().setServerOneLogflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(wdtbFormVO.getWdtbVO().getServerOneLogflId());

			final List<AtchmnflVO> serverOneLogflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("serverOneLogflList", serverOneLogflList);
    	}

    	if (GenericValidator.isBlankOrNull(wdtbFormVO.getWdtbVO().getServerTwoLogflId())) {
    		wdtbFormVO.getWdtbVO().setServerTwoLogflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(wdtbFormVO.getWdtbVO().getServerTwoLogflId());

			final List<AtchmnflVO> serverTwoLogflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("serverTwoLogflList", serverTwoLogflList);
    	}

		// 확인 담당자
		final UserVO userVO = new UserVO();
		userVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
		userVO.setUserTyCode(userVO.USER_TY_CODE_CSTMR);
		model.addAttribute("cstmrList", userService.retrieveList(userVO));
		
		return "/itsm/wdtb/mngr/edit";
	}
	
	/**
	 * 배포확인서 수정
	 * @param session
	 * @param wdtbFormVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/wdtb/mngr/update.do")
	public String update(HttpSession session, WdtbFormVO wdtbFormVO) throws Exception {
		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

		wdtbFormVO.getWdtbVO().setUpdtId(loginUserVO.getUserId());
		wdtbFormVO.getWdtbVO().makeWdtbDt();
		wdtbFormVO.getWdtbVO().makeWdtbDtOne();
		wdtbFormVO.getWdtbVO().makeWdtbDtTwo();
		
		wdtbService.update(wdtbFormVO.getWdtbVO());
		
		return "forward:/itsm/wdtb/mngr/retrievePagingList.do";
	}
	
	/**
	 * 배포확인서 삭제
	 * @param session	
	 * @param wdtbFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/wdtb/mngr/delete.do")
	public String delete(HttpSession session, WdtbFormVO wdtbFormVO) throws Exception {
		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
		wdtbFormVO.getWdtbVO().setUpdtId(loginUserVO.getUserId());
		
		final SrvcRsponsVO vo = new SrvcRsponsVO();
		vo.setWdtbCnfirmNo(wdtbFormVO.getWdtbVO().getWdtbCnfirmNo());
		
		wdtbService.delete(wdtbFormVO.getWdtbVO());

		final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
		srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

		for (String srvcRsponsNo : wdtbFormVO.getSrvcRsponsNos()) {
			vo.setSrvcRsponsNo(srvcRsponsNo);
			srvcRsponsService.deleteWdtbCnfirm(srvcRsponsVO);
		}
		
		return "forward:/itsm/wdtb/mngr/retrievePagingList.do";
	}
	
	/**
	 * 배포확인서 엑셀 다운로드
	 * @param wdtbVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/itsm/wdtb/mngr/retrieveExcelList.do")
	public String retrieveExcelList(WdtbFormVO wdtbFormVO, ModelMap model) throws Exception {
	    LOGGER.info("ProcessMt: " + wdtbFormVO.getSrvcRsponsVO().getProcessMt());
	    
		final List<SrvcRsponsVO> list = srvcRsponsService.retrieveAllwdtbList(wdtbFormVO.getSrvcRsponsVO());
	    
//	    List<WdtbVO> list= wdtbService.retrieveList(wdtbVO);
	    
		model.addAttribute("templateName", ExcelView.TEMPLATE_WDTB);
		model.addAttribute("excelName", ExcelView.EXCEL_NAME_WDTB);
	    model.addAttribute("srvcRsponsVO", list);

	    return "excelView";
	 }
	
	/**
	 * 배포확인서 유무 확인
	 * @param wdtbVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/itsm/wdtb/mngr/retrieveWdtbAjax.do")
	public String retrieveWdtbAjax(WdtbVO wdtbVO, ModelMap model) throws Exception {
		if (wdtbVO.getProcessMt() != null) {
			if (wdtbService.retrieveList(wdtbVO).size() > 0) {
				model.addAttribute("wdtbList", wdtbService.retrieveList(wdtbVO));
    		}
			else {
    			model.addAttribute("returnMessage", "empty");
    		}

    		return "jsonView";
    	}
    	else if (wdtbService.retrieve(wdtbVO) == null) {
			model.addAttribute("returnMessage", "empty");
		}
		
		return "jsonView";
	}
}
