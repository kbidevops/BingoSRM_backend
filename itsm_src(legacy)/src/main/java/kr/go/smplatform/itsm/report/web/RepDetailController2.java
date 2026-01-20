package kr.go.smplatform.itsm.report.web;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.report.service.RepDetailService2;
import kr.go.smplatform.itsm.report.vo.*;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 업무보고작성(상세, 수정) 화면 및 그에 대한 기능
 */
@Controller
public class RepDetailController2 extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepDetailController2.class);
    
    @Resource(name = "repDetailService2")
    private RepDetailService2 repDetailService2;

    @Resource(name = "atchmnflService")
    private AtchmnflService atchmnflService;

    /**
     * 보고서 작성 화면 조회
     * @param repTyCode 보고서 종류(일일: B001, 주간: B002, 월간: B003)
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail/mngr/createView.do", method = RequestMethod.GET)
    public String createView(@ModelAttribute String repTyCode, Model model) {
        switch (repTyCode) {
            case "B001":
                model.addAttribute("programName", "일일보고서");
                break;

            case "B002":
                model.addAttribute("programName", "주간보고서");
                break;

            case "B003":
                model.addAttribute("programName", "월간보고서");
                break;
        }

        return "/itsm/report/detail/mngr/editC";
    }

    /**
     * 이전 보고서 Sn 알아내기
     * ! 본인 무관
     * @param repTyCode 보고서 종류(일일: B001, 주간: B002, 월간: B003)
     * @param reportDt 보고일자
     * @param model
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getLastRepSnsAjax.do", method = RequestMethod.GET)
    public String getLastDetails(Model model, String repTyCode, Date reportDt) {
        final int lastRepSn = repDetailService2.getLastRepSn(repTyCode, reportDt);

        model.addAttribute("data", lastRepSn);

        return "jsonView";
    }

    /**
     * 보고서 내용 조회
     * ! 본인 무관
     * @param repSn 보고서 마스터 sn
     * @param userId 보고서 작성자 (비어있을 시 로그인중인 유저 ID)
     * @param model
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getDetailsAjax.do", method = RequestMethod.GET)
    public String getDetails(String repSn, String userId, Model model, HttpSession session) throws ParseException {
        if (userId == null || "".equals(userId.trim())) {
            final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

            userId = loginUserVO.getUserId();
        }

        final RepMasterVO2 master = repDetailService2.getMasterInfo(repSn);
        final List<RepDetailVO2 > detailList = repDetailService2.getDetailList(userId, repSn);

        model.addAttribute("master", master);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Date reportDate = sdf.parse(master.getReportDt());
        final Date prevDate = repDetailService2.getLastWeekday(reportDate);
        model.addAttribute("prevDate", sdf.format(prevDate));
        model.addAttribute("detailList", detailList);

        // 첨부파일
        List<RepAttachmentNameAndSizeVO> attachmentInfoList = repDetailService2.getAttachmentInfo(repSn, userId);
        if (attachmentInfoList.size() == 1) {
            model.addAttribute("attachments", attachmentInfoList.get(0));
        }

        return "jsonView";
    }

    /**
     * 담당 보고서 코드 조회
     * ! 본인 것만
     * @param repTyCode 보고서 종류(일일: B001, 주간: B002, 월간: B003)
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getAssignListAjax.do", method = RequestMethod.GET)
    public String getAssignList(String repTyCode, HttpSession session, Model model) {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        final List<RepAssignVO> list = repDetailService2.getAssignList(loginUserVO.getUserId(), repTyCode);

        model.addAttribute("list", list);

        return "jsonView";
    }

    /**
     * 근태 코드 목록 조회 (일일보고만 사용)
     * ! 본인 무관
     * 파라미터 비어있을 시 그냥 근태 목록만, 날짜와 유저 ID 입력 시 pick 네 개 중에 하나는 true로 조회될 것임
     * @param model
     * @param reportDt 입력 안 해도 됨
     * @param userId 입력 안 해도 됨
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getAttitudesAjax.do", method = RequestMethod.GET)
    public String getAttitudes(HttpSession session, Model model, Date reportDt, String userId, @RequestParam(value = "next", required = false, defaultValue = "false") boolean next) throws Exception {
        if (userId == null || "".equals(userId.trim())) {
            final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

            userId = loginUserVO.getUserId();
        }

        if (reportDt == null) {
            model.addAttribute("currentList", repDetailService2.getAttitudeList(null, userId));
            model.addAttribute("nextList", repDetailService2.getAttitudeList(null, userId));

            return "jsonView";
        }

        if (next) {
            reportDt = repDetailService2.getNextWeekday(reportDt, RepMasterVO.REP_TY_CODE_DAILY);
        }

        final List<RepAttitudeVO> currentList = repDetailService2.getAttitudeList(repDetailService2.getLastWeekday(reportDt), userId);
        model.addAttribute("currentList", currentList);

        final List<RepAttitudeVO> nextList = repDetailService2.getAttitudeList(reportDt, userId);
        model.addAttribute("nextList", nextList);

        return "jsonView";
    }

    /**
     * 추가 첨부파일 삭제 ajax
     * @param session
     * @param model
     * @param repSn
     * @param userId
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/deleteFileAjax.do", method = RequestMethod.POST)
    public String deleteFile(HttpSession session, Model model, String repSn, String userId) {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        model.addAttribute("result", false);

        if (loginUserVO == null) {
            model.addAttribute("message", "로그인이 필요합니다.");

            return "jsonView";
        }

        if (userId == null || "".equals(userId.trim())) {
            model.addAttribute("message", "삭제할 보고서의 작성 유저를 지정해주세요.");

            return "jsonView";
        }

        if (!userId.equals(loginUserVO.getUserId())) {
            model.addAttribute("message", "본인이 작성한 보고서의 파일만 삭제할 수 있습니다.");

            return "jsonView";
        }

        repDetailService2.removeAdditionalFile(repSn, userId);
        model.addAttribute("result", true);

        return "jsonView";
    }

    /**
     * 보고서 작성/수정
     * ! 본인 것만
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/createAjax.do", method = RequestMethod.POST)
    public String create(HttpSession session, Model model, RepFormVO2 formData, @RequestPart(value = "requiredFile", required = false) MultipartFile requiredFile, @RequestPart(value = "additionalFile", required = false) MultipartFile additionalFile) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO == null || StringUtils.isEmpty(loginUserVO.getUserId())) {
            model.addAttribute("result", false);
            model.addAttribute("message", "로그인이 필요합니다.");

            return "jsonView";
        }

        final String userId = loginUserVO.getUserId();

        final String tempRepSn = formData.getRepSn();
        final String repTyCode = formData.getType();

        final String repSn;

        try {
            if (tempRepSn == null || "".equals(tempRepSn)) {
                if (repTyCode == null || "".equals(repTyCode)) {
                    model.addAttribute("result", false);
                    model.addAttribute("message", "repSn 혹은 type 중 하나는 비어있지 않아야 합니다.");

                    return "jsonView";
                }

                repSn = repDetailService2.createMaster(formData, userId);
            }
            else {
                repSn = tempRepSn;
            }

            final boolean confirmed = repDetailService2.isConfirmed(repSn);

            if (confirmed) {
                model.addAttribute("result", false);
                model.addAttribute("message", "이미 확정 되었거나 확정 요청된 보고서입니다.");

                return "jsonView";
            }

            repDetailService2.updateDetails(repSn, formData, userId);

            model.addAttribute("result", true);
            model.addAttribute("repSn", repSn);

            // 파일 처리
            final String requiredFileId;
            if (requiredFile != null && !requiredFile.isEmpty()) {
                final AtchmnflVO fileVO = atchmnflService.saveFile(requiredFile);
                requiredFileId = fileVO.getAtchmnflId();
            }
            else {
                requiredFileId = null;
            }

            // 파일 처리
            final String additionalFileId;
            if (additionalFile != null && !additionalFile.isEmpty()) {
                // TODO get fileId
                final AtchmnflVO fileVO = atchmnflService.saveFile(additionalFile);
                additionalFileId = fileVO.getAtchmnflId();
            }
            else {
                additionalFileId = null;
            }

            if (requiredFileId != null && additionalFileId != null) {
                final CRRepAttachmentVO vo = CRRepAttachmentVO.Builder
                        .aCRRepAttachmentVO()
                        .requiredFile(requiredFileId)
                        .additionalFile(additionalFileId)
                        .repSn(repSn)
                        .userId(userId)
                        .build();

                repDetailService2.updateAttachment(vo);
            }
        }
        catch (Exception e) {
            model.addAttribute("result", false);
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "jsonView";
    }

    /**
     * 보고서 삭제
     * ! 본인 것만
     * @param session
     * @param model
     * @param reportDt
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/deleteAjax.do", method = RequestMethod.POST)
    public String delete(HttpSession session, Model model, String repSn) {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        try {
            repDetailService2.deleteDetails(repSn, loginUserVO.getUserId());
            model.addAttribute("result", true);
        }
        catch (Exception e) {
            model.addAttribute("result", false);
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "jsonView";
    }

    /**
     * 상태 확인 (확정, 미확정, 중복 등)
     * @param session
     * @param model
     * @param reportDt
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getStatusAjax.do", method = RequestMethod.GET)
    public String getStatus(HttpSession session, ModelMap model, Date reportDt, String repTyCode, String repSn) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO == null || loginUserVO.getUserId() == null) {
            model.addAttribute("message", "로그인이 필요합니다");

            return "jsonView";
        }

        if (repSn == null || "".equals(repSn.trim())) {
            repSn = repDetailService2.getMasterSn(reportDt, repTyCode);
        }
        final boolean confirmed = repDetailService2.isConfirmed(repSn);
        List<RepDetailVO2> list = repDetailService2.getDetailList(loginUserVO.getUserId(), repSn);

        model.addAttribute("duplicated", list.size() > 0);
        model.addAttribute("confirmed", confirmed);

        return "jsonView";
    }

    /**
     * 이전 보고서 내용 가져오기
     * @param session
     * @param model
     * @param reportDt
     * @param repTyCode
     * @return
     */
    @RequestMapping(value = "/itsm/rep/detail2/mngr/getLastReportInfoAjax.do", method = RequestMethod.GET)
    public String getLastReportInfoAjax(HttpSession session, Model model, Date reportDt, String repTyCode) throws Exception {
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO == null || loginUserVO.getUserId() == null) {
            model.addAttribute("result", false);
            model.addAttribute("message", "로그인이 필요합니다.");

            return "jsonView";
        }

        final String userId = loginUserVO.getUserId();

        final Date nextWeekday = repDetailService2.getNextWeekday(reportDt, repTyCode);

        final String repSn = repDetailService2.getLastRepSn(repTyCode, nextWeekday) + "";
        final RepMasterVO2 master = repDetailService2.getMasterInfo(repSn);
        final List<RepDetailVO2> detailList = repDetailService2.getDetailList(userId, repSn);

        model.addAttribute("master", master);
        model.addAttribute("detailList", detailList);

        return "jsonView";
    }
}
