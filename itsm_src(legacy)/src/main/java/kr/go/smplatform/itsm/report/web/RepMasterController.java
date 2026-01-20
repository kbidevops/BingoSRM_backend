package kr.go.smplatform.itsm.report.web;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.repAttd.service.RepAttdService;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdFormVO;
import kr.go.smplatform.itsm.repcharger.service.RepChargerService;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.report.service.RepDetailService;
import kr.go.smplatform.itsm.report.service.RepDetailService2;
import kr.go.smplatform.itsm.report.service.RepMasterService;
import kr.go.smplatform.itsm.report.vo.*;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.util.DateUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RepMasterController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepMasterController.class);
    
    @Resource(name = "repMasterService")
    private RepMasterService repMasterService;
    
    @Resource(name = "repDetailService")
    private RepDetailService repDetailService;
    
    @Resource(name = "userService")
    private UserService userService;
    
    @Resource(name = "repAttdService")
    private RepAttdService repAttdService;
    
    @Resource(name = "repChargerService")
    private RepChargerService repChargerService;

    @Resource(name = "repDetailService2")
    private RepDetailService2 repDetailService2;

    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;
    
    /**
     * 보고서 목록 조회
     * @param repFormVO
     * @param model
     * @return "/itsm/report/mngr/repListMaster"
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/retrievePagingList.do")
    public String dailyRepList(HttpSession session, String repTyCode, RepFormVO repFormVO, ModelMap model) throws Exception {
        // 고객사 담당자일 경우
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO.getUserTyCode().equals(UserVO.USER_TY_CODE_CSTMR)) {
            repFormVO.getRepMasterVO().setConfirmUsr(loginUserVO.getUserId());
            model.addAttribute("loginUserId", loginUserVO.getUserId());
        }
        
        /** EgovPropertyService.sample */
        repFormVO.getSearchMasterVO().setPageUnit(propertiesService.getInt("pageUnit"));
        repFormVO.getSearchMasterVO().setPageSize(propertiesService.getInt("pageSize"));
        
        /** paging setting */
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(repFormVO.getSearchMasterVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(repFormVO.getSearchMasterVO().getRecordCountPerPage());
        paginationInfo.setPageSize(repFormVO.getSearchMasterVO().getPageSize());
        
        repFormVO.getSearchMasterVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        repFormVO.getSearchMasterVO().setLastIndex(paginationInfo.getLastRecordIndex());
        repFormVO.getSearchMasterVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
        repFormVO.getSearchMasterVO().setDeleteYn("N");
        
        paginationInfo.setTotalRecordCount(repMasterService.totCnt(repFormVO.getSearchMasterVO()));
        
        if (repTyCode != null) {
            repFormVO.getSearchMasterVO().setRepTyCode(repTyCode);
        }
         
        final List<RepMasterVO> list = repMasterService.retrievePagingList(repFormVO.getSearchMasterVO());
        
        retrieveCmmnCode(model);
        
        model.addAttribute("resultList", list);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/report/master/mngr/list";
    }
    
    private void retrieveCmmnCode(ModelMap model) throws Exception {
        // 마스터보고서 상태 코드 불러오기
        model.addAttribute("sttusCodeList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.REP_STTUS_CODE)
        ));

        //마스터보고서 구분 코드 불러오기
        model.addAttribute("repTyCodes", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.REP_TY_CODE)
        ));
        
        model.addAttribute("locateList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.LOCATE_CODE)
        ));
        
        model.addAttribute("repAttdList", cmmnCodeService.retrieveList(
                new CmmnCodeVO(CmmnCodeVO.REP_ATTD_CODE)
        ));
    } 
    
    /**
     * 보고서 수정 화면
     * @param repFormVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/updateView.do")
    public String updateView(RepFormVO repFormVO, RepAttdFormVO repAttdFormVO, ModelMap model) throws Exception {
        if (repFormVO.getRepMasterVO().getRepSn() == 0) {
            return "forward:/itsm/rep/master/mngr/retrievePagingList.do";
        }
        
        retrieveCmmnCode(model);

        // repMaster retreive 위한 VO
        final RepMasterVO retrieveMaster = new RepMasterVO();
        retrieveMaster.setRepSn(repFormVO.getRepMasterVO().getRepSn());
        retrieveMaster.setRepTyCode(repFormVO.getRepMasterVO().getRepTyCode());
        
        final RepDetailVO repDetailVO = repFormVO.getRepDetailVO();
        repDetailVO.setRepSn(repFormVO.getRepMasterVO().getRepSn());
        repDetailVO.setRepTyCode(repFormVO.getRepMasterVO().getRepTyCode());
        repDetailVO.setDeleteYn("N");
        
        repFormVO.setRepMasterVO(repMasterService.retrieve(retrieveMaster));
            
        // 미작성분 포함 화면에 전체 detialVO 표시 하기
        final List<RepDetailVO> detailVOList = repDetailService.retrieveList(repDetailVO);

        final String repTyCode = repFormVO.getRepMasterVO().getRepTyCode();
        final List<RepChargerVO> repChargerList = repChargerService.retrieveAssignList(new RepChargerVO())
                .stream().filter(vo ->
                        // 일일보고면 XX1X 코드인 것만
                        (RepMasterVO.REP_TY_CODE_DAILY.equals(repTyCode) && vo.getSysCode().charAt(2) == '1')
                        // 주간보고면 XX2X 코드인 것만
                        || (RepMasterVO.REP_TY_CODE_WEEKLY.equals(repTyCode) && vo.getSysCode().charAt(2) == '2')
                        // 월간보고면 XX3X 코드인 것만
                        || (RepMasterVO.REP_TY_CODE_MONTHLY.equals(repTyCode) && vo.getSysCode().charAt(2) == '3')
                ).collect(Collectors.toList());
        
        //특수문자 처리
        String execDesc = "";
        String planDesc = "";

        for (int i = 0; i < detailVOList.size(); i++) {
            execDesc = detailVOList.get(i).getExecDesc();
            planDesc = detailVOList.get(i).getPlanDesc();
            detailVOList.get(i).setExecDesc(StringEscapeUtils.unescapeHtml4(execDesc));
            detailVOList.get(i).setPlanDesc(StringEscapeUtils.unescapeHtml4(planDesc));
        }
        
        boolean returnMessage = false;

        for (RepChargerVO chargerVO : repChargerList) {
            boolean exist = false; // 미작성
            
            for(RepDetailVO detailVO : detailVOList) { // 확인
                if(chargerVO.getSysCode().equals(detailVO.getSysCode())) {
                    exist = true; // 작성
                    break;
                }
            }
            
            // 미작성 시 list에 add
            if (!exist) {
                if (!returnMessage) {
                    model.addAttribute("returnMessage", "false");
                    returnMessage = true;
                }

                final RepDetailVO vo = new RepDetailVO();
                vo.setSysCode(chargerVO.getSysCode());
                vo.setUserId(chargerVO.getUserId());
                vo.setsysCodeNm(chargerVO.getSysCodeNm());
                vo.setSysCodeSubNm1(chargerVO.getSysCodeSubNm1());
                vo.setUserNm(chargerVO.getUserNm());
                vo.setCreatId(chargerVO.getUserId());
                vo.setExecDesc("");
                vo.setPlanDesc("");
                vo.setRepSn(repFormVO.getRepMasterVO().getRepSn());
                
                detailVOList.add(vo);
                LOGGER.info(vo.getsysCodeNm() + " ");
            }
        }

        repFormVO.setRepDetailVOList(detailVOList); // 끝
        
        if (repFormVO.getRepMasterVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            // 전체 인원
            final Object locateList = model.get("locateList");
            final List<Integer> personNo = new ArrayList<>();

            if (locateList != null && locateList instanceof List && ((List<?>) locateList).size() > 0 && ((List<?>) locateList).get(0) instanceof CmmnCodeVO) {
                final List<CmmnCodeVO> list = ((List<CmmnCodeVO>) locateList).stream().filter(locate -> {
                    final RepChargerVO repChargerVO = new RepChargerVO();
                    repChargerVO.setUserLocat(locate.getCmmnCode());
                    repChargerVO.setReportCharger(true);
                    try {
                        final int size = repChargerService.retrieveUsers(repChargerVO).size();

                        if (size > 0) {
                            personNo.add(size);
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return false;
                }).collect(Collectors.toList());

                model.addAttribute("locateList", list);
            }
            
            // 금일계획 날짜 세팅
            final Date planDate = repFormVO.getRepMasterVO().getReportDt();
            repAttdFormVO.getPlanAttdVO().setAttdDt(planDate);
                
            // 전일 실적 날짜 계산
            final Calendar cal = Calendar.getInstance();
            cal.setTime(planDate);
            cal.add(Calendar.DATE, -1);

            long date = DateUtil.dateToLong(cal.getTime());

            final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
            
            LOGGER.info("{}", cal.getTime());

            while (DateUtil.isHoliday(date, solar, lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
            
            // 전일 실적 날짜 세팅-
            repAttdFormVO.getExecAttdVO().setAttdDt(cal.getTime());
            
            model.addAttribute("personNo", personNo);
            // attdCodeList1
            model.addAttribute("attdCodeList",cmmnCodeService.retrieveList(
                    new CmmnCodeVO("B2")
            ));
            model.addAttribute("planAttdList",repAttdService.retrieveList(repAttdFormVO.getPlanAttdVO()));
            model.addAttribute("execAttdList",repAttdService.retrieveList(repAttdFormVO.getExecAttdVO()));
            
        }
        else if (repFormVO.getRepMasterVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            // 금주, 차주 범위 구하기
            final Date planDate = repFormVO.getRepMasterVO().getReportDt();
            final Calendar cal = Calendar.getInstance();
            cal.setTime(planDate);
            model.addAttribute("weekOfYear", cal.get(Calendar.WEEK_OF_YEAR));
            
            cal.add(Calendar.DATE, -7);
            
            final SimpleDateFormat weekDateFormat = new SimpleDateFormat("MM.dd");
            final List<String> dayRange = new ArrayList<String>();

            final int[] week = { 2, 6, 2, 6 }; // 월, 금, 월, 금

            for (int i = 0; i < week.length; i++) {
                for (int j = 0; j < 7; j++) {
                    if (cal.get(Calendar.DAY_OF_WEEK) == week[i]) {
                        dayRange.add(weekDateFormat.format(cal.getTime()));
                        break;
                    }

                    cal.add(Calendar.DATE, +1);
                }
            }
            model.addAttribute("dayRange",dayRange);
            
        }
        else if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
            /* 이번달, 다음달 날짜 범위 구하기 */
            final Calendar cal = Calendar.getInstance();
            cal.setTime(repFormVO.getRepMasterVO().getReportDt());

            final int thisMonth = cal.get(Calendar.MONTH) + 1; // 오늘이 어떤 달인지 구하기
            int nextMonth = thisMonth + 1;

            if (nextMonth == 13) {
                nextMonth = 1; // 13이면 1월
            }
            
            final List<String> dayRange = new ArrayList<String>();
            dayRange.add(thisMonth + ".1");
            dayRange.add(thisMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 현재달 말일 구하기
            dayRange.add(nextMonth + ".1");
            cal.add(Calendar.MONTH, 1);
            dayRange.add(nextMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 다음달 말일 구하기
            
            model.addAttribute("month", thisMonth); // 이번 달
            model.addAttribute("dayRange",dayRange); // 날짜 범위 담은 list
        }
        
        // Modal창 담당자 선택
        // 승인된 고객목록
        final UserVO userVO = new UserVO();
        userVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
        userVO.setUserTyCode(userVO.USER_TY_CODE_CSTMR); 
           
        model.addAttribute("cstmrList", userService.retrieveList(userVO));
        final RepChargerVO chargerVO = new RepChargerVO();
        chargerVO.setReportCharger(true);
        model.addAttribute("repChargerList",repChargerService.retrieveUsers(chargerVO));
        
        // 첨부파일 조회
        final List<RepAttachmentNameAndSizeVO> fileList = repMasterService.getAttachments(repFormVO.getRepMasterVO().getRepSn() + "");
        model.addAttribute("fileList", fileList);

        if (repFormVO.getRepMasterVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            if (repFormVO.getRepMasterVO().getSttusCode().equals(RepMasterVO.STTUS_CODE_WRITE)) {
                return "/itsm/report/master/mngr/editWeek1";
               }

            return "/itsm/report/master/mngr/editWeek2";
        }
        else if (repFormVO.getRepMasterVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
            if (repFormVO.getRepMasterVO().getSttusCode().equals(RepMasterVO.STTUS_CODE_WRITE)) {
                return "/itsm/report/master/mngr/editMonth1";
            }

            return "/itsm/report/master/mngr/editMonth2";
        }
        
        return "/itsm/report/master/mngr/edit";
    }

    /**
     * 보고서 수정
     * @param repFormVO
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/update.do")
    public String update(RepFormVO repFormVO, HttpSession session, ModelMap model, String[] fileUserIds, @RequestPart(value = "requiredFile", required = false) List<MultipartFile> requiredFileList, @RequestPart(value = "additionalFile", required = false) List<MultipartFile> additionalFileList) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        repFormVO.getRepMasterVO().setUpdtId(loginUserVO.getUserId());
        
        // detailVO 수정 시작
        final List<RepDetailVO> createVOList = new ArrayList<RepDetailVO>(); // create 시킬 vo 담을 list
        final List<RepDetailVO> detailVOList = repFormVO.getVariableVOList(); // 수정할 vo 담긴  list
        final RepDetailVO vo = new RepDetailVO();
        
        for (int i = 0; i < detailVOList.size(); i++) {
            detailVOList.get(i).setRepSn(repFormVO.getRepMasterVO().getRepSn());
            detailVOList.get(i).setUpdtId(loginUserVO.getUserId());
            
            vo.setRepSn(repFormVO.getRepMasterVO().getRepSn());
            vo.setSysCode(detailVOList.get(i).getSysCode());
            vo.setRepTyCode(repFormVO.getRepMasterVO().getRepTyCode());
            
            vo.setDeleteYn("Y");

            // 지워진 데이터가 있을 때
            if (repDetailService.retrieve(vo) != null) {
                break;
            }

            vo.setDeleteYn("N");

            // 데이터가 없을때
            if (repDetailService.retrieve(vo) == null) {
                final Calendar cal = Calendar.getInstance();

                cal.setTime(repFormVO.getRepMasterVO().getReportDt());

                if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
                    if (cal.get(Calendar.DAY_OF_WEEK) == 2) {
                        cal.add(Calendar.DATE, -3);
                    }
                    else {
                        cal.add(Calendar.DATE, -1);
                    }
                }
                else if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
                    
                }
                
                if (detailVOList.get(i).getExecDesc().isEmpty() && detailVOList.get(i).getPlanDesc().isEmpty()) {
                    continue;
                }

                detailVOList.get(i).setCreatDt(cal.getTime());
                detailVOList.get(i).setCreatId(loginUserVO.getUserId());
                createVOList.add(detailVOList.get(i));
            }
        }

        model.addAttribute("repTyCode", repFormVO.getRepMasterVO().getRepTyCode());
        
        repDetailService.create(createVOList);
        repDetailService.update(repFormVO.getVariableVOList()); // detailVO 수정 끝
        repMasterService.update(repFormVO.getRepMasterVO());

        // 첨부파일 처리
        if (fileUserIds != null) {
            for (int i = 0; i < fileUserIds.length; i++) {
                final String userId = fileUserIds[i];

                final MultipartFile requiredFile = requiredFileList.get(i);
                final MultipartFile additionalFile = additionalFileList.get(i);

                final String requiredFileId;
                if (requiredFile != null && !requiredFile.isEmpty()) {
                    final AtchmnflVO fileVO = atchmnflService.saveFile(requiredFile);
                    requiredFileId = fileVO.getAtchmnflId();
                } else {
                    requiredFileId = null;
                }

                final String additionalFileId;
                if (additionalFile != null && !additionalFile.isEmpty()) {
                    final AtchmnflVO fileVO = atchmnflService.saveFile(additionalFile);
                    additionalFileId = fileVO.getAtchmnflId();
                } else {
                    additionalFileId = null;
                }

                final CRRepAttachmentVO vo2 = CRRepAttachmentVO.Builder
                        .aCRRepAttachmentVO()
                        .requiredFile(requiredFileId)
                        .additionalFile(additionalFileId)
                        .repSn(vo.getRepSn() + "")
                        .userId(userId)
                        .build();

                repDetailService2.updateAttachment(vo2);
            }
        }

        return "redirect:/itsm/rep/master/mngr/retrievePagingList.do";
    }

    /**
     * 보고서 삭제
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/delete.do")
    public String delete(RepFormVO repFormVO) throws Exception {
        // 마스터 보고서 삭제
        repMasterService.delete(repFormVO.getRepMasterVO());
        
        // 딸린 디테일 보고서 삭제
        repFormVO.getRepDetailVO().setRepSn(repFormVO.getRepMasterVO().getRepSn());
        repFormVO.setRepDetailVOList(repDetailService.retrieveList(repFormVO.getRepDetailVO()));
        repDetailService.delete(repFormVO.getRepDetailVOList());
        
        return "forward:/itsm/rep/master/mngr/retrievePagingList.do";
    }
    
    /**
     * 중복검증
     * @param model
     * @param repMasterVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/retrieveAjax.do")
    private String retrieveRepMasterAjax(ModelMap model, RepMasterVO repMasterVO) throws Exception {
        final RepMasterVO retrieveMasterVO = repMasterService.retrieve(repMasterVO);
        
        if (retrieveMasterVO == null) {
            model.addAttribute("returnMessage", "Y");
        }
        else {
            model.addAttribute("returnMessage", "N");
        }
        
        return "jsonView";
    }

    /**
     * 마스터 보고서 확정
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/confirmRepMaster.do")
    private String confirmRepMasterAjax(RepFormVO repFormVO, String[] fileUserIds, @RequestPart(value = "requiredFile", required = false) List<MultipartFile> requiredFileList, @RequestPart(value = "additionalFile", required = false) List<MultipartFile> additionalFileList) throws Exception {
        final String sttusCode = repFormVO.getRepMasterVO().getSttusCode();
        final int repSn = repFormVO.getRepMasterVO().getRepSn();

        if (sttusCode.equals(RepMasterVO.STTUS_CODE_WRITE)) {
            final String confirmUsr = repFormVO.getRepMasterVO().getConfirmUsr();
            repFormVO.getRepMasterVO().setConfirmUsr(confirmUsr);
            repFormVO.getRepMasterVO().setReturnResn(" ");
            repFormVO.getRepMasterVO().setSttusCode(RepMasterVO.STTUS_CODE_CONFIRMING);
        }
        else if (sttusCode.equals(RepMasterVO.STTUS_CODE_CONFIRMING)) {
            repFormVO.getRepMasterVO().setConfirmUsr(null);
            repFormVO.getRepMasterVO().setSttusCode(RepMasterVO.STTUS_CODE_CONFIRM);
        }
        
        repMasterService.update(repFormVO.getRepMasterVO());
        repFormVO.setRepMasterVO(null);

        // 첨부파일 처리
        if (fileUserIds != null) {
            for (int i = 0; i < fileUserIds.length; i++) {
                final String userId = fileUserIds[i];

                final MultipartFile requiredFile = requiredFileList.get(i);
                final MultipartFile additionalFile = additionalFileList.get(i);

                final String requiredFileId;
                if (requiredFile != null && !requiredFile.isEmpty()) {
                    final AtchmnflVO fileVO = atchmnflService.saveFile(requiredFile);
                    requiredFileId = fileVO.getAtchmnflId();
                } else {
                    requiredFileId = null;
                }

                final String additionalFileId;
                if (additionalFile != null && !additionalFile.isEmpty()) {
                    final AtchmnflVO fileVO = atchmnflService.saveFile(additionalFile);
                    additionalFileId = fileVO.getAtchmnflId();
                } else {
                    additionalFileId = null;
                }

                final CRRepAttachmentVO vo2 = CRRepAttachmentVO.Builder
                        .aCRRepAttachmentVO()
                        .requiredFile(requiredFileId)
                        .additionalFile(additionalFileId)
                        .repSn(repSn + "")
                        .userId(userId)
                        .build();

                repDetailService2.updateAttachment(vo2);
            }
        }

        return "redirect:/itsm/rep/master/mngr/retrievePagingList.do";
    }
    
    /**
     * 반려
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/returnRepMaster.do")
    private String reutrn(RepFormVO repFormVO) throws Exception {
        repFormVO.getRepMasterVO().setConfirmUsr("");
        repFormVO.getRepMasterVO().setSttusCode(RepMasterVO.STTUS_CODE_RETURN);
        repMasterService.update(repFormVO.getRepMasterVO());
        
        return "redirect:/itsm/rep/master/mngr/retrievePagingList.do";
    }

    /**
     * 반려된 보고서 재작성
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/writeRepMaster.do")
    private String write(RepFormVO repFormVO) throws Exception {
        repFormVO.getRepMasterVO().setConfirmUsr("");
        repFormVO.getRepMasterVO().setSttusCode(RepMasterVO.STTUS_CODE_WRITE);
        repMasterService.update(repFormVO.getRepMasterVO());
        
        return "redirect:/itsm/rep/master/mngr/retrievePagingList.do";
    }

    /**
     * 확정 취소
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/master/mngr/cancelConfirmAjax.do", method = RequestMethod.POST)
    private String cancelConfirmAjax(String repSn, Model model, HttpSession session) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO == null) {
            model.addAttribute("result", false);
            model.addAttribute("message", "로그인 해주시기 바랍니다");

            return "jsonView";
        }

        try {
            final RepMasterVO vo = new RepMasterVO();
            vo.setRepSn(Integer.parseInt(repSn));
            vo.setUpdtId(loginUserVO.getUserId());
            vo.setSttusCode(RepMasterVO.STTUS_CODE_CONFIRMING);

            repMasterService.update(vo);
        }
        catch (NumberFormatException e) {
            model.addAttribute("result", false);
            model.addAttribute("message", "올바른 보고서 번호를 입력해주세요");

            return "jsonView";
        }

        model.addAttribute("result", true);

        return "jsonView";
    }
}
