package kr.go.smplatform.itsm.srvcrspons.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsFormVO;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class SrvcRsponsSiteController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SrvcRsponsMngrController.class);
    
    @Resource(name = "srvcRsponsService")
    private SrvcRsponsService srvcRsponsService;
    
    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;

    @Resource(name = "userService")
    private UserService userService;
    
    /**
     * SR요청, 요청목록을 조회한다. (pageing)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrieveSrReqList.do")
    public String retrieveSrReqList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
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

        if ("kftd0007".equals(loginUserVO.getUserId())) {
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrReqList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(userTyCode);
        
        final int totalCount = srvcRsponsService.retrieveSrReqPagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/srlist";
    }
    
    /**
     * SR접수, 목록을 조회한다. (pageing)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrieveSrRcvList.do")
    public String retrieveSrRcvList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
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

        if ("kftd0007".equals(loginUserVO.getUserId())) {
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrRcvList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        srvcRsponsFormVO.getSrvcRsponsVO().setUserTyCode(userTyCode);
        
        final int totalCount = srvcRsponsService.retrieveSrRcvPagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/srcvlist";
    }
     
    
    /**
     * SR요청접수, 목록을 조회한다. (pageing)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrievePagingList.do")
    public String retrievePagingList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
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

        if ("kftd0007".equals(loginUserVO.getUserId())) {
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrievePagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/recvlist";
    }
    
   /**
     * SR요청, 목록을 조회한다. (요청자별)
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrieveList.do")
    public String retrieveList(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
////        UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
////        
////        srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
////        
////        List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsFormVO.getSearchSrvcRsponsVO());
////        model.addAttribute("resultList", list);
//        retrieveCmmnCode(model);
//        
//        return "/itsm/srvcrspons/site/stateList";

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

        //
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

//        if ("kftd0007".equals(loginUserVO.getUserId())) {
////            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
//        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrievePagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/list";
    
    
    }
    
    /**
     * SR요청 목록을 조회한다. (시스템 담당자별)
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 조회할 정보가 담긴 UserVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
//    public String retrieveListDiv(SrvcRsponsVO srvcRsponsVO, HttpSession session, ModelMap model) throws Exception {
 
    @RequestMapping(value="/srvcrspons/site/retrieveList2.do")
    public String retrieveList2(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
 
//    	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
//        
//        if (UserVO.USER_TY_CODE_CNSLT.equals(loginUserVO.getUserTyCode())
//                || UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
//            srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
//        }
//        
//        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsVO);
//
//        model.addAttribute("resultList", list);
//        
//        return "/itsm/srvcrspons/site/stateListDiv";
        
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

        if ("kftd0007".equals(loginUserVO.getUserId())) {
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrievePagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/list";
        
    }

    @RequestMapping(value="/srvcrspons/site/retrieveListDiv.do")
    public String retrieveListDiv(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
 
//    	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
//        
//        if (UserVO.USER_TY_CODE_CNSLT.equals(loginUserVO.getUserTyCode())
//                || UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
//            srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
//        }
//        
//        final List<SrvcRsponsVO> list = srvcRsponsService.retrieveList(srvcRsponsVO);
//
//        model.addAttribute("resultList", list);
//        
//        return "/itsm/srvcrspons/site/stateListDiv";
        
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

        if ("kftd0007".equals(loginUserVO.getUserId())) {
//            srvcRsponsFormVO.getSearchSrvcRsponsVO().setRqesterId(loginUserVO.getUserId());
            srvcRsponsFormVO.getSearchSrvcRsponsVO().setTrgetSrvcCode("A035");
        }

        final String userTyCode = loginUserVO.getUserTyCode();
        final String userId = loginUserVO.getUserId();
        final SrvcRsponsVO searchSrvcRsponsVO = srvcRsponsFormVO.getSearchSrvcRsponsVO();

        searchSrvcRsponsVO.setUserTyCode(userTyCode);
        searchSrvcRsponsVO.setUserId(userId);

        final List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(searchSrvcRsponsVO);
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        final int totalCount = srvcRsponsService.retrievePagingListCnt(searchSrvcRsponsVO);
        paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/srvcrspons/site/list";
        
    }

    /**
     * @param model
     * @throws Exception
     * 
     * SRVC_RSPONS_BASIS_CODE = "A0"; //SR_근거_코드 요청 범주
     * SRVC_RSPONS_CL_CODE = "A2"; //SR_분류_코드 SR 구분 요청 구분
     * TRGET_SRVC_CODE = "S1"; //대상_서비스_코드, 변경 분류
     * TRGET_SRVC_DETAIL_CODE = "S3"; //대상_서비스_코드, 변경 유형
     * 
     */
    private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("trgetSrvcCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.TRGET_SRVC_CODE)
        ));
        model.addAttribute("trgetSrvcDetailCodeVOList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.TRGET_SRVC_DETAIL_CODE)
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
     * SR요청 등록 화면을 조회한다.
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/site/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/createView.do")
    public String createView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
        final Date currentDate = new Date();
        
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -3));
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setRqesterNm(loginUserVO.getUserNm());
        srvcRsponsVO.setRqesterPsitn(loginUserVO.getPsitn());
        srvcRsponsVO.setRqesterCttpc(loginUserVO.getMoblphon());
        srvcRsponsVO.setRqesterEmail(loginUserVO.getEmail());

        // srvcRsponsBasisCode
        srvcRsponsVO.setSrvcRsponsBasisCode(SrvcRsponsVO.SRVC_RSPONS_BASIS_CODE_S306);
        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO); 
        retrieveCmmnCode(model);
        
        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);

        return "/itsm/srvcrspons/site/edit";
    }
    
    @RequestMapping("/srvcrspons/site/createSrView.do")
    public String createSrView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
        final Date currentDate = new Date();
        
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, 0));
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setRqesterNm(loginUserVO.getUserNm());
        srvcRsponsVO.setRqesterPsitn(loginUserVO.getPsitn());
        srvcRsponsVO.setRqesterCttpc(loginUserVO.getMoblphon());
        srvcRsponsVO.setRqesterEmail(loginUserVO.getEmail());

        // srvcRsponsBasisCode
        srvcRsponsVO.setSrvcRsponsBasisCode(SrvcRsponsVO.SRVC_RSPONS_BASIS_CODE_S306);
        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO); 
        retrieveCmmnCode(model);
        
        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);

        return "/itsm/srvcrspons/site/sredit";
    }

    @RequestMapping("/srvcrspons/site/createSrcvView.do")
    public String createSrcvView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = new SrvcRsponsVO();
        final Date currentDate = new Date();
        
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -3));
        
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setRequstAtchmnflId(UUID.randomUUID().toString());
        srvcRsponsVO.setRsponsAtchmnflId(UUID.randomUUID().toString());
        
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setRqesterNm(loginUserVO.getUserNm());
        srvcRsponsVO.setRqesterPsitn(loginUserVO.getPsitn());
        srvcRsponsVO.setRqesterCttpc(loginUserVO.getMoblphon());
        srvcRsponsVO.setRqesterEmail(loginUserVO.getEmail());

        // srvcRsponsBasisCode
        srvcRsponsVO.setSrvcRsponsBasisCode(SrvcRsponsVO.SRVC_RSPONS_BASIS_CODE_S306);
        srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO); 
        retrieveCmmnCode(model);
        
        // 중복등록 방지 처리
        final String saveToken = ITSMDefine.generateSaveToken(session);
        srvcRsponsFormVO.getSrvcRsponsVO().setSaveToken(saveToken);

        return "/itsm/srvcrspons/site/srcvedit";
    }

    /**
     * SR요청을 등록한다.
     * @param userVO - 등록할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param status
     * @return "forward:/user/site/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/create.do")
    public String create(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        // Server-Side Validation
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
//            model.addAttribute("userVO", srvcRsponsFormVO.getUserVO());
            return "/itsm/srvcrspons/site/edit";
        }

        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        srvcRsponsVO.setSrvcRsponsSj(
                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
        );
        
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, srvcRsponsVO.getSaveToken())) {
            if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
                return "forward:/srvcrspons/site/retrievePagingList.do";
            }
            else {
                return "forward:/srvcrspons/site/retrieveList.do";
            }
        }
        
        // session에서 로그인 정보를 가져온다.
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final Date currentDate = new Date();
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -3));
        
//        srvcRsponsFormVO.getSrvcRsponsVO().makeRequstDt();
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        
        try {
            srvcRsponsService.create(srvcRsponsVO);
        }
        catch (Exception e) {
            LOGGER.error("신규 등록 중 오류 발생");
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "등록중 오류가 발생되었습니다. 관리자에게 문의하세요.");
        }
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrievePagingList.do";
        }

        return "forward:/srvcrspons/site/retrieveList.do";
    }

    //SR요청을 등록한다.
    @RequestMapping("/srvcrspons/site/createsr.do")
    public String createsr(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        // Server-Side Validation
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
//            model.addAttribute("userVO", srvcRsponsFormVO.getUserVO());
            return "/itsm/srvcrspons/site/sredit";
        }

        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        srvcRsponsVO.setSrvcRsponsSj(
                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
        );
        
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, srvcRsponsVO.getSaveToken())) {
            if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
                return "forward:/srvcrspons/site/retrieveSrReqList.do";
            }
            else {
                return "forward:/srvcrspons/site/retrieveSrReqList.do";
            }
        }
        
        // session에서 로그인 정보를 가져온다.
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final Date currentDate = new Date();
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -3));
        
//        srvcRsponsFormVO.getSrvcRsponsVO().makeRequstDt();
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        
        try {
            srvcRsponsService.create(srvcRsponsVO);
        }
        catch (Exception e) {
            LOGGER.error("신규 등록 중 오류 발생");
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "등록중 오류가 발생되었습니다. 관리자에게 문의하세요.");
        }
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrieveSrReqList.do";
        }

        return "forward:/srvcrspons/site/retrieveSrReqList.do";
    }

    //SR요청을 재등록.
    @RequestMapping("/srvcrspons/site/createsrre.do")
    public String createsrre(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        // Server-Side Validation
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
//            model.addAttribute("userVO", srvcRsponsFormVO.getUserVO());
            return "/itsm/srvcrspons/site/sredit";
        }

        final SrvcRsponsVO srvcRsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        srvcRsponsVO.setSrvcRsponsSj(
                srvcRsponsVO.getSrvcRsponsSj().substring(0, Math.min(srvcRsponsVO.getSrvcRsponsSj().length(), 40))
        );
        
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, srvcRsponsVO.getSaveToken())) {
            if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
                return "forward:/srvcrspons/site/retrieveSrReqList.do";
            }
            else {
                return "forward:/srvcrspons/site/retrieveSrReqList.do";
            }
        }
        
        // session에서 로그인 정보를 가져온다.
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final Date currentDate = new Date();
        srvcRsponsVO.setRequstDt(DateUtils.addMinutes(currentDate, -3));
        
//        srvcRsponsFormVO.getSrvcRsponsVO().makeRequstDt();
        srvcRsponsVO.setRqesterId(loginUserVO.getUserId());
        srvcRsponsVO.setCreatId(loginUserVO.getUserId());
        srvcRsponsVO.setUpdtId(loginUserVO.getUserId());
        
        try {
            // 재요철일 등록
        	srvcRsponsService.updateSrEvReRequest(srvcRsponsVO);
        	// 재요청된 SR 등록
            srvcRsponsService.createSrReRequest(srvcRsponsVO);
        }
        catch (Exception e) {
            LOGGER.error("신규 등록 중 오류 발생");
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "등록중 오류가 발생되었습니다. 관리자에게 문의하세요.");
        }
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrieveSrReqList.do";
        }

        return "forward:/srvcrspons/site/retrieveSrReqList.do";
    }

    
    /**
     * SR요청 수정화면을 조회한다.
     * @param id - 수정할 SR요청 id
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/site/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/updateView.do")
    public String updateView(SrvcRsponsFormVO srvcRsponsFormVO, ModelMap model) throws Exception {
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
        
        //접수 후부터 수정 불가, 본인글만 수정, 아니면 보기만
        if (srvcRsponsVO.getRspons1stDt() == null) {
            return "/itsm/srvcrspons/site/edit";
        }

        return "/itsm/srvcrspons/site/view";
    }

    @RequestMapping("/srvcrspons/site/updateSrView.do")
    public String updateSrView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
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

        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        //접수 후부터 수정 불가, 본인글만 수정, 아니면 보기만
        if(loginUserVO.getUserId().equals(srvcRsponsFormVO.getSrvcRsponsVO().getRqesterId())) {
            if (srvcRsponsVO.getRspons1stDt() == null) {
                return "/itsm/srvcrspons/site/sredit";
            } else 
            	return "/itsm/srvcrspons/site/srview";
        }

        return "/itsm/srvcrspons/site/srview";
    }


    /**
     * SR요청을 수정한다.
     * @param userVO - 수정할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/site/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/update.do")
    public String update(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO rsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        rsponsVO.setSrvcRsponsSj(
                rsponsVO.getSrvcRsponsSj().substring(0, Math.min(rsponsVO.getSrvcRsponsSj().length(), 40))
        );

        LOGGER.debug("srvcRsponsVO: " + rsponsVO);
        
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
            srvcRsponsFormVO.setSrvcRsponsVO(rsponsVO);
            return "/itsm/srvcrspons/site/edit";
        }
        
        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(rsponsVO);
        
        if (srvcRsponsVO != null
                && srvcRsponsVO.getRspons1stDt() != null) {
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");
            return "forward:/srvcrspons/site/updateView.do";
        }
        
        final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        
        rsponsVO.makeRequstDt();
        rsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsService.updateRequst(rsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrievePagingList.do";
        }

        return "forward:/srvcrspons/site/retrieveList.do";
    }

    //SR요청을 수정한다.
    @RequestMapping("/srvcrspons/site/updatesr.do")
    public String updatesr(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO rsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        rsponsVO.setSrvcRsponsSj(
                rsponsVO.getSrvcRsponsSj().substring(0, Math.min(rsponsVO.getSrvcRsponsSj().length(), 40))
        );

        LOGGER.debug("srvcRsponsVO: " + rsponsVO);
        
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
            srvcRsponsFormVO.setSrvcRsponsVO(rsponsVO);
            return "/itsm/srvcrspons/site/sredit";
        }
        
        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(rsponsVO);
        
        if (srvcRsponsVO != null
                && srvcRsponsVO.getRspons1stDt() != null) {
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");
            return "forward:/srvcrspons/site/updateSrView.do";
        }
        
        final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        
        rsponsVO.makeRequstDt();
        rsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsService.updateRequst(rsponsVO);
        
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrieveSrReqList.do";
        }

        return "forward:/srvcrspons/site/retrieveSrReqList.do";
    }

    @RequestMapping("/srvcrspons/site/srupdate.do")
    public String srupdate(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO rsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

        // 제목 40자 이내로 자르기
        rsponsVO.setSrvcRsponsSj(
                rsponsVO.getSrvcRsponsSj().substring(0, Math.min(rsponsVO.getSrvcRsponsSj().length(), 40))
        );

        LOGGER.debug("srvcRsponsVO: " + rsponsVO);
        
        beanValidator.validate(srvcRsponsFormVO, bindingResult);
        
        if (bindingResult.hasErrors()) {
            srvcRsponsFormVO.setSrvcRsponsVO(rsponsVO);
            return "/itsm/srvcrspons/site/sredit";
        }
        
        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(rsponsVO);
        
        if (srvcRsponsVO != null
                && srvcRsponsVO.getRspons1stDt() != null) {
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");
            return "forward:/srvcrspons/site/updateSrView.do";
        }
        
        final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        
        rsponsVO.makeRequstDt();
        rsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsService.updateRequst(rsponsVO);
        
//        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
//            return "forward:/itsm/srvcrspons/site/retrievePagingList.do";
//        }

        return "forward:/srvcrspons/site/retrieveSrReqList.do";
    }
    
    //SR접수 접수자, 접수일
    @RequestMapping("/srvcrspons/site/updatesrcv.do")
    public String updatesrcv(HttpSession session, SrvcRsponsFormVO srvcRsponsFormVO, BindingResult bindingResult) throws Exception {
        final SrvcRsponsVO rsponsVO = srvcRsponsFormVO.getSrvcRsponsVO();

//        // 제목 40자 이내로 자르기
//        rsponsVO.setSrvcRsponsSj(
//                rsponsVO.getSrvcRsponsSj().substring(0, Math.min(rsponsVO.getSrvcRsponsSj().length(), 40))
//        );

        LOGGER.debug("srvcRsponsVO: " + rsponsVO);
        
//        beanValidator.validate(srvcRsponsFormVO, bindingResult);
//        
//        if (bindingResult.hasErrors()) {
//            srvcRsponsFormVO.setSrvcRsponsVO(rsponsVO);
//            return "/itsm/srvcrspons/site/srcvedit";
//        }
        
//        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
//        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(rsponsVO);
//        
//        if (srvcRsponsVO != null
//                && srvcRsponsVO.getRspons1stDt() != null) {
//            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");
//            return "forward:/itsm/srvcrspons/site/updateSrView.do";
//        }
        final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
        
        CmmnCodeVO ccde = new CmmnCodeVO();
        ccde.setCmmnCode(rsponsVO.getTrgetSrvcCode());
        CmmnCodeVO ccder = cmmnCodeService.retrieve(ccde);
        if("Z1".equals(ccder.getCmmnCodeSubNm1())) 
        	rsponsVO.setVerifyYn("Y");
        else rsponsVO.setVerifyYn("N");
        rsponsVO.makeRspons1stDt();
        rsponsVO.setUpdtId(loginUserVO.getUserId());
        srvcRsponsService.updateReceive(rsponsVO);
        
        // 중복등록 방지 처리
        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrieveSrRcvList.do";
        }

        return "forward:/srvcrspons/site/retrieveSrRcvList.do";
    }

    
    /**
     * SR요청을 삭제한다.
     * @param userVO - 삭제할 정보가 담긴 VO
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @return "forward:/user/site/retrievePagingList.do"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/delete.do")
    public String delete(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsFormVO.getSrvcRsponsVO().setUpdtId(loginUserVO.getUserId());
        
        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        
        if (srvcRsponsVO != null
                && srvcRsponsVO.getRspons1stDt() != null) {
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");

            return "forward:/srvcrspons/site/updateView.do";
        }
        
        LOGGER.info("userVO: "+srvcRsponsFormVO.getSrvcRsponsVO());
        srvcRsponsService.delete(srvcRsponsFormVO.getSrvcRsponsVO());
//        status.setComplete();

        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
            return "forward:/srvcrspons/site/retrievePagingList.do";
        }

        return "forward:/srvcrspons/site/retrieveList.do";
    }

    @RequestMapping("/srvcrspons/site/deletesr.do")
    public String deletesr(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        srvcRsponsFormVO.getSrvcRsponsVO().setUpdtId(loginUserVO.getUserId());
        
        // 업데이트 수행전 이미 1차 접수처리했는지 확인 필요
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        
        if (srvcRsponsVO != null
                && srvcRsponsVO.getRspons1stDt() != null) {
            session.setAttribute(ITSMDefine.ERROR_MESSAGE, "운영팀에 의해 접수된 SR은 수정할 수 없습니다.");

            return "forward:/srvcrspons/site/updateSrView.do";
        }
        
        LOGGER.info("userVO: "+srvcRsponsFormVO.getSrvcRsponsVO());
        srvcRsponsService.delete(srvcRsponsFormVO.getSrvcRsponsVO());
//        status.setComplete();

//        if (GenericValidator.isBlankOrNull(srvcRsponsFormVO.getReturnListMode())) {
//            return "forward:/itsm/srvcrspons/site/retrievePagingList.do";
//        }

        return "forward:/srvcrspons/site/retrieveSrReqList.do";
    }
 
    /**
     * SR요청 접수화면.
     * @param id - 수정할 SR요청 id
     * @param srvcRsponsFormVO.getSearchSrvcRsponsVO() - 목록 조회조건 정보가 담긴 VO
     * @param model
     * @return "/user/site/egovSampleRegister"
     * @exception Exception
     */
    @RequestMapping("/srvcrspons/site/updateSrcvView.do")
    public String updateSrcvView(SrvcRsponsFormVO srvcRsponsFormVO, HttpSession session, ModelMap model) throws Exception {
        final SrvcRsponsVO srvcRsponsVO = srvcRsponsService.retrieve(srvcRsponsFormVO.getSrvcRsponsVO());
        final Date currentDate = new Date();
        
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
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        //접수는 시스템 담당자, 관리자만 가능/그외는 조회만
        if (srvcRsponsVO.getRspons1stDt() == null && !UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
            if(srvcRsponsVO.getCnfrmrUserNm() == null) {
            	srvcRsponsVO.setCnfrmrId(loginUserVO.getUserId());
            	srvcRsponsVO.setCnfrmrUserNm(loginUserVO.getUserNm());
            }
            srvcRsponsVO.setRspons1stDt(DateUtils.addMinutes(currentDate, 0));
            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
            retrieveCmmnCode(model);

        	return "/itsm/srvcrspons/site/srcvedit";
        } else {                                                              
            srvcRsponsFormVO.setSrvcRsponsVO(srvcRsponsVO);
            retrieveCmmnCode(model);
       	
            return "/itsm/srvcrspons/site/srcvview";
        }
    }
    
    
    /**
     * 요청자 정보 목록을 조회한다.
     * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrieveRqesterNmListAjax.do")
    public String retrieveRqesterNmListAjax(SrvcRsponsVO srvcRsponsVO, ModelMap model) {
        if (!GenericValidator.isBlankOrNull(srvcRsponsVO.getRqesterNm())) {
            LOGGER.debug("srvcRsponsVO.getRqesterNm(): " + srvcRsponsVO.getRqesterNm());

            try {
                final List<SrvcRsponsVO> resultList = srvcRsponsService.retrieveRqesterNmList(srvcRsponsVO);

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
    
    /**
     * 요청자 정보 목록을 조회한다.
     * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
     * @param model
     * @return "/user/site/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/srvcrspons/site/retrieveRqester1stNmListAjax.do")
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