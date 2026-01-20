package kr.go.smplatform.itsm.srvcrspons.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.excel.ExcelView;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.service.FuncImprvmService;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsFormVO;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.sms.service.SmsService;
import kr.go.smplatform.sms.vo.SmsVO;
import kr.go.smplatform.sms.web.SmsController;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class SrvcRsponsMngrController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SrvcRsponsMngrController.class);
    
    @Resource(name = "srvcRsponsService")
    private SrvcRsponsService srvcRsponsService;
    
    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;
    
    @Resource(name = "funcImprvmService")
    private FuncImprvmService funcImprvmService;
    
    @Resource(name = "sysChargerService")
    private SysChargerService sysChargerService;
    
    @Resource(name = "smsService")
    private SmsService smsService;
    
    @Resource(name = "userService")
    private UserService userService;

    /**
     * 사용자 목록을 조회한다. (paging)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/mngr/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/mngr/retrievePagingList.do")
    public String retrievePagingList(SrvcRsponsFormVO srvcRsponsFormVO, ModelMap model) throws Exception {
        /** EgovPropertyService.sample */
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** pageing setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageUnit());
        paginationInfo.setPageSize(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageSize());
        
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setDeleteYn("N");
        
        final List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(srvcRsponsFormVO.getSearchSrvcRsponsVO());

        model.addAttribute("resultList", list);
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrievePagingListCnt(srvcRsponsFormVO.getSearchSrvcRsponsVO());

        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        srvcRsponsFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
        
        return "/itsm/srvcrspons/mngr/list";
    }
    
    @RequestMapping(value="/srvcrspons/mngr/retrieveExcelList.do")
    public String retrieveExcelList(SrvcRsponsVO srvcRsponsVO, ModelMap model) throws Exception {
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setProcessMt("201711");
//        SrvcRsponsVO paramSrvcRsponsVO = new SrvcRsponsVO();
//        paramSrvcRsponsVO.setProcessMt(processMt);
        LOGGER.info("ProcessMt: "+srvcRsponsVO.getProcessMt());
        
        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveAllList(srvcRsponsVO);

        model.addAttribute("templateName", ExcelView.TEMPLATE_SR);
        model.addAttribute("excelName", ExcelView.EXCEL_NAME_SR);
        model.addAttribute("srvcRsponsVO", list);

        return "excelView";
    }
    
    /**
     * SR요청 목록을 조회한다. (시스템 담당자별)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/mngr/retrieveList.do")
    public String retrieveList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
//        UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
//
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setChargerId(loginUserVO.getUserId());
//
//        List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        model.addAttribute("resultList", list);
        
        final String saveToken = ITSMDefine.generateSaveToken(session);

        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);
        retrieveCmmnCode(model);
        
        return "/itsm/srvcrspons/mngr/stateList";
    }
    
    @RequestMapping(value="/srvcrspons/mngr/retrieveSrProcList.do")
    public String retrieveSrProcList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        /** EgovPropertyService.sample */
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** pageing setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageUnit());
        paginationInfo.setPageSize(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageSize());
        
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setDeleteYn("N");

        // A035
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        //R001 모두 보기, R003 해당 서비스만 보기, R005 자신거만
        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserId(loginUserVO.getUserId());
        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        List<SrvcRsponsVO> list = null;
//        if( "R003".equals(loginUserVO.getUserTyCode())) {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        } else {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        }

        model.addAttribute("resultList", list);
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrieveSrProcPagingListCnt(srvcRsponsFormVO.getSearchSrvcRsponsVO());

        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        srvcRsponsFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
        
        return "/itsm/srvcrspons/mngr/srproclist";

    }

    @RequestMapping(value="/srvcrspons/mngr/retrieveSrVrList.do")
    public String retrieveSrVrList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        /** EgovPropertyService.sample */
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** pageing setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageUnit());
        paginationInfo.setPageSize(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageSize());
        
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setDeleteYn("N");

        // A035
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        //R001 모두 보기, R003 해당 서비스만 보기, R005 자신거만
        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserId(loginUserVO.getUserId());
        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrVrList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        List<SrvcRsponsVO> list = null;
//        if( "R003".equals(loginUserVO.getUserTyCode())) {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        } else {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        }

        model.addAttribute("resultList", list);
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrieveSrVrPagingListCnt(srvcRsponsFormVO.getSearchSrvcRsponsVO());

        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        srvcRsponsFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
        
        return "/itsm/srvcrspons/mngr/srvrlist";

    }

    @RequestMapping(value="/srvcrspons/mngr/retrieveSrFnList.do")
    public String retrieveSrFnList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        /** EgovPropertyService.sample */
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** pageing setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageUnit());
        paginationInfo.setPageSize(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageSize());
        
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setDeleteYn("N");

        // A035
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        //R001 모두 보기, R003 해당 서비스만 보기, R005 자신거만
        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setUserId(loginUserVO.getUserId());
        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrFnList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        List<SrvcRsponsVO> list = null;
//        if( "R003".equals(loginUserVO.getUserTyCode())) {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        } else {
//        	list = srvcRsponsService.retrieveSrProcList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
//        }

        model.addAttribute("resultList", list);
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrieveSrFnPagingListCnt(srvcRsponsFormVO.getSearchSrvcRsponsVO());

        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        srvcRsponsFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
        
        return "/itsm/srvcrspons/mngr/srfnlist";

    }

    
    @RequestMapping(value="/srvcrspons/mngr/retrieveSrEvList.do")
    public String retrieveSrEvList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        /** EgovPropertyService.sample */
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageUnit(propertiesService.getInt("pageUnit"));
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** pageing setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageUnit());
        paginationInfo.setPageSize(srvcRsponsFormVO.getSearchSrvcRsponsVO().getPageSize());
        
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setLastIndex(paginationInfo.getLastRecordIndex());
        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//        srvcRsponsFormVO.getSearchSrvcRsponsVO().setDeleteYn("N");
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(loginUserVO.getUserTyCode());
        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrEvList(searchSrvcRsponsVO);

        model.addAttribute("resultList", list);
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrieveSrEvPagingListCnt(searchSrvcRsponsVO);

        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        srvcRsponsFormVO.setProcessMt(new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(new Date()));
        
        return "/itsm/srvcrspons/mngr/evlist";

    }
    

    /**
     * SR요청 목록을 조회한다. (시스템 담당자별)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/mngr/retrieveListDiv.do")
    public String retrieveListDiv(SrvcRsponsVO srvcRsponsVO, HttpSession session, ModelMap model) throws Exception {
        final UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);

        srvcRsponsVO.setChargerId(loginUserVO.getUserId());

        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsVO);

        model.addAttribute("resultList", list);
        
        return "/itsm/srvcrspons/mngr/stateListDiv";
    }
    
    /**
     * 미응답건 Sms전송
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/srvcrspons/mngr/retrieveListDivSmsAjax.do")
    public String retrieveListDivSmsAjax(HttpSession session, ModelMap model) throws Exception {
        final UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();

        srvcRsponsVO.setChargerId(loginUserVO.getUserId());
        
        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsVO);
        model.addAttribute("resultList", list);
        
        final Calendar cal = Calendar.getInstance();
        boolean isSmsGo = false;

        for (SrvcRsponsVO vo : list) {
            if (!vo.getOrderLevel().equals("1")) {
                model.addAttribute("returnMessage", "false");
                break;
            }
            else {
                cal.setTime(vo.getRequstDt());
                cal.add(Calendar.MINUTE, 20);
                
                if (cal.getTime().before(new Date()) && vo.getSmsChk().equals("N")) {
                    if (!isSmsGo) {
                        SysChargerVO sysChargerVo = new SysChargerVO();
                        sysChargerVo.setSysCode(vo.getTrgetSrvcCode());
                        
                        final List<SysChargerVO> chargerList = sysChargerService.retrieveChargerList(sysChargerVo);
                        
                        for (SysChargerVO charger: chargerList) {
                            isSmsGo = true;
                            SmsVO smsVo = new SmsVO();
                            smsVo.setSubject("ITSM");
                            smsVo.setMsg("SR요청을 확인해주세요.");
                            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
                            smsVo.setDestel(charger.getMoblphon());
                            smsVo.setSrctel("0443000762");
                            try {
                                final String result = smsService.create(smsVo);
                                LOGGER.info("sms vo = {}", smsVo);
                                LOGGER.info("sms result = {}", result);
                            } catch (Exception e) {
                                LOGGER.error("sms failed = {}", smsVo);
                                e.printStackTrace();
                            }
                        }
                    }
                    srvcRsponsService.updateSmsChk(vo);
                }
            }
        }

        return "jsonView";
    }

    private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("trgetSrvcCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.TRGET_SRVC_CODE)
        ));
        
        model.addAttribute("changeDfflyCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.CHANGE_DFFLY_CODE)
        ));
        
        model.addAttribute("srvcRsponsClCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.SRVC_RSPONS_CL_CODE)
        ));
        
        model.addAttribute("processStdrCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.PROCESS_STDR_CODE)
        ));
        
        model.addAttribute("srvcRsponsBasisCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.SRVC_RSPONS_BASIS_CODE)
        ));
        UserVO uservo = new UserVO();
        uservo.setUserTyCode2(UserVO.USER_TY_CODE_R005);
        model.addAttribute("srvcManagerCodeVOList", userService.retrieveList(uservo));

        //uservo.setUserTyCode(UserVO.USER_TY_CODE_R005);
        //uservo.setUserTyCode2("");
        //model.addAttribute("srvcSRUserCodeVOList", userService.retrieveList(uservo));
    } 
    
    /**
     * 글 등록 화면을 조회한다.
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/mngr/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/createView.do")
    public String createView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
        final Date currentDate = new Date();
        
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -5));
        srvcRsponsVO.setRspons1stDt(DateUtils.addMinutes(currentDate, -3));
        srvcRsponsVO.setProcessDt(currentDate);
        
        final UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setChargerId(loginUserVO.getUserId());
        srvcRsponsVO.setChargerUserNm(loginUserVO.getUserNm());
        srvcRsponsVO.setProcessStdrCode(SrvcRsponsVO.DEFAULT_PROCESS_STDR_CODE);
        srvcRsponsVO.setChangeDfflyCode(SrvcRsponsVO.DEFAULT_CHANGE_DFFLY_CODE);
        srvcRsponsVO.setSrvcRsponsBasisCode(SrvcRsponsVO.DEFAULT_SRVC_RSPONS_BASIS_CODE);
        
        srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        
        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO); 
        retrieveCmmnCode(model);
        
//        srvcRsponsFormVO.getUserVO().setUserTyCode(UserVO.USER_TY_CODE_NORMAL);
        
        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);

        return "/itsm/srvcrspons/mngr/edit";
    }
    
    /**
     * 글을 등록한다.
     * @param userVO - 등록할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/create.do")
    public String create(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        srvcRsponsVO.setSrvcRsponsSj(
                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
        );

        // Server-Side Validation
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
//            model.addAttribute("userVO", srvcRsponsFormVO.getUserVO());
            return "/itsm/srvcrspons/mngr/edit";
        }
        
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, srvcRsponsVO.getSaveToken())) {
            if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
                return "forward:/srvcrspons/mngr/retrievePagingList.do";
            }
            else {
                return "forward:/srvcrspons/mngr/retrieveList.do";
            }
        }
        
        // session에서 로그인 정보를 가져온다.
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.makeRequstDt();
        srvcRsponsVO.makeRspons1stDt();
        srvcRsponsVO.makeProcessDt();
        
        srvcRsponsVO.setChargerId(loginUserVO.getUserId());
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        
        try {
            srvcRsponsService.createForMngr(srvcRsponsVO);
        } catch (Exception e) {
            LOGGER.error("신규 등록 중 오류 발생");
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "등록중 오류가 발생되었습니다. 관리자에게 문의하세요.");
        }
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrievePagingList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveList.do";
    }
    
    /**
     * 글 수정화면을 조회한다.
     * @param id - 수정할 글 id
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/mngr/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updateView.do")
    public String updateView(SrvcRsponsFormVO srvcRsponsFormVO ,ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
            srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

            final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("requstAtchmnflList", requstAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
            srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

            final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
        }

        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
        retrieveCmmnCode(model);

        return "/itsm/srvcrspons/mngr/edit";
    }
    
    /**
     * 글 평가화면을 조회한다.
     * @param id - 수정할 글 id
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/mngr/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updateEvView.do")
    public String updateEvView(SrvcRsponsFormVO srvcRsponsFormVO ,HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
            srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

            final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("requstAtchmnflList", requstAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
            srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

            final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
        }

        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
        retrieveCmmnCode(model);

        //작성자만 만족도 가능
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        
        //만족도 평가된 요청은 조회기능만 사용
        if (srvcRsponsVO.getChangeDfflyCode() == null && userId.equals(srvcRsponsVO.getRqesterId()))
        	return "/itsm/srvcrspons/mngr/evedit";
        else {
        	if (userId.equals(srvcRsponsVO.getRqesterId())) { 
        		return "/itsm/srvcrspons/mngr/evview";
        	} else {
        		return "/itsm/srvcrspons/mngr/evview2";
        	}
        }
    }

    //접수처리
    @RequestMapping("/srvcrspons/mngr/updateSrpView.do")
    public String updateSrpView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
        final Date currentDate = new Date();

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
            srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

            final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("requstAtchmnflList", requstAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
            srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

            final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
        }
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        model.addAttribute("recvuser", loginUserVO.getUserNm());

        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
        retrieveCmmnCode(model);

        //처리는 시스템 담당자, 관리자만 가능/그외는 조회만
        if (srvcRsponsVO.getProcessDt() == null && !UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
        	srvcRsponsVO.setProcessDt(DateUtils.addMinutes(currentDate, 0));
        	return "/itsm/srvcrspons/mngr/srprocedit";
        }                                                              
        
        return "/itsm/srvcrspons/mngr/srprocview";
    }

    //검증처리
    @RequestMapping("/srvcrspons/mngr/updateSrVrView.do")
    public String updateSrVrView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
        final Date currentDate = new Date();

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
            srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

            final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("requstAtchmnflList", requstAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
            srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

            final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
        }
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        model.addAttribute("recvuser", loginUserVO.getUserNm());

        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
        retrieveCmmnCode(model);

//        UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())
//        srvcRsponsVO.getRqesterId().equals(loginUserVO.getUserId())
//        srvcRsponsVO.getRefIds().contains(loginUserVO.getUserId())
        //검증는 요청자가 그외는 시스템 담당자, 관리자 조회만
        //|| srvcRsponsVO.getRefIds().contains(loginUserVO.getUserId())
        if (srvcRsponsVO.getVerifyDt() == null 
        		&& (srvcRsponsVO.getRqesterId().equals(loginUserVO.getUserId())) ) {
        	srvcRsponsVO.setVerifyDt(currentDate);
            return "/itsm/srvcrspons/mngr/srvredit";
        }                                                              
        
        return "/itsm/srvcrspons/mngr/srvrview";
    }

    //완료처리
    @RequestMapping("/srvcrspons/mngr/updateSrFnView.do")
    public String updateSrFnView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
        final Date currentDate = new Date();

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
            srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

            final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("requstAtchmnflList", requstAtchmnflList);
        }

        if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
            srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
        else {
            paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

            final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
            model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
        }
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        model.addAttribute("recvuser", loginUserVO.getUserNm());

        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
        retrieveCmmnCode(model);

        //처리는 시스템 담당자, 관리자만 가능/그외는 조회만
        if (srvcRsponsVO.getFinishDt() == null && !UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
        	srvcRsponsVO.setFinishDt(currentDate);
            return "/itsm/srvcrspons/mngr/srfnedit";
        }                                                              
        
        return "/itsm/srvcrspons/mngr/srfnview";
    }

    /**
     * 글 수정화면을 조회한다.
     * @param id - 수정할 글 id
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/mngr/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updateStateView.do")
    public String updateStateView(SrvcRsponsFormVO srvcRsponsFormVO, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        
        if (srvcRsponsVO != null) {
            final AtchmnflVO paramAtchmnflVO = new AtchmnflVO();
            if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRequstAtchmnflId())) {
                srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
            }
            else {
                paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRequstAtchmnflId());

                final List<AtchmnflVO> requstAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
                model.addAttribute("requstAtchmnflList", requstAtchmnflList);
            }

            if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
                srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
            }
            else {
                paramAtchmnflVO.setAtchmnflId(srvcRsponsVO.getRsponsAtchmnflId());

                final List<AtchmnflVO> rsponsAtchmnflList = atchmnflService.retrieveList(paramAtchmnflVO);
                model.addAttribute("rsponsAtchmnflList", rsponsAtchmnflList);
            }

            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
            retrieveCmmnCode(model);
            
            if (srvcRsponsVO.getRspons1stDt() == null) {
                return "/itsm/srvcrspons/mngr/editState1";
            }
            else if (srvcRsponsVO.getProcessDt() == null) {
                retrieveCmmnCode(model);
                
//                srvcRsponsVO.setProcessStdrCode(SrvcRsponsVO.DEFAULT_PROCESS_STDR_CODE);
//                srvcRsponsVO.setChangeDfflyCode(SrvcRsponsVO.DEFAULT_CHANGE_DFFLY_CODE);
                
                if (GenericValidator.isBlankOrNull(srvcRsponsVO.getRsponsAtchmnflId())) {
                    srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
                }
                
                return "/itsm/srvcrspons/mngr/editState2";
            }
        }
        
        return "/itsm/srvcrspons/mngr/edit";
    }
    
    /**
     * 글을 수정한다.
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param status
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updateState.do")
    public String updateState(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO) throws Exception {
        final SrvcRsponsVO responseVO = srvcRsponsFormVO.getSrvcRsponsVO();
        LOGGER.debug("srvcRsponsVO: " + responseVO);
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        responseVO.setUpdtId(loginUserVO.getUserId());

        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(responseVO);
        
        if (srvcRsponsVO != null) {
            if (srvcRsponsVO.getRspons1stDt() == null) {
                // 1차응답만 처리
                srvcRsponsService.updateRspons1st(responseVO);
            }
            else if (srvcRsponsVO.getProcessDt() == null) {
                // 처리 결과만 등록
                srvcRsponsService.updateProcess(responseVO);

                final SmsVO smsVo = new SmsVO();
                smsVo.setSubject("ITSM");
                smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
                smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
                smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
                smsVo.setSrctel("0443000762");

                try {
                    final String result = smsService.create(smsVo);
                    LOGGER.info("sms vo = {}", smsVo);
                    LOGGER.info("sms result = {}", result);
                } catch (Exception e) {
                    LOGGER.error("sms failed = {}", smsVo);
                    e.printStackTrace();
                }
            }
        }
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrievePagingList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveList.do";
    }

    /**
     * AJAX를 통해 사용중인 ID인지 검사 한다.
     * @param srvcRsponsVO
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/srvcrspons/mngr/updateStateAjax.do")    
    public String updateStateAjax(SrvcRsponsVO srvcRsponsVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO retrieveSrvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsVO);
        
        model.addAttribute("returnMessage", "fail");
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        if (retrieveSrvcRsponsVO != null) {
            if (retrieveSrvcRsponsVO.getRspons1stDt() == null) {
                // 1차응답만 처리
                if (GenericValidator.isBlankOrNull(srvcRsponsVO.getProcessStdrCode())) {
                    srvcRsponsVO.setProcessStdrCode(SrvcRsponsVO.DEFAULT_PROCESS_STDR_CODE);
                }

                if (GenericValidator.isBlankOrNull(srvcRsponsVO.getChangeDfflyCode())) {
                    srvcRsponsVO.setChangeDfflyCode(SrvcRsponsVO.DEFAULT_CHANGE_DFFLY_CODE);
                }

                if (GenericValidator.isBlankOrNull(srvcRsponsVO.getSrvcRsponsClCode())) {
                    srvcRsponsVO.setSrvcRsponsClCode(SrvcRsponsVO.SRVC_RSPONS_CL_CODE_S102);
                }

                srvcRsponsService.updateRspons1st(srvcRsponsVO);
                model.remove("returnMessage");
                model.addAttribute("returnMessage", "success");
            }
        }
        
        return "jsonView";
    }

    /**
     * 글을 수정한다.
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/update.do")
    public String update(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        srvcRsponsVO.setSrvcRsponsSj(
                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);

            return "/itsm/srvcrspons/mngr/edit";
        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.makeRequstDt();
        srvcRsponsVO.makeRspons1stDt();
        srvcRsponsVO.makeProcessDt();
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        srvcRsponsService.update(srvcRsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrievePagingList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveList.do";
    }
    
    /**
     * 글을 삭제한다.
     * @param userVO - 삭제할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/delete.do")
    public String delete(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsFormVO.getSrvcRsponsVO().setUpdtId(loginUserVO.getUserId());
        
        LOGGER.info("userVO: " + srvcRsponsFormVO.getSrvcRsponsVO());

        srvcRsponsService.delete(srvcRsponsFormVO.getSrvcRsponsVO());
//        status.setComplete();

        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrievePagingList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveList.do";
    }
    
    
    /**
     * SR처리 입력.
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updatesrproc.do")
    public String updatesrproc(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        srvcRsponsVO.setSrvcRsponsSj(
//                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
//
//            return "/itsm/srvcrspons/mngr/srprocedit";
//        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
//        srvcRsponsVO.makeRequstDt();
//        srvcRsponsVO.makeRspons1stDt();
        srvcRsponsVO.makeProcessDt();
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        
        //if (srvcRsponsVO.getProcessDt() == null) 
        {
            // 처리 결과만 등록
            srvcRsponsService.updateSrProcess(srvcRsponsVO);

//            final SmsVO smsVo = new SmsVO();
//            smsVo.setSubject("ITSM");
//            smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
//            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
//            smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
//            smsVo.setSrctel("0443000762");
//
//            try {
//                final String result = smsService.create(smsVo);
//                LOGGER.info("sms vo = {}", smsVo);
//                LOGGER.info("sms result = {}", result);
//            } catch (Exception e) {
//                LOGGER.error("sms failed = {}", smsVo);
//                e.printStackTrace();
//            }
        }
        
//        srvcRsponsService.update(srvcRsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrieveSrProcList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveSrProcList.do";
    }

    //sr 검증처리
    @RequestMapping("/srvcrspons/mngr/updatesrvr.do")
    public String updatesrvr(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        srvcRsponsVO.setSrvcRsponsSj(
//                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
//
//            return "/itsm/srvcrspons/mngr/srprocedit";
//        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
//        srvcRsponsVO.makeRequstDt();
//        srvcRsponsVO.makeRspons1stDt();
        srvcRsponsVO.makeVerifyDt();
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        
        //if (srvcRsponsVO.getProcessDt() == null) 
        {
            // 처리 결과만 등록
            srvcRsponsService.updateSrVerify(srvcRsponsVO);

//            final SmsVO smsVo = new SmsVO();
//            smsVo.setSubject("ITSM");
//            smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
//            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
//            smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
//            smsVo.setSrctel("0443000762");
//
//            try {
//                final String result = smsService.create(smsVo);
//                LOGGER.info("sms vo = {}", smsVo);
//                LOGGER.info("sms result = {}", result);
//            } catch (Exception e) {
//                LOGGER.error("sms failed = {}", smsVo);
//                e.printStackTrace();
//            }
        }
        
//        srvcRsponsService.update(srvcRsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrieveSrVrList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveSrVrList.do";
    }

    //sr 완료처리
    @RequestMapping("/srvcrspons/mngr/updatesrfn.do")
    public String updatesrfn(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        srvcRsponsVO.setSrvcRsponsSj(
//                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
//
//            return "/itsm/srvcrspons/mngr/srprocedit";
//        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
//        srvcRsponsVO.makeRequstDt();
//        srvcRsponsVO.makeRspons1stDt();
        srvcRsponsVO.makeFinishDt();
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        
        //if (srvcRsponsVO.getProcessDt() == null) 
        {
            // 처리 결과만 등록
            srvcRsponsService.updateSrFinish(srvcRsponsVO);

//            final SmsVO smsVo = new SmsVO();
//            smsVo.setSubject("ITSM");
//            smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
//            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
//            smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
//            smsVo.setSrctel("0443000762");
//
//            try {
//                final String result = smsService.create(smsVo);
//                LOGGER.info("sms vo = {}", smsVo);
//                LOGGER.info("sms result = {}", result);
//            } catch (Exception e) {
//                LOGGER.error("sms failed = {}", smsVo);
//                e.printStackTrace();
//            }
        }
        
//        srvcRsponsService.update(srvcRsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrieveSrFnList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveSrFnList.do";
    }

    /**
     * SR 만족도 입력.
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updatesrev.do")
    public String updatesrev(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        srvcRsponsVO.setSrvcRsponsSj(
//                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
//
//            return "/itsm/srvcrspons/mngr/srprocedit";
//        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
//        srvcRsponsVO.makeRequstDt();
//        srvcRsponsVO.makeRspons1stDt();
//        srvcRsponsVO.makeProcessDt();
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());

        
        //if (srvcRsponsVO.getProcessDt() == null) 
        {
            // 처리 결과만 등록
            srvcRsponsService.updateSrEv(srvcRsponsVO);

//            final SmsVO smsVo = new SmsVO();
//            smsVo.setSubject("ITSM");
//            smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
//            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
//            smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
//            smsVo.setSrctel("0443000762");
//
//            try {
//                final String result = smsService.create(smsVo);
//                LOGGER.info("sms vo = {}", smsVo);
//                LOGGER.info("sms result = {}", result);
//            } catch (Exception e) {
//                LOGGER.error("sms failed = {}", smsVo);
//                e.printStackTrace();
//            }
        }
        
//        srvcRsponsService.update(srvcRsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/mngr/retrieveSrEvList.do";
        }

        return "forward:/srvcrspons/mngr/retrieveSrEvList.do";
    }

    /**
     * SR 재요청 입력. -> SR처리에서 보이도록 저장
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/mngr/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/mngr/updateresr.do")
    public String updateresr(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();
        final Date currentDate = new Date();
        
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, 0));

        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsVO.setReSrvcRsponsNo(srvcRsponsVO.getSrvcRsponsNo());
        srvcRsponsVO.setReSrvcRsponsSj(srvcRsponsVO.getSrvcRsponsSj());
        srvcRsponsVO.setReSrvcRsponsCn(srvcRsponsVO.getSrvcRsponsCn());
        srvcRsponsVO.setSrvcRsponsNo("");
        srvcRsponsVO.setSrvcRsponsSj("");
        srvcRsponsVO.setSrvcRsponsCn("");

        srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());

        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);

        
        return "/itsm/srvcrspons/site/srreedit";
    }
    
    @RequestMapping("/srvcrspons/mngr/updateresr2.do")
    public String updateresr2(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        srvcRsponsVO.setSrvcRsponsSj(
//                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + srvcRsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
//
//            return "/itsm/srvcrspons/mngr/srprocedit";
//        }
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
//        srvcRsponsVO.makeRequstDt();
//        srvcRsponsVO.makeRspons1stDt();
        //srvcRsponsVO.makeProcessDt();
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsVO.setReSrvcRsponsNo(srvcRsponsVO.getSrvcRsponsNo());
        
        //if (srvcRsponsVO.getProcessDt() == null) 
        {
            // 재요철일 등록
        	srvcRsponsService.updateSrEvReRequest(srvcRsponsVO);
        	// 재요청된 SR 등록
            srvcRsponsService.createSrReRequest(srvcRsponsVO);

//            final SmsVO smsVo = new SmsVO();
//            smsVo.setSubject("ITSM");
//            smsVo.setMsg(String.format("%s님이 요청하신 %s이 처리되었습니다", srvcRsponsVO.getRqester1stNm(), srvcRsponsVO.getSrvcRsponsSj()));
//            smsVo.setSendType(SmsController.msgSendType(smsVo.getMsg()));
//            smsVo.setDestel(srvcRsponsVO.getRqester1stCttpc());
//            smsVo.setSrctel("0443000762");
//
//            try {
//                final String result = smsService.create(smsVo);
//                LOGGER.info("sms vo = {}", smsVo);
//                LOGGER.info("sms result = {}", result);
//            } catch (Exception e) {
//                LOGGER.error("sms failed = {}", smsVo);
//                e.printStackTrace();
//            }
        }
        
//        srvcRsponsService.update(srvcRsponsVO);
        
//        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
//            return "forward:/itsm/srvcrspons/mngr/retrieveSrEvList.do";
//        }

        return "forward:/srvcrspons/mngr/retrieveSrEvList.do";
    }
    
    /**
     * 요청자 정보 목록을 조회한다.
     * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
     * @param model
     * @return "/user/mngr/egovSampleList"
     */
    @RequestMapping(value="/srvcrspons/mngr/retrieveRqesterNmListAjax.do")
    public String retrieveRqesterNmListAjax(SrvcRsponsVO srvcRsponsVO, ModelMap model) {
        if (!GenericValidator.isBlankOrNull(srvcRsponsVO.getRqesterNm())) {
            LOGGER.debug("srvcRsponsVO.getRqesterNm(): " + srvcRsponsVO.getRqesterNm());

            try {
                final List<SrvcRsponsVO> resultList = srvcRsponsService.retrieveRqesterNmList(srvcRsponsVO);
                model.addAttribute("returnMessage", "success");
                model.addAttribute("resultList", resultList);
            }
            catch(Exception e) {
                model.addAttribute("returnMessage", "fail");
            }
        }
        else {
            model.addAttribute("returnMessage", "fail");
        }

        return "jsonView";
    }
    
    /**
     * 요청자 정보 목록을 조회한다.
     * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
     * @param model
     * @return "/user/mngr/egovSampleList"
     */
    @RequestMapping(value="/srvcrspons/mngr/retrieveRqester1stNmListAjax.do")
    public String retrieveRqester1stNmListAjax(SrvcRsponsVO srvcRsponsVO, ModelMap model) {
        if (!GenericValidator.isBlankOrNull(srvcRsponsVO.getRqester1stNm())) {
            LOGGER.debug("srvcRsponsVO.getRqester1stNm(): " + srvcRsponsVO.getRqester1stNm());

            try {
                final List<SrvcRsponsVO> resultList = srvcRsponsService.retrieveRqester1stNmList(srvcRsponsVO);

                model.addAttribute("returnMessage", "success");
                model.addAttribute("resultList", resultList);
            }
            catch (Exception e) {
                model.addAttribute("returnMessage", "fail");
            }
        }
        else {
            model.addAttribute("returnMessage", "fail");
        }

        return "jsonView";
    }  
}
