package kr.go.smplatform.itsm.infraopert.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmFormVO;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.infraopert.service.InfraOpertService;
import kr.go.smplatform.itsm.infraopert.vo.InfraOpertFormVO;
import kr.go.smplatform.itsm.infraopert.vo.InfraOpertVO;
import kr.go.smplatform.itsm.report.web.RepMasterController;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.wdtb.vo.WdtbFormVO;

@Controller
public class InfraOpertController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepMasterController.class);
	
	@Resource(name = "infraOpertService")
	private InfraOpertService infraOperService;
	
	@Resource(name = "srvcRsponsService")
	private SrvcRsponsService srvcRsponsService;
	
	@Resource(name = "atchmnflService")
	private AtchmnflService atchmnflService;
	
	@Resource(name = "sysChargerService")
	private SysChargerService sysChargerService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	/**
	 * 인프라작업목록 조회
	 * @param session
	 * @param infraOpertFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/retrievePagingList.do")
	public String retrievePagingList(InfraOpertFormVO infraOpertFormVO, ModelMap model) throws Exception {
		infraOpertFormVO.getSearchInfraOpertVO().setDeleteYn("N");
		infraOpertFormVO.getSearchInfraOpertVO().setInfraOpertYn("Y");
		
		/** EgovPropertyService.sample */
		infraOpertFormVO.getSearchInfraOpertVO().setPageUnit(propertiesService.getInt("pageUnit"));
		infraOpertFormVO.getSearchInfraOpertVO().setPageSize(propertiesService.getInt("pageSize"));
		
		/** paging setting */
		final PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(infraOpertFormVO.getSearchInfraOpertVO().getPageIndex());
		paginationInfo.setRecordCountPerPage(infraOpertFormVO.getSearchInfraOpertVO().getRecordCountPerPage());
		paginationInfo.setPageSize(infraOpertFormVO.getSearchInfraOpertVO().getPageSize());
		paginationInfo.setTotalRecordCount(
				srvcRsponsService.retrievePagingListCnt(infraOpertFormVO.getSearchInfraOpertVO())
		);
		
		infraOpertFormVO.getSearchInfraOpertVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
		infraOpertFormVO.getSearchInfraOpertVO().setLastIndex(paginationInfo.getLastRecordIndex());
		infraOpertFormVO.getSearchInfraOpertVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		final List<SrvcRsponsVO> list = srvcRsponsService.retrieveInfraOpertPagingList(infraOpertFormVO.getSearchInfraOpertVO());
		
		// 담당자 목록 조회
		final UserVO userVo = new UserVO();
		userVo.setUserTyCode(UserVO.USER_TY_CODE_CHARGER);

		final List<UserVO> sysChargerList = userService.retrieveList(userVo);
		
		retrieveCmmnCode(model);
		
		infraOpertFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
		
		model.addAttribute("sysChargerList", sysChargerList);
		model.addAttribute("resultList", list);
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "/itsm/infraopert/mngr/list";
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
	 * 작성화면 조회
	 * @param session
	 * @param infraOpertFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/createView.do")
	public String createView(HttpSession session, InfraOpertFormVO infraOpertFormVO, ModelMap model) throws Exception {
		retrieveCmmnCode(model);
		
		// 파일 ID 생성
		infraOpertFormVO.getInfraOpertVO().setInfraPlanAtchmnflId(UUID.randomUUID().toString());
		infraOpertFormVO.getInfraOpertVO().setInfraResultAtchmnflId(UUID.randomUUID().toString());
		infraOpertFormVO.getInfraOpertVO().setInfraPlanEtcAtchmnflId(UUID.randomUUID().toString());
		infraOpertFormVO.getInfraOpertVO().setInfraResultEtcAtchmnflId(UUID.randomUUID().toString());
		
		SrvcRsponsVO retrieveSrvcRsponsVO = new SrvcRsponsVO();

		for (String srvcNo : infraOpertFormVO.getSrvcRsponsNos()) {
			final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
			srvcRsponsVO.setSrvcRsponsNo(srvcNo);
			
			if (retrieveSrvcRsponsVO.getSrvcRsponsSj() == null) {
				retrieveSrvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsVO);
				infraOpertFormVO.setSrvcRsponsVO(retrieveSrvcRsponsVO);
			}
			else {
				retrieveSrvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsVO);
				infraOpertFormVO.getSrvcRsponsVO().setSrvcRsponsSj(
						infraOpertFormVO.getSrvcRsponsVO().getSrvcRsponsSj() + ", "+ retrieveSrvcRsponsVO.getSrvcRsponsSj()
				);
			}
		}
		
		LOGGER.debug("srvcList : {} " , infraOpertFormVO.getSrvcRsponsNos());
		
		// 중복등록 방지 처리
    	final String saveToken = ITSMDefine.generateSaveToken(session);
    	infraOpertFormVO.getInfraOpertVO().setSaveToken(saveToken);
		
		return "/itsm/infraopert/mngr/edit";
	}
	
	/**
	 * 작성
	 * @param session
	 * @param infraOpertFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/create.do")
	public String create(HttpSession session, InfraOpertFormVO infraOpertFormVO) throws Exception {
		// 중복등록 방지 처리
    	if (!ITSMDefine.checkSaveToken(session, infraOpertFormVO.getInfraOpertVO().getSaveToken())) {
    		return "forward:/itsm/infraOpert/mngr/retrievePagingList.do";
    	}
		
		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
		infraOpertFormVO.getInfraOpertVO().setCreatId(loginUserVO.getUserId());
		
		infraOperService.create(infraOpertFormVO.getInfraOpertVO());
		
		final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();

		for (String srvcRsponsNo : infraOpertFormVO.getSrvcRsponsNos()) {
			srvcRsponsVO.setSrvcRsponsNo(srvcRsponsNo);
			srvcRsponsVO.setInfraOpertNo(infraOpertFormVO.getInfraOpertVO().getInfraOpertNo());
			srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
			
			srvcRsponsService.updateInfraOpert(srvcRsponsVO);
		}
		
		return "forward:/itsm/infraOpert/mngr/retrievePagingList.do";
	}
	
	/**
	 * 인프라작업 수정화면 조회
	 * @param infraOpertFormVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/updateView.do")
	public String updateView(InfraOpertFormVO infraOpertFormVO, ModelMap model) throws Exception {
		retrieveCmmnCode(model);
		
		infraOpertFormVO.setSrvcRsponsVO(srvcRsponsService.retrieve(infraOpertFormVO.getSrvcRsponsVO()));
		infraOpertFormVO.getInfraOpertVO().setInfraOpertNo(infraOpertFormVO.getSrvcRsponsVO().getInfraOpertNo());
		infraOpertFormVO.setInfraOpertVO(infraOperService.retrieve(infraOpertFormVO.getInfraOpertVO()));
		
		if (infraOpertFormVO.getInfraOpertVO() != null) {
			final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
			srvcRsponsVO.setInfraOpertNo(infraOpertFormVO.getInfraOpertVO().getInfraOpertNo());

			final List<SrvcRsponsVO> srvcRsponsVOlist = srvcRsponsService.retrieveAllList(srvcRsponsVO);
			
			for (SrvcRsponsVO vo : srvcRsponsVOlist) {
				infraOpertFormVO.getSrvcRsponsNos().add(vo.getSrvcRsponsNo());

				if (!infraOpertFormVO.getSrvcRsponsVO().getSrvcRsponsSj().equals(vo.getSrvcRsponsSj())) {
					infraOpertFormVO.getSrvcRsponsVO().setSrvcRsponsSj(
							infraOpertFormVO.getSrvcRsponsVO().getSrvcRsponsSj() + ", "+ vo.getSrvcRsponsSj()
					);
				}
			}
		}
		else {
			infraOpertFormVO.getSrvcRsponsNos().add(infraOpertFormVO.getSrvcRsponsVO().getSrvcRsponsNo());
		}
		
		final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();

    	if (GenericValidator.isBlankOrNull(infraOpertFormVO.getInfraOpertVO().getInfraPlanAtchmnflId())) {
    		infraOpertFormVO.getInfraOpertVO().setInfraPlanAtchmnflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(infraOpertFormVO.getInfraOpertVO().getInfraPlanAtchmnflId());

    		final List<AtchmnflVO> infraPlanAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("infraPlanAtchmnflList", infraPlanAtchmnflList);
    	}

    	if (GenericValidator.isBlankOrNull(infraOpertFormVO.getInfraOpertVO().getInfraResultAtchmnflId())) {
    		infraOpertFormVO.getInfraOpertVO().setInfraResultAtchmnflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(infraOpertFormVO.getInfraOpertVO().getInfraResultAtchmnflId());

    		final List<AtchmnflVO> infraResultAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("infraResultAtchmnflList", infraResultAtchmnflList);
    	}

    	if (GenericValidator.isBlankOrNull(infraOpertFormVO.getInfraOpertVO().getInfraPlanEtcAtchmnflId())) {
    		infraOpertFormVO.getInfraOpertVO().setInfraPlanEtcAtchmnflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(infraOpertFormVO.getInfraOpertVO().getInfraPlanEtcAtchmnflId());

    		final List<AtchmnflVO> infraPlanEtcAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("infraPlanEtcAtchmnflList", infraPlanEtcAtchmnflList);
    	}

    	if (GenericValidator.isBlankOrNull(infraOpertFormVO.getInfraOpertVO().getInfraResultEtcAtchmnflId())) {
    		infraOpertFormVO.getInfraOpertVO().setInfraResultEtcAtchmnflId(UUID.randomUUID().toString());
    	}
		else {
    		paramAtchmnflVO.setAtchmnflId(infraOpertFormVO.getInfraOpertVO().getInfraResultEtcAtchmnflId());

    		final List<AtchmnflVO> infraResultEtcAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
    		model.addAttribute("infraResultEtcAtchmnflList", infraResultEtcAtchmnflList);
    	}
		
		return "/itsm/infraopert/mngr/edit";
	}
	
	/**
	 * 인프라 작업 수정
	 * @param session
	 * @param infraOpertFormVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/update.do")
	public String update(HttpSession session, InfraOpertFormVO infraOpertFormVO) throws Exception {
		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

		infraOpertFormVO.getInfraOpertVO().setUpdtId(loginUserVO.getUserId());
		infraOperService.update(infraOpertFormVO.getInfraOpertVO());
		
		final SrvcRsponsVO vo = new SrvcRsponsVO();
		for (String srvcRsponsNo : infraOpertFormVO.getSrvcRsponsNos()) {
			vo.setSrvcRsponsNo(srvcRsponsNo);
			vo.setInfraOpertNo(infraOpertFormVO.getInfraOpertVO().getInfraOpertNo_sub());
			vo.setUpdtId(loginUserVO.getUserId());
			srvcRsponsService.updateInfraOpert(vo);
		}

		return "forward:/itsm/infraOpert/mngr/retrievePagingList.do";
	}
	
	/**
	 * 인프라작업 삭제
	 * @param session
	 * @param infraOpertFormVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/delete.do")
	public String delete(HttpSession session, InfraOpertFormVO infraOpertFormVO) throws Exception {
		final UserVO loginUserVo = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
		infraOpertFormVO.getInfraOpertVO().setUpdtId(loginUserVo.getUserId());
		
		infraOperService.delete(infraOpertFormVO.getInfraOpertVO());
		
		final SrvcRsponsVO vo = new SrvcRsponsVO();
		vo.setUpdtId(loginUserVo.getUserId());

		for (String srvcRsponsNo : infraOpertFormVO.getSrvcRsponsNos()) {
			vo.setSrvcRsponsNo(srvcRsponsNo);
			srvcRsponsService.deleteInfraOpert(vo);
		}
		
		return "forward:/itsm/infraOpert/mngr/retrievePagingList.do";
	}
	
	/**
	 * 
	 * @param infraOpertNo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itsm/infraOpert/mngr/retrieveAjax.do")
	public String retrieveAjax(String infraOpertNo, ModelMap model) throws Exception {
		LOGGER.info("infraOpertNo = {}", infraOpertNo);
		
		InfraOpertVO searchVO = new InfraOpertVO();
		searchVO.setInfraOpertNo(infraOpertNo);

		InfraOpertVO vo = infraOperService.retrieve(searchVO);
		
		if (vo == null) {
			model.addAttribute("returnMessage", null);
		}
		else {
			model.addAttribute("returnMessage", vo);
		}
		
		return "jsonView";
	}
}
