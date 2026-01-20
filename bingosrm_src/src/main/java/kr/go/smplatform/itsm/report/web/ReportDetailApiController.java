package kr.go.smplatform.itsm.report.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.report.service.RepDetailService;
import kr.go.smplatform.itsm.report.vo.RepDetailVO;

@RestController
@RequestMapping("/api/v1/report-details")
public class ReportDetailApiController {
    private final RepDetailService repDetailService;

    public ReportDetailApiController(RepDetailService repDetailService) {
        this.repDetailService = repDetailService;
    }

    @GetMapping
    public List<RepDetailVO> list(RepDetailVO search) throws Exception {
        search.setDeleteYn("N");
        return repDetailService.retrieveList(search);
    }

    @GetMapping("/paged")
    public Map<String, Object> paged(RepDetailVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setDeleteYn("N");
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        List<RepDetailVO> list = repDetailService.retrievePagingList(search);
        int totalCount = repDetailService.retrieveTotalCnt(search);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody ReportDetailBulkRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request == null || request.getDetails() == null || request.getDetails().isEmpty()) {
            return ResponseEntity.badRequest().body(error("details is required"));
        }

        for (RepDetailVO detail : request.getDetails()) {
            if (actorUserId != null && !actorUserId.isEmpty()) {
                detail.setCreatId(actorUserId);
            }
            if (detail.getCreatDt() == null && detail.getReportDt() != null) {
                detail.setCreatDt(detail.getReportDt());
            }
        }

        repDetailService.create(request.getDetails());

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> update(
            @RequestBody ReportDetailBulkRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request == null || request.getDetails() == null || request.getDetails().isEmpty()) {
            return ResponseEntity.badRequest().body(error("details is required"));
        }

        for (RepDetailVO detail : request.getDetails()) {
            if (actorUserId != null && !actorUserId.isEmpty()) {
                detail.setUpdtId(actorUserId);
            }
        }

        int updated = repDetailService.update(request.getDetails());

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", updated == 1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> delete(
            @RequestParam("repSn") int repSn,
            @RequestParam("sysCode") String sysCode,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        RepDetailVO request = new RepDetailVO();
        request.setRepSn(repSn);
        request.setSysCode(sysCode);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        repDetailService.deleteOne(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("error", message);
        return response;
    }

    public static class ReportDetailBulkRequest {
        private List<RepDetailVO> details;

        public List<RepDetailVO> getDetails() {
            return details;
        }

        public void setDetails(List<RepDetailVO> details) {
            this.details = details;
        }
    }
}
