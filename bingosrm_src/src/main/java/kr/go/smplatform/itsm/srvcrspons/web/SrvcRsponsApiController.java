package kr.go.smplatform.itsm.srvcrspons.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeService;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

@RestController
@RequestMapping("/api/v1/sr")
public class SrvcRsponsApiController {
    private final SrvcRsponsService srvcRsponsService;
    private final CmmnCodeService cmmnCodeService;

    public SrvcRsponsApiController(SrvcRsponsService srvcRsponsService, CmmnCodeService cmmnCodeService) {
        this.srvcRsponsService = srvcRsponsService;
        this.cmmnCodeService = cmmnCodeService;
    }

    @GetMapping
    public Map<String, Object> list(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrievePagingList(search);
        int totalCount = srvcRsponsService.retrievePagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/requests")
    public Map<String, Object> requestList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrReqList(search);
        int totalCount = srvcRsponsService.retrieveSrReqPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/receives")
    public Map<String, Object> receiveList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrRcvList(search);
        int totalCount = srvcRsponsService.retrieveSrRcvPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/processes")
    public Map<String, Object> processList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrProcList(search);
        int totalCount = srvcRsponsService.retrieveSrProcPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/verifications")
    public Map<String, Object> verifyList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrVrList(search);
        int totalCount = srvcRsponsService.retrieveSrVrPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/finishes")
    public Map<String, Object> finishList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrFnList(search);
        int totalCount = srvcRsponsService.retrieveSrFnPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/evaluations")
    public Map<String, Object> evaluationList(SrvcRsponsVO search) throws Exception {
        Paging paging = applyPaging(search);
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrEvList(search);
        int totalCount = srvcRsponsService.retrieveSrEvPagingListCnt(search);
        return buildPagedResponse(list, totalCount, paging);
    }

    @GetMapping("/requesters")
    public Map<String, Object> requesterList(SrvcRsponsVO search) throws Exception {
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveRqesterNmList(search);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        return response;
    }

    @GetMapping("/requesters/first")
    public Map<String, Object> requesterFirstList(SrvcRsponsVO search) throws Exception {
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveRqester1stNmList(search);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        return response;
    }

    @GetMapping("/numbers")
    public Map<String, Object> numberList(SrvcRsponsVO search) throws Exception {
        List<SrvcRsponsVO> list = srvcRsponsService.retrieveSrvcRsponsNoList(search);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        return response;
    }

    @GetMapping("/{srvcRsponsNo}")
    public SrvcRsponsVO get(@PathVariable("srvcRsponsNo") String srvcRsponsNo) throws Exception {
        SrvcRsponsVO search = new SrvcRsponsVO();
        search.setSrvcRsponsNo(srvcRsponsNo);
        return srvcRsponsService.retrieve(search);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        trimSubject(request);
        ensureRequestDate(request);
        ensureRequestAttachmentIds(request);

        if (isEmpty(request.getSrvcRsponsBasisCode())) {
            request.setSrvcRsponsBasisCode(SrvcRsponsVO.SRVC_RSPONS_BASIS_CODE_S306);
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            if (isEmpty(request.getRqesterId())) {
                request.setRqesterId(actorUserId);
            }
            if (isEmpty(request.getCreatId())) {
                request.setCreatId(actorUserId);
            }
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.create(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("srvcRsponsNo", request.getSrvcRsponsNo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manager")
    public ResponseEntity<Map<String, Object>> createForManager(
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        trimSubject(request);
        ensureRequestDate(request);
        ensureRequestAttachmentIds(request);

        if (isEmpty(request.getProcessStdrCode())) {
            request.setProcessStdrCode(SrvcRsponsVO.DEFAULT_PROCESS_STDR_CODE);
        }
        if (isEmpty(request.getChangeDfflyCode())) {
            request.setChangeDfflyCode(SrvcRsponsVO.DEFAULT_CHANGE_DFFLY_CODE);
        }
        if (isEmpty(request.getSrvcRsponsBasisCode())) {
            request.setSrvcRsponsBasisCode(SrvcRsponsVO.DEFAULT_SRVC_RSPONS_BASIS_CODE);
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            if (isEmpty(request.getCreatId())) {
                request.setCreatId(actorUserId);
            }
            if (isEmpty(request.getChargerId())) {
                request.setChargerId(actorUserId);
            }
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.createForMngr(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("srvcRsponsNo", request.getSrvcRsponsNo());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        trimSubject(request);
        ensureRequestDateForUpdate(request);
        ensureResponseDate(request);
        ensureProcessDate(request);
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.update(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/request")
    public ResponseEntity<Map<String, Object>> updateRequest(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        SrvcRsponsVO search = new SrvcRsponsVO();
        search.setSrvcRsponsNo(srvcRsponsNo);
        SrvcRsponsVO existing = srvcRsponsService.retrieve(search);
        if (existing != null && existing.getRspons1stDt() != null) {
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("error", "SR already received; request update is not allowed.");
            return ResponseEntity.status(409).body(response);
        }

        trimSubject(request);
        ensureRequestDateForUpdate(request);
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateRequst(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/receive")
    public ResponseEntity<Map<String, Object>> updateReceive(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request.getRspons1stDt() == null) {
            if (hasDateTime(request.getRspons1stDtDateDisplay(), request.getRspons1stDtTimeDisplay())) {
                request.makeRspons1stDt();
            } else {
                request.setRspons1stDt(new Date());
            }
        }
        if (isEmpty(request.getVerifyYn()) && !isEmpty(request.getTrgetSrvcCode())) {
            CmmnCodeVO code = new CmmnCodeVO();
            code.setCmmnCode(request.getTrgetSrvcCode());
            CmmnCodeVO resolved = cmmnCodeService.retrieve(code);
            if (resolved != null && "Z1".equals(resolved.getCmmnCodeSubNm1())) {
                request.setVerifyYn("Y");
            } else {
                request.setVerifyYn("N");
            }
        }

        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateReceive(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/response-first")
    public ResponseEntity<Map<String, Object>> updateResponseFirst(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (isEmpty(request.getProcessStdrCode())) {
            request.setProcessStdrCode(SrvcRsponsVO.DEFAULT_PROCESS_STDR_CODE);
        }
        if (isEmpty(request.getChangeDfflyCode())) {
            request.setChangeDfflyCode(SrvcRsponsVO.DEFAULT_CHANGE_DFFLY_CODE);
        }
        if (isEmpty(request.getSrvcRsponsClCode())) {
            request.setSrvcRsponsClCode(SrvcRsponsVO.SRVC_RSPONS_CL_CODE_S102);
        }
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateRspons1st(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/process")
    public ResponseEntity<Map<String, Object>> updateProcess(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateSrProcess(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/verify")
    public ResponseEntity<Map<String, Object>> updateVerify(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request.getVerifyDt() == null) {
            if (hasDateTime(request.getVerifyDtDateDisplay(), request.getVerifyDtTimeDisplay())) {
                request.makeVerifyDt();
            } else {
                request.setVerifyDt(new Date());
            }
        }

        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateSrVerify(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/finish")
    public ResponseEntity<Map<String, Object>> updateFinish(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (request.getFinishDt() == null) {
            if (hasDateTime(request.getFinishDtDateDisplay(), request.getFinishDtTimeDisplay())) {
                request.makeFinishDt();
            } else {
                request.setFinishDt(new Date());
            }
        }

        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateSrFinish(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{srvcRsponsNo}/evaluation")
    public ResponseEntity<Map<String, Object>> updateEvaluation(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateSrEv(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{srvcRsponsNo}/re-request")
    public ResponseEntity<Map<String, Object>> reRequest(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestBody SrvcRsponsVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        trimSubject(request);
        ensureRequestDate(request);
        ensureRequestAttachmentIds(request);
        request.setReSrvcRsponsNo(srvcRsponsNo);

        if (isEmpty(request.getSrvcRsponsBasisCode())) {
            request.setSrvcRsponsBasisCode(SrvcRsponsVO.SRVC_RSPONS_BASIS_CODE_S306);
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            if (isEmpty(request.getCreatId())) {
                request.setCreatId(actorUserId);
            }
            if (isEmpty(request.getRqesterId())) {
                request.setRqesterId(actorUserId);
            }
            request.setUpdtId(actorUserId);
        }

        srvcRsponsService.updateSrEvReRequest(request);
        srvcRsponsService.createSrReRequest(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("srvcRsponsNo", request.getSrvcRsponsNo());
        response.put("reSrvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{srvcRsponsNo}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("srvcRsponsNo") String srvcRsponsNo,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        SrvcRsponsVO request = new SrvcRsponsVO();
        request.setSrvcRsponsNo(srvcRsponsNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }
        srvcRsponsService.delete(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("srvcRsponsNo", srvcRsponsNo);
        return ResponseEntity.ok(response);
    }

    private void trimSubject(SrvcRsponsVO request) {
        if (request.getSrvcRsponsSj() != null) {
            request.setSrvcRsponsSj(request.getSrvcRsponsSj()
                    .substring(0, Math.min(request.getSrvcRsponsSj().length(), 40)));
        }
    }

    private void ensureRequestDate(SrvcRsponsVO request) {
        if (request.getRequstDt() != null) {
            return;
        }
        if (hasDateTime(request.getRequstDtDateDisplay(), request.getRequstDtTimeDisplay())) {
            request.makeRequstDt();
            return;
        }
        request.setRequstDt(new Date());
    }

    private void ensureRequestDateForUpdate(SrvcRsponsVO request) {
        if (request.getRequstDt() != null) {
            return;
        }
        if (hasDateTime(request.getRequstDtDateDisplay(), request.getRequstDtTimeDisplay())) {
            request.makeRequstDt();
        }
    }

    private void ensureResponseDate(SrvcRsponsVO request) {
        if (request.getRspons1stDt() != null) {
            return;
        }
        if (hasDateTime(request.getRspons1stDtDateDisplay(), request.getRspons1stDtTimeDisplay())) {
            request.makeRspons1stDt();
        }
    }

    private void ensureProcessDate(SrvcRsponsVO request) {
        if (request.getProcessDt() != null) {
            return;
        }
        if (hasDateTime(request.getProcessDtDateDisplay(), request.getProcessDtTimeDisplay())) {
            request.makeProcessDt();
        }
    }

    private void ensureRequestAttachmentIds(SrvcRsponsVO request) {
        if (isEmpty(request.getRequstAtchmnflId())) {
            request.setRequstAtchmnflId(UUID.randomUUID().toString());
        }
        if (isEmpty(request.getRsponsAtchmnflId())) {
            request.setRsponsAtchmnflId(UUID.randomUUID().toString());
        }
    }

    private boolean hasDateTime(String date, String time) {
        return date != null && !date.isEmpty() && time != null && !time.isEmpty();
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private Paging applyPaging(SrvcRsponsVO search) {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);
        return new Paging(pageIndex, recordCount);
    }

    private Map<String, Object> buildPagedResponse(List<SrvcRsponsVO> list, int totalCount, Paging paging) {
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", paging.pageIndex);
        pagination.put("recordCountPerPage", paging.recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }

    private static class Paging {
        private final int pageIndex;
        private final int recordCount;

        private Paging(int pageIndex, int recordCount) {
            this.pageIndex = pageIndex;
            this.recordCount = recordCount;
        }
    }
}
