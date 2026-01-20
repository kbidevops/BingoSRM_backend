package kr.go.smplatform.itsm.report.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.report.service.RepMasterService;
import kr.go.smplatform.itsm.report.vo.RepAttachmentNameAndSizeVO;
import kr.go.smplatform.itsm.report.vo.RepMasterVO;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportMasterApiController {
    private final RepMasterService repMasterService;

    public ReportMasterApiController(RepMasterService repMasterService) {
        this.repMasterService = repMasterService;
    }

    @GetMapping
    public Map<String, Object> list(RepMasterVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setDeleteYn("N");
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        List<RepMasterVO> list = repMasterService.retrievePagingList(search);
        int totalCount = repMasterService.totCnt(search);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }

    @GetMapping("/{repSn}")
    public RepMasterVO get(@PathVariable("repSn") int repSn, RepMasterVO search) throws Exception {
        search.setRepSn(repSn);
        return repMasterService.retrieve(search);
    }

    @GetMapping("/{repSn}/attachments")
    public List<RepAttachmentNameAndSizeVO> attachments(@PathVariable("repSn") int repSn) {
        return repMasterService.getAttachments(String.valueOf(repSn));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody RepMasterVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request.getReportDt() == null) {
            if (request.getReportDtDisplay() != null && !request.getReportDtDisplay().isEmpty()) {
                request.setReportDtDisplay(request.getReportDtDisplay());
            }
        }
        if (request.getReportDt() == null) {
            request.setReportDt(new Date());
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setCreatId(actorUserId);
            request.setUpdtId(actorUserId);
        }

        repMasterService.create(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("repSn", request.getRepSn() > 0 ? request.getRepSn() : repMasterService.lastInsertedId(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{repSn}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("repSn") int repSn,
            @RequestBody RepMasterVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        request.setRepSn(repSn);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        repMasterService.update(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("repSn", repSn);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{repSn}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("repSn") int repSn,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        RepMasterVO request = new RepMasterVO();
        request.setRepSn(repSn);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        repMasterService.delete(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("repSn", repSn);
        return ResponseEntity.ok(response);
    }
}
