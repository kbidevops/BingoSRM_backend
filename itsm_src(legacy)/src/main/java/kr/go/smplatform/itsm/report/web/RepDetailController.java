package kr.go.smplatform.itsm.report.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import kr.go.smplatform.itsm.report.service.RepDetailService2;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers.CalendarDeserializer;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.repAttd.service.RepAttdService;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdFormVO;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;
import kr.go.smplatform.itsm.repcharger.service.RepChargerService;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.report.service.RepDetailService;
import kr.go.smplatform.itsm.report.service.RepMasterService;
import kr.go.smplatform.itsm.report.vo.RepDetailVO;
import kr.go.smplatform.itsm.report.vo.RepFormVO;
import kr.go.smplatform.itsm.report.vo.RepMasterVO;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.util.DateUtil;

@Controller
public class RepDetailController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepDetailController.class);
    
    @Resource(name = "repDetailService")
    private RepDetailService repDetailService;

    @Resource(name = "repDetailService2")
    private RepDetailService2 repDetailService2;
    
    @Resource(name ="repMasterService")
    private RepMasterService repMasterService;
    
    @Resource(name = "repChargerService")
    private RepChargerService repChargerService;
    
    @Resource(name = "repAttdService")
    private RepAttdService repAttdService;
    
    
    /**
     * 보고서 목록 조회
     * @param repFormVO
     * @param model
     * @return "/itsm/report/mngr/repListDetail"
     * @throws Exception
     */
    @RequestMapping(value = "/itsm/rep/detail/mngr/retrievePagingList.do")
    public String dailyRepList(RepFormVO repFormVO, String repTyCode, HttpSession session, ModelMap model) throws Exception {
        final UserVO login_user_vo = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        
        /** EgovPropertyService.sample */
        repFormVO.getSearchDetailVO().setPageUnit(propertiesService.getInt("pageUnit"));
        repFormVO.getSearchDetailVO().setPageSize(propertiesService.getInt("pageSize"));
        
        final PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(repFormVO.getSearchDetailVO().getPageIndex());
        paginationInfo.setRecordCountPerPage(repFormVO.getSearchDetailVO().getRecordCountPerPage());
        paginationInfo.setPageSize(repFormVO.getSearchDetailVO().getPageSize());
        
        repFormVO.getSearchDetailVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
        repFormVO.getSearchDetailVO().setLastIndex(paginationInfo.getLastRecordIndex());
        repFormVO.getSearchDetailVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
        repFormVO.getSearchDetailVO().setDeleteYn("N");
        
        if (repTyCode != null) {
            repFormVO.getSearchDetailVO().setRepTyCode(repTyCode);
        }
        
        if (repFormVO.getSearchDetailVO().getUserId() == null) {
            repFormVO.getSearchDetailVO().setUserId(login_user_vo.getUserId());
        }
        
        final int totalCount = repDetailService.retrieveTotalCnt(repFormVO.getSearchDetailVO());
        paginationInfo.setTotalRecordCount(totalCount);
        
        final List<RepDetailVO> list = repDetailService.retrievePagingList(repFormVO.getSearchDetailVO());
        list.forEach(vo -> {
            try {
                final Date lastWeekday = repDetailService2.getLastWeekday(vo.getReportDt());
                final String reportName = new SimpleDateFormat("yyyy-MM-dd").format(lastWeekday) + " 업무보고서";
                vo.setDailyReportName(reportName);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        final CmmnCodeVO cmmnCodeVo = new CmmnCodeVO(CmmnCodeVO.REP_TY_CODE);

        model.addAttribute("chargerList", repChargerService.retrieveUsers(new RepChargerVO()));
        model.addAttribute("resultList", list);
        model.addAttribute("repTyCodes", cmmnCodeService.retrieveList(cmmnCodeVo));
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/report/detail/mngr/list";
    }

    // 보고서 작성 화면 변경으로 인해 제거
//    /**
//     * 보고서 작성 화면 조회
//     * @param repFormVO - 조회 정보가 담긴 VO
//     * @param session
//     * @param model
//     * @return "/itsm/report/detail/mngr/edit"
//     * @throws Exception 
//     */
//    @RequestMapping(value = "/itsm/rep/detail/mngr/createView.do")
//    public String createView(RepFormVO repFormVO, HttpSession session, ModelMap model) throws Exception {
//        final RepDetailVO repDetailVO = repFormVO.getRepDetailVO();
//        final String repTyCode = repDetailVO.getRepTyCode();
//        repDetailVO.setUserId(null);
//
//        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
//        final RepChargerVO repChargerVO = new RepChargerVO();
//        repChargerVO.setUserId(loginUserVO.getUserId());
//        repChargerVO.setDeleteYn("N");
//
//        final List<RepChargerVO> assignList = repChargerService.retrieveAssignList(repChargerVO)
//                .stream()
//                .filter(vo -> !"B100".equals(vo.getSysCode())) // 헤더(B100)를 작성할 필요가 없음
//                .filter(vo ->
//                        // 일일보고면 XX1X 코드인 것만
//                        (RepMasterVO.REP_TY_CODE_DAILY.equals(repTyCode) && vo.getSysCode().charAt(2) == '1')
//                        // 주간보고면 XX2X 코드인 것만
//                        || (RepMasterVO.REP_TY_CODE_WEEKLY.equals(repTyCode) && vo.getSysCode().charAt(2) == '2')
//                        // 월간보고면 XX3X 코드인 것만
//                        || (RepMasterVO.REP_TY_CODE_MONTHLY.equals(repTyCode) && vo.getSysCode().charAt(2) == '3')
//                )
//                .collect(Collectors.toList());
//
//        final CmmnCodeVO cmmnCodeVo = new CmmnCodeVO();
//        cmmnCodeVo.setCmmnCodeTy("B2");
//        cmmnCodeVo.setDeleteYn("N");
//
//        final List<CmmnCodeVO> attdCodeList = cmmnCodeService.retrieveList(cmmnCodeVo);
//        
//        model.addAttribute("dayRange", getDayRange(model, repTyCode));
//        model.addAttribute("attdCodeList", attdCodeList);
//        model.addAttribute("repDetailVOList", assignList);
//        
//        //중복등록 방지 처리
//        final String saveToken = ITSMDefine.generateSaveToken(session);
//        repDetailVO.setSaveToken(saveToken);
//        
//        if (RepMasterVO.REP_TY_CODE_DAILY.equals(repTyCode)) {
//            return "/itsm/report/detail/mngr/edit";
//        }
//
//        if (RepMasterVO.REP_TY_CODE_WEEKLY.equals(repTyCode)) {
//            return "/itsm/report/detail/mngr/editWeek1";
//        }
//
//        return "/itsm/report/detail/mngr/editMonth1";
//    }
    
    /**
     * 보고서 등록
     * @param session
     * @param repFormVO - 등록할 정보가 담긴 VO
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/itsm/rep/detail/mngr/create.do")
    public String create(HttpSession session, RepAttdFormVO repAttdFormVO, RepFormVO repFormVO, ModelMap model) throws Exception {
        // 중복등록 방지 처리
        if (!ITSMDefine.checkSaveToken(session, repFormVO.getRepDetailVO().getSaveToken())) {
            return "forward:/itsm/rep/detail/mngr/retrievePagingList.do";
        }
        
        // userId setting
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        final String userId = loginUserVO.getUserId();
        repFormVO.getRepDetailVO().setCreatId(userId);
        repAttdFormVO.setCreatId(userId);
        
        repFormVO.getRepDetailVO().setCreatDt(repFormVO.getRepDetailVO().getReportDt());
        LOGGER.info("{}", repFormVO.getRepDetailVO().getCreatDt());
        
        // 마스터보고서 날짜 세팅
        final Calendar cal = Calendar.getInstance();
        cal.setTime(repFormVO.getRepDetailVO().getReportDt());
        
        long date;
        final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
        cmmnCodeVO.setCmmnCodeSubNm1("양력");

        final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);
        cmmnCodeVO.setCmmnCodeSubNm1("음력");

        final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
        
        // 일일보고서 : 보고일인 다음날로
        if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            cal.add(Calendar.DATE, +1);
            date = DateUtil.dateToLong(cal.getTime());
            
            while (DateUtil.isHoliday(date,solar,lunar)) {
                cal.add(Calendar.DATE, +1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }
        // 주간보고서 : 그주 금요일로
        else if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            while (cal.get(cal.DAY_OF_WEEK) != 6) {
                cal.add(Calendar.DATE, +1);
            }

            date = DateUtil.dateToLong(cal.getTime());
            
            while(DateUtil.isHoliday(date,solar,lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }
        //월간보고서 : 그 달의 말일로
        else {
            final int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            if (month == 13) {
                month = 1;
            }
            final int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(year, month, day);
            date = DateUtil.dateToLong(cal.getTime());
            
            while (DateUtil.isHoliday(date,solar,lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }
        
        repFormVO.getRepMasterVO().setReportDt(cal.getTime());
          
        // master 보고서 중복검증 및 생성
        repFormVO.getRepMasterVO().setRepTyCode(repFormVO.getRepDetailVO().getRepTyCode());
        final RepMasterVO retreiveMaster = repMasterService.retrieve(repFormVO.getRepMasterVO());

        if (retreiveMaster == null) {
            // 마스터 보고서가 없으면 새로 만든다.
            repFormVO.getRepMasterVO().setCreatId(userId);
            repMasterService.create(repFormVO.getRepMasterVO());
            
            if (!repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
                // 새로 만든 마스터 보고서 맨 위에 나올 헤더(날짜범위)를 만든다.
                final List<String> dayRange = getDayRange(model, repFormVO.getRepDetailVO().getRepTyCode());
                final RepMasterVO lastInsertedVO = new RepMasterVO();
                lastInsertedVO.setRepTyCode(repFormVO.getRepDetailVO().getRepTyCode());
                
                final RepDetailVO header = new RepDetailVO();
                header.setRepSn(repMasterService.lastInsertedId(lastInsertedVO));
                header.setReportDt(repFormVO.getRepMasterVO().getReportDt());
                header.setCreatId("admin");
                header.setSysCode("B100");
                header.setExecDesc("今週  추진실적 ("+dayRange.get(0)+" ~ "+dayRange.get(1)+")");
                header.setPlanDesc("次週  추진계획 ("+dayRange.get(2)+" ~ "+dayRange.get(3)+")");

                if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
                    header.setExecDesc("今月  추진실적 ("+dayRange.get(0)+" ~ "+dayRange.get(1)+")");
                    header.setPlanDesc("次月  추진계획 ("+dayRange.get(2)+" ~ "+dayRange.get(3)+")");
                }

                final List<RepDetailVO> headerList = new ArrayList<RepDetailVO>();
                headerList.add(header);
                
                repDetailService.create(headerList);
            }
        }
        else {
            repFormVO.getRepMasterVO().setRepSn(retreiveMaster.getRepSn());
        }
        
        final Date repDate = repFormVO.getRepDetailVO().getReportDt(); // get date
        
        // 근태
        if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            repAttdFormVO.getExecAttdVO().setAttdDt(repDate); // set execDate
            repAttdFormVO.setPlanDate(repDate, solar, lunar); // set planDate
            
            // 근태 중복검증
            final List<RepAttdVO> attdVoList = repAttdFormVO.getRepAttdVOList();
            attdVoList.add(repAttdFormVO.getExecAttdVO());
            attdVoList.add(repAttdFormVO.getPlanAttdVO());
            
            final RepAttdVO repVo = new RepAttdVO();
            
            for (RepAttdVO vo : attdVoList) {
                repVo.setAttdDt(vo.getAttdDt());
                repVo.setUserId(vo.getCreatId());

                LOGGER.debug("AttdVO : {}", vo);
                
                if (repAttdService.retrieveList(repVo).size() > 0) {
                    if (vo.getAttdCode().equals("-") || vo.getAttdCode().equals("")) {
                        repAttdService.deleteOne(vo);
                    }
                    else {
                        repAttdService.update(vo);
                    }
                }
                else {
                    // 근태 인서트
                    repAttdService.createOne(vo);
                }
            }
        }
        
        // 업무보고서 중복검증
        for (String sysCode : repFormVO.getSysCodes()) {
            final RepDetailVO repDetailVO = new RepDetailVO();
            repDetailVO.setSysCode(sysCode);
            repDetailVO.setCreatDt(repDate);
            repDetailVO.setDeleteYn("Y");
            repDetailVO.setRepTyCode(repFormVO.getRepMasterVO().getRepTyCode());
            
            LOGGER.info("repdate: " + repDate);
            LOGGER.info("sysCode: " + sysCode);

            RepDetailVO retrieveVO = repDetailService.retrieve(repDetailVO);
            LOGGER.info("retrieveVO: " + retrieveVO);

            if (retrieveVO != null) {
                // VOList 세팅
                final List<RepDetailVO> repDetailVOList = repFormVO.getVariableVOList();

                for (int i = 0; i < repDetailVOList.size(); i++) {
                    repDetailVOList.get(i).setUpdtId(userId);
                    repDetailVOList.get(i).setRepSn(retrieveVO.getRepSn());
                    repDetailVOList.get(i).setDeleteYn("N");
                }

                // update
                repDetailService.update(repDetailVOList);

            }
            else {
                repDetailVO.setDeleteYn("N");
                retrieveVO = repDetailService.retrieve(repDetailVO);

                if (retrieveVO != null) {
                    // VOList 세팅
                    final List<RepDetailVO> repDetailVOList = repFormVO.getVariableVOList();

                    for (int i = 0; i < repDetailVOList.size(); i++) {
                        repDetailVOList.get(i).setUpdtId(userId);
                        repDetailVOList.get(i).setRepSn(retrieveVO.getRepSn());
                        repDetailVOList.get(i).setDeleteYn("N");
                    }

                    // update
                    repDetailService.update(repDetailVOList);
                    break;
                }

                //create
                repDetailService.create(repFormVO.getVariableVOList());
            }
            break;
            /*
            기존 : if와 else 모두 마지막에 무조건 break을 해줌
            변경 : 무조건 break이므로 밖으로 빼냄

            하지만 무조건 break를 하므로 for문을 쓸 이유가 없어보임
            TODO 추후 의도를 파악하여 고칠 것
             */
        }
        
        return "redirect:/itsm/rep/detail/mngr/retrievePagingList.do";
    }
    
    /**
     * 보고서 수정화면 조회
     * @param repFormVO - 수정할 글 정보가 담긴 VO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/rep/detail/mngr/updateView.do")
    public String updateView(RepFormVO repFormVO, RepAttdFormVO repAttdFormVO, HttpSession session, ModelMap model) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO); // 로그인 유저 정보
        String repStatus = RepMasterVO.STTUS_CODE_WRITE; // 보고서 상태 코드

        repFormVO.setRepDetailVOList(repDetailService.retrieveList(repFormVO.getRepDetailVO()));

        if (repFormVO.getRepDetailVOList().size() != 0) {
            CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);

            // 유저 근태 코드 가져오기
            if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(repFormVO.getRepDetailVOList().get(0).getReportDt());
                cal.add(Calendar.DATE, -1);

                long date = DateUtil.dateToLong(cal.getTime());

                while (DateUtil.isHoliday(date,solar,lunar)) {
                    cal.add(Calendar.DATE, -1);
                    date = DateUtil.dateToLong(cal.getTime());
                }

                repAttdFormVO.setCreatId(repFormVO.getRepDetailVO().getUserId());
                repAttdFormVO.getExecAttdVO().setAttdDt(cal.getTime());
                repAttdFormVO.setPlanDate(cal.getTime(), solar, lunar);
            }

            final Calendar cal = Calendar.getInstance();
            cal.setTime(repFormVO.getRepDetailVOList().get(0).getReportDt());

            // 해당 보고서의 보고 주차수
            if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
                cal.setFirstDayOfWeek(Calendar.MONDAY); //주차의 기준을 월요일로 정한다.
                cal.setMinimalDaysInFirstWeek(7); //주차에 포함될 최소 일수는 7일이다.
                model.addAttribute("weekOfYear", cal.get(Calendar.WEEK_OF_YEAR));
            }
            // 해당 보고서의 보고 월
            else if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
                model.addAttribute("month", cal.get(Calendar.MONTH)+1);
            }
        }

        // 전체 근태 코드 가져오기
        final CmmnCodeVO cmmnCodeVo = new CmmnCodeVO("B2");
        final List<CmmnCodeVO> attdCodeList = cmmnCodeService.retrieveList(cmmnCodeVo);

        if (repFormVO.getRepDetailVOList().size() > 0) {
            repStatus = repFormVO.getRepDetailVOList().get(0).getSttusCode();
            model.addAttribute("repStatus", repStatus);
        }

        model.addAttribute("chargerId", repFormVO.getRepDetailVO().getUserId());

        // escape된 문자 unescape(보안상 이유로 Filter때문에 escape되어있음)
        for (int i = 0; i < repFormVO.getRepDetailVOList().size(); i++) {
            final RepDetailVO vo = repFormVO.getRepDetailVOList().get(i);
            repFormVO.getRepDetailVOList().get(i).setExecDesc(StringEscapeUtils.unescapeHtml4(vo.getExecDesc()));
            repFormVO.getRepDetailVOList().get(i).setPlanDesc(StringEscapeUtils.unescapeHtml4(vo.getPlanDesc()));
        }

        model.addAttribute("repDetailVOList", repFormVO.getRepDetailVOList());

        if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            model.addAttribute("attdCodeList", attdCodeList);
            model.addAttribute("execAttdVO", repAttdService.retrieve(repAttdFormVO.getExecAttdVO()));
            model.addAttribute("planAttdVO", repAttdService.retrieve(repAttdFormVO.getPlanAttdVO()));

            return "/itsm/report/detail/mngr/edit";
        }
        else if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            if (repFormVO.getRepDetailVO().getUserId().equals(loginUserVO.getUserId())
                    && repStatus.equals(RepMasterVO.STTUS_CODE_WRITE)) {
                return "/itsm/report/detail/mngr/editWeek1";
            }

            return "/itsm/report/detail/mngr/editWeek2";
        }
        else if(repFormVO.getRepDetailVO().getUserId().equals(loginUserVO.getUserId())
                    && repStatus.equals(RepMasterVO.STTUS_CODE_WRITE)) {
            return "/itsm/report/detail/mngr/editMonth1";
        }

        return "/itsm/report/detail/mngr/editMonth2";
    }
    
    
    /**
     * 보고서 수정
     * @param repFormVO
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/rep/detail/mngr/update.do")
    public String update(RepFormVO repFormVO, RepAttdFormVO repAttdFormVO, HttpSession session, ModelMap model) throws Exception {
        // 로그인 유저정보
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        repFormVO.getRepDetailVO().setUpdtId(loginUserVO.getUserId()); // 수정자 로그인 유저 아이디
        repFormVO.setRegisterFlag("modify");

        // 담당자
        final RepChargerVO repCharger = new RepChargerVO();
        repCharger.setUserId(loginUserVO.getUserId());
        repCharger.setDeleteYn("N");

        // 담당 시스템 리스트
        final List<RepChargerVO> chargerList = repChargerService.retrieveAssignList(repCharger);
        
        // 근태 수정
        if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            Date execDate = repFormVO.getRepDetailVO().getCreatDt(); // update화면에서 수정

            if (execDate == null) {
                execDate = repFormVO.getRepDetailVO().getReportDt(); // create화면에서 수정
                List<Integer> repSn = new ArrayList<Integer>();
                repSn.add(repFormVO.getRepMasterVO().getRepSn());
                
                repFormVO.setRepSns(repSn);
            }
            
            final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
            
            repAttdFormVO.getExecAttdVO().setAttdDt(execDate);
            repAttdFormVO.setCreatId(loginUserVO.getUserId());
            repAttdFormVO.setPlanDate(execDate, solar, lunar);
            repAttdFormVO.setUserLocat(chargerList.get(0).getUserLocat());
            
            // exec update
            if (repAttdService.retrieve(repAttdFormVO.getExecAttdVO()) == null) {
                repAttdFormVO.getRepAttdVOList().add(repAttdFormVO.getExecAttdVO());
            }
            else if (repAttdFormVO.getExecAttd().equals("-")) {
                repAttdService.deleteOne(repAttdFormVO.getExecAttdVO());
            }
            else {
                repAttdService.update(repAttdFormVO.getExecAttdVO());
            }
            
            // plan update
            if (repAttdService.retrieve(repAttdFormVO.getPlanAttdVO()) == null) {
                repAttdFormVO.getRepAttdVOList().add(repAttdFormVO.getPlanAttdVO());
            }
            else if (repAttdFormVO.getPlanAttd().equals("-")) {
                repAttdService.deleteOne(repAttdFormVO.getPlanAttdVO());
            }
            else {
                repAttdService.update(repAttdFormVO.getPlanAttdVO());
            }

            if (repAttdFormVO.getRepAttdVOList().size() > 0) {
                repAttdService.create(repAttdFormVO.getRepAttdVOList()); 
            }
            
            // 작성되지 않은 보고서는 create
            final List<RepDetailVO> createList = new ArrayList<RepDetailVO>();
            for (RepDetailVO vo : repFormVO.getVariableVOList()) {
                if(repDetailService.retrieve(vo) == null) {
                    createList.add(vo);
                }
            }

            // detail create
            repDetailService.create(createList);
            
        }
        else {
            Date date = repFormVO.getRepDetailVO().getReportDt(); // create화면에서 수정
            
            if (date != null) {
                List<Integer> repSn = new ArrayList<Integer>();
                repSn.add(repFormVO.getRepMasterVO().getRepSn());
                repFormVO.setRepSns(repSn);
            }
        }
        
        //detail update
        repDetailService.update(repFormVO.getVariableVOList());
        
        model.addAttribute("repFormVO", repFormVO);
        model.addAttribute("repTyCode", repFormVO.getRepDetailVO().getRepTyCode());
        
        return "redirect:/itsm/rep/detail/mngr/retrievePagingList.do";
    }
    
    /**
     * 보고서 삭제
     * @param repFormVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/rep/detail/mngr/delete.do")
    public String delete(RepFormVO repFormVO, HttpSession session) throws Exception {
        repDetailService.delete(repFormVO.getVariableVOList());
        
        if (repFormVO.getRepDetailVO().getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            // 로그인 유저 정보
            final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
            
            // 근태 수정 시작
            final RepAttdVO repAttdVO = new RepAttdVO();
            repAttdVO.setUserId(loginUserVO.getUserId());
            repAttdVO.setAttdDt(repFormVO.getRepDetailVO().getReportDt());
            
            repAttdService.deleteOne(repAttdVO);
            
            final Calendar cal = Calendar.getInstance();
            cal.setTime(repFormVO.getRepDetailVO().getReportDt());
            
            final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
            
            cal.add(Calendar.DATE, -1);
            long date = DateUtil.dateToLong(cal.getTime());
            
            while (DateUtil.isHoliday(date,solar,lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
            
            repAttdVO.setAttdDt(cal.getTime());
            repAttdService.deleteOne(repAttdVO);
        }
        
        return "redirect:/itsm/rep/detail/mngr/retrievePagingList.do";
    }
    
    /**
     * 이전 보고서 가져오기(ajax)
     * @param repDetailVO
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/rep/detail/mngr/retreiveRepDetailAjax.do")
    public String retrieveRepDetailAjax(RepDetailVO repDetailVO, HttpSession session, ModelMap model) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        repDetailVO.setUserId(loginUserVO.getUserId());
        
        // 일일보고서는 근태코드도 같이 가져온다.
        if (repDetailVO.getRepTyCode().equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            final RepAttdFormVO repAttdFormVO = new RepAttdFormVO();
            repAttdFormVO.setCreatId(loginUserVO.getUserId());
            repAttdFormVO.getExecAttdVO().setAttdDt(repDetailVO.getReportDt());
            RepAttdVO execAttdVO = repAttdService.retrieve(repAttdFormVO.getExecAttdVO());
            model.addAttribute("execAttdVO", execAttdVO);
            
            final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
            
            repAttdFormVO.setPlanDate(repDetailVO.getReportDt(), solar, lunar);
            
            final RepAttdVO planAttdVO = repAttdService.retrieve(repAttdFormVO.getPlanAttdVO());
            model.addAttribute("planAttdVO", planAttdVO);
        }
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(repDetailVO.getReportDt());
        cal.add(Calendar.DATE, 1);
        repDetailVO.setReportDt(cal.getTime());                              
        
        
        // 해당 날짜 이전에 저장된 마스터보고서의 PK를 가져온다.
        final RepMasterVO lastInsertedVO = new RepMasterVO();
        lastInsertedVO.setReportDt(repDetailVO.getReportDt());
        lastInsertedVO.setRepTyCode(repDetailVO.getRepTyCode());
        Integer lastId = repMasterService.lastInsertedId(lastInsertedVO);

        // 해당 날짜 이전 데이터가 하나도 없는 경우
        if (lastId == null) {
            lastInsertedVO.setReportDt(null);
            lastId = repMasterService.lastInsertedId(lastInsertedVO);
        }

        // 해당 마스터보고서에 딸린 디테일보고서 가져온다.
        final RepDetailVO lastDetailVO = new RepDetailVO();
        lastDetailVO.setRepSn(lastId);
        lastDetailVO.setUserId(loginUserVO.getUserId());
        lastDetailVO.setRepTyCode(repDetailVO.getRepTyCode());

        List<RepDetailVO> detailVOList = repDetailService.retrieveList(lastDetailVO);
        
        //해당 마스터보고서에 딸린 디테일보고서가 없을 경우
        int cnt = 0;

        while (detailVOList.size() < 1 && cnt < 5) {
            cal.add(Calendar.DATE, -1);
            lastInsertedVO.setReportDt(cal.getTime());
            lastId = repMasterService.lastInsertedId(lastInsertedVO);
            lastDetailVO.setRepSn(lastId);
            detailVOList = repDetailService.retrieveList(lastDetailVO);
            System.out.println(lastDetailVO.getRepSn());
            cnt++;
        };
        
        LOGGER.debug("PK : {}", lastId);
        LOGGER.debug("repSn, usrId, repTyCode : {} {} {}", lastDetailVO.getRepSn(), lastDetailVO.getUserId(), lastDetailVO.getRepTyCode());
        
        // 특수문자 unescape
        for (int i = 0; i < detailVOList.size(); i++) {
            final RepDetailVO vo = detailVOList.get(i);
            detailVOList.get(i).setExecDesc(StringEscapeUtils.unescapeHtml4(vo.getExecDesc()));
            detailVOList.get(i).setPlanDesc(StringEscapeUtils.unescapeHtml4(vo.getPlanDesc()));
        }
        
        model.addAttribute("detailVOList", detailVOList);
        
        return "jsonView";
    }

    /**
     * 검증
     * @param reportDt
     * @param sysCodes
     * @param repTyCode
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/itsm/rep/detail/mngr/retrieveAjax.do")
    public String retrieveAjax(Date reportDt, String[] sysCodes, String repTyCode, ModelMap model) throws Exception {
        final Calendar cal = Calendar.getInstance();

        if (repTyCode.equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            cal.setTime(reportDt);

            if (cal.get(cal.DAY_OF_WEEK) == 6) {
                cal.add(Calendar.DATE, +3);
            }
            else {
                cal.add(Calendar.DATE, +1);
            }
        }
        else if (repTyCode.equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 6 - cal.get(cal.DAY_OF_WEEK));
        }
        else if (repTyCode.equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
            cal.setTime(new Date());
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH+1), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        
        final RepMasterVO vo = new RepMasterVO();
        vo.setReportDt(cal.getTime());
        vo.setRepTyCode(repTyCode);

        final RepMasterVO retrieveVO = repMasterService.retrieve(vo);

        // 마스터보고서가 있는경우
        if (retrieveVO != null) {
            // 마스터보고서가 확정됐을경우
            if (!retrieveVO.getSttusCode().equals(RepMasterVO.STTUS_CODE_WRITE)) {
                model.addAttribute("returnMessage", "confirm");

                return "jsonView";
            }
        }
        // 마스터보고서가 없는 경우
        else {
            model.addAttribute("returnMessage", "create");

            return "jsonView";
        }

        for (String sysCode:sysCodes) {
            final RepDetailVO repDetailVO = new RepDetailVO();
            repDetailVO.setSysCode(sysCode);
            repDetailVO.setRepSn(retrieveVO.getRepSn());
            repDetailVO.setRepTyCode(repTyCode);

            final RepDetailVO retrieveDetailVO = repDetailService.retrieve(repDetailVO);

            // 디테일 보고서가 있는경우
            if (retrieveDetailVO != null) {
                model.addAttribute("returnMessage", retrieveDetailVO.getRepSn());

                return "jsonView";
            }
        }

        model.addAttribute("returnMessage", "create");

        return "jsonView";
    }
    
    /**
     * 보고서의 날짜 범위 구하기
     * @param model
     * @param repTyCode
     * @throws Exception
     */
    public List<String> getDayRange(ModelMap model, String repTyCode) throws Exception {
        final List<String> dayRange = new ArrayList<String>();

        if (repTyCode.equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.HOLIDAY_CODE);
            cmmnCodeVO.setCmmnCodeSubNm1("양력");
            final List<CmmnCodeVO> solar = cmmnCodeService.retrieveList(cmmnCodeVO);

            cmmnCodeVO.setCmmnCodeSubNm1("음력");
            final List<CmmnCodeVO> lunar = cmmnCodeService.retrieveList(cmmnCodeVO);
            
            // 금주, 차주 범위 구하기
            final Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // 현재 날짜
            cal.setFirstDayOfWeek(Calendar.MONDAY); // 주차의 기준을 월요일로 정한다.
            cal.setMinimalDaysInFirstWeek(7); // 주차에 포함될 최소 일수는 7일이다.
            
            model.addAttribute("weekOfYear", cal.get(Calendar.WEEK_OF_YEAR)); // 현재 주차수
            
            final SimpleDateFormat weekDateFormat = new SimpleDateFormat("MM.dd");
            long date;
            final int[] week = { 2, 6, 2, 6 }; // 월, 금, 월, 금
            
            // 금주와 차주 범위 구하는 로직 시작
            for (int i = 0; i < week.length; i++) {
                if (i == 2) {
                    cal.add(Calendar.DATE, +7); // 다음주로
                }
                
                // 요일의 날짜구하기
                cal.set(Calendar.DAY_OF_WEEK, week[i]);
                date = DateUtil.dateToLong(cal.getTime());
                
                while (DateUtil.isHoliday(date,solar,lunar)) {
                    // 월요일이면 +1(다음날로), 금요일이면 -1(전일로)
                    if (week[i] == Calendar.MONDAY) {
                        cal.add(Calendar.DATE, +1);
                    }
                    else {
                        cal.add(Calendar.DATE, -1);
                    }

                    date = DateUtil.dateToLong(cal.getTime());
                }
                dayRange.add(weekDateFormat.format(cal.getTime()));
            }
        }
        else if (repTyCode.equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            
            final int thisMonth = cal.get(Calendar.MONTH) + 1; // 오늘이 어떤 달인지 구하기
            int nextMonth = thisMonth + 1;

            if (nextMonth == 13) {
                nextMonth = 1; // 13이면 1월
            }
            
            dayRange.add(thisMonth + ".1");
            dayRange.add(thisMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 현재달 말일 구하기
            dayRange.add(nextMonth + ".1");
            cal.add(Calendar.MONTH, 1); 
            dayRange.add(nextMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 다음달 말일 구하기
            
            model.addAttribute("month", thisMonth); 
        }
        
        return dayRange;
    }
}
