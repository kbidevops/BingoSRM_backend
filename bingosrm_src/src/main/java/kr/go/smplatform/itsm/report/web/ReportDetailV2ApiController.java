package kr.go.smplatform.itsm.report.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.report.service.RepDetailService2;
import kr.go.smplatform.itsm.report.vo.CRRepAttachmentVO;
import kr.go.smplatform.itsm.report.vo.RepAssignVO;
import kr.go.smplatform.itsm.report.vo.RepAttachmentNameAndSizeVO;
import kr.go.smplatform.itsm.report.vo.RepAttitudeVO;
import kr.go.smplatform.itsm.report.vo.RepDetailVO2;
import kr.go.smplatform.itsm.report.vo.RepFormVO2;
import kr.go.smplatform.itsm.report.vo.RepMasterVO2;

@RestController
@RequestMapping("/api/v1/report-details/v2")
public class ReportDetailV2ApiController {
    private final RepDetailService2 repDetailService2;
    private final AtchmnflService atchmnflService;

    public ReportDetailV2ApiController(RepDetailService2 repDetailService2, AtchmnflService atchmnflService) {
        this.repDetailService2 = repDetailService2;
        this.atchmnflService = atchmnflService;
    }

    @GetMapping("/last")
    public ResponseEntity<Map<String, Object>> lastRepSn(
            @RequestParam("repTyCode") String repTyCode,
            @RequestParam("reportDt") String reportDt) throws ParseException {
        Date date = parseDate(reportDt);
        if (date == null) {
            return ResponseEntity.badRequest().body(error("reportDt must be yyyy-MM-dd"));
        }
        int lastRepSn = repDetailService2.getLastRepSn(repTyCode, date);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("lastRepSn", lastRepSn);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> details(
            @RequestParam("repSn") String repSn,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        RepMasterVO2 master = repDetailService2.getMasterInfo(repSn);
        List<RepDetailVO2> detailList = repDetailService2.getDetailList(effectiveUserId, repSn);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("master", master);
        if (master != null && master.getReportDt() != null) {
            Date reportDate = new SimpleDateFormat("yyyy-MM-dd").parse(master.getReportDt());
            Date prevDate = repDetailService2.getLastWeekday(reportDate);
            response.put("prevDate", new SimpleDateFormat("yyyy-MM-dd").format(prevDate));
        }
        response.put("detailList", detailList);

        List<RepAttachmentNameAndSizeVO> attachmentInfoList = repDetailService2.getAttachmentInfo(repSn, effectiveUserId);
        if (attachmentInfoList.size() == 1) {
            response.put("attachments", attachmentInfoList.get(0));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignments")
    public ResponseEntity<Map<String, Object>> assignments(
            @RequestParam("repTyCode") String repTyCode,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        List<RepAssignVO> list = repDetailService2.getAssignList(effectiveUserId, repTyCode);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("list", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/attitudes")
    public ResponseEntity<Map<String, Object>> attitudes(
            @RequestParam(value = "reportDt", required = false) String reportDt,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        Map<String, Object> response = new HashMap<String, Object>();
        if (reportDt == null || reportDt.isEmpty()) {
            response.put("currentList", repDetailService2.getAttitudeList(null, effectiveUserId));
            response.put("nextList", repDetailService2.getAttitudeList(null, effectiveUserId));
            return ResponseEntity.ok(response);
        }

        Date reportDate = parseDate(reportDt);
        if (reportDate == null) {
            return ResponseEntity.badRequest().body(error("reportDt must be yyyy-MM-dd"));
        }

        if (next) {
            reportDate = repDetailService2.getNextWeekday(reportDate, "B001");
        }

        List<RepAttitudeVO> currentList = repDetailService2.getAttitudeList(repDetailService2.getLastWeekday(reportDate), effectiveUserId);
        List<RepAttitudeVO> nextList = repDetailService2.getAttitudeList(reportDate, effectiveUserId);
        response.put("currentList", currentList);
        response.put("nextList", nextList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status(
            @RequestParam("repTyCode") String repTyCode,
            @RequestParam("reportDt") String reportDt,
            @RequestParam(value = "repSn", required = false) String repSn,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        Date reportDate = parseDate(reportDt);
        if (reportDate == null) {
            return ResponseEntity.badRequest().body(error("reportDt must be yyyy-MM-dd"));
        }

        if (repSn == null || repSn.isEmpty()) {
            repSn = repDetailService2.getMasterSn(reportDate, repTyCode);
        }
        if (repSn == null || repSn.isEmpty()) {
            return ResponseEntity.badRequest().body(error("repSn is required"));
        }

        boolean confirmed = repDetailService2.isConfirmed(repSn);
        List<RepDetailVO2> list = repDetailService2.getDetailList(effectiveUserId, repSn);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("duplicated", !list.isEmpty());
        response.put("confirmed", confirmed);
        response.put("repSn", repSn);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-info")
    public ResponseEntity<Map<String, Object>> lastInfo(
            @RequestParam("repTyCode") String repTyCode,
            @RequestParam("reportDt") String reportDt,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        Date reportDate = parseDate(reportDt);
        if (reportDate == null) {
            return ResponseEntity.badRequest().body(error("reportDt must be yyyy-MM-dd"));
        }

        Date nextWeekday = repDetailService2.getNextWeekday(reportDate, repTyCode);
        String repSn = String.valueOf(repDetailService2.getLastRepSn(repTyCode, nextWeekday));

        RepMasterVO2 master = repDetailService2.getMasterInfo(repSn);
        List<RepDetailVO2> detailList = repDetailService2.getDetailList(effectiveUserId, repSn);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("master", master);
        response.put("detailList", detailList);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody ReportDetailV2Request request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request == null || request.getFormData() == null) {
            return ResponseEntity.badRequest().body(error("formData is required"));
        }

        String effectiveUserId = resolveUserId(request.getUserId(), actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        RepFormVO2 formData = request.getFormData();
        String repSn = formData.getRepSn();
        if (repSn == null || repSn.isEmpty()) {
            if (formData.getType() == null || formData.getType().isEmpty()) {
                return ResponseEntity.badRequest().body(error("repSn or type is required"));
            }
            repSn = repDetailService2.createMaster(formData, effectiveUserId);
        }

        if (repDetailService2.isConfirmed(repSn)) {
            return ResponseEntity.status(409).body(error("Report is already confirmed"));
        }

        repDetailService2.updateDetails(repSn, formData, effectiveUserId);

        if (request.getRequiredFileId() != null || request.getAdditionalFileId() != null) {
            CRRepAttachmentVO vo = CRRepAttachmentVO.Builder
                    .aCRRepAttachmentVO()
                    .requiredFile(request.getRequiredFileId())
                    .additionalFile(request.getAdditionalFileId())
                    .repSn(repSn)
                    .userId(effectiveUserId)
                    .build();
            repDetailService2.updateAttachment(vo);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", true);
        response.put("repSn", repSn);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> delete(
            @RequestParam("repSn") String repSn,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        repDetailService2.deleteDetails(repSn, effectiveUserId);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", true);
        response.put("repSn", repSn);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/attachments/additional")
    public ResponseEntity<Map<String, Object>> deleteAdditionalFile(
            @RequestParam("repSn") String repSn,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        repDetailService2.removeAdditionalFile(repSn, effectiveUserId);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/attachments")
    public ResponseEntity<Map<String, Object>> uploadAttachment(
            @RequestParam("repSn") String repSn,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "requiredFileId", required = false) String requiredFileId,
            @RequestParam(value = "additionalFileId", required = false) String additionalFileId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        if (requiredFileId != null || additionalFileId != null) {
            CRRepAttachmentVO vo = CRRepAttachmentVO.Builder
                    .aCRRepAttachmentVO()
                    .requiredFile(requiredFileId)
                    .additionalFile(additionalFileId)
                    .repSn(repSn)
                    .userId(effectiveUserId)
                    .build();
            repDetailService2.updateAttachment(vo);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/attachments/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("repSn") String repSn,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        String effectiveUserId = resolveUserId(userId, actorUserId);
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(error("userId is required"));
        }

        AtchmnflVO fileVO = atchmnflService.saveFile(file);
        CRRepAttachmentVO vo = CRRepAttachmentVO.Builder
                .aCRRepAttachmentVO()
                .requiredFile(fileVO.getAtchmnflId())
                .repSn(repSn)
                .userId(effectiveUserId)
                .build();
        repDetailService2.updateAttachment(vo);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", true);
        response.put("atchmnflId", fileVO.getAtchmnflId());
        return ResponseEntity.ok(response);
    }

    private String resolveUserId(String userId, String actorUserId) {
        if (userId != null && !userId.isEmpty()) {
            return userId;
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            return actorUserId;
        }
        return null;
    }

    private Date parseDate(String value) throws ParseException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").parse(value);
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("error", message);
        return response;
    }

    public static class ReportDetailV2Request {
        private RepFormVO2 formData;
        private String userId;
        private String requiredFileId;
        private String additionalFileId;

        public RepFormVO2 getFormData() {
            return formData;
        }

        public void setFormData(RepFormVO2 formData) {
            this.formData = formData;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRequiredFileId() {
            return requiredFileId;
        }

        public void setRequiredFileId(String requiredFileId) {
            this.requiredFileId = requiredFileId;
        }

        public String getAdditionalFileId() {
            return additionalFileId;
        }

        public void setAdditionalFileId(String additionalFileId) {
            this.additionalFileId = additionalFileId;
        }
    }
}
