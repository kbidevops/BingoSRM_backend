package kr.go.smplatform.itsm.wdtb.web;

import java.util.ArrayList;
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

import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import kr.go.smplatform.itsm.wdtb.service.WdtbService;
import kr.go.smplatform.itsm.wdtb.vo.WdtbVO;

@RestController
@RequestMapping("/api/v1/wdtb")
public class WdtbApiController {
    private final WdtbService wdtbService;
    private final SrvcRsponsService srvcRsponsService;

    public WdtbApiController(WdtbService wdtbService, SrvcRsponsService srvcRsponsService) {
        this.wdtbService = wdtbService;
        this.srvcRsponsService = srvcRsponsService;
    }

    @GetMapping
    public Map<String, Object> list(WdtbVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setDeleteYn("N");
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        if (search.getWdtbDtDateDisplay() != null && !search.getWdtbDtDateDisplay().isEmpty()) {
            search.makeWdtbDt();
        }

        List<SrvcRsponsVO> list = srvcRsponsService.retrieveWdtbPagingList(search);
        int totalCount = srvcRsponsService.retrievePagingListCnt(search);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }

    @GetMapping("/check")
    public Map<String, Object> check(WdtbVO search) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        if (search.getProcessMt() != null && !search.getProcessMt().isEmpty()) {
            List<WdtbVO> list = wdtbService.retrieveList(search);
            if (list.isEmpty()) {
                response.put("returnMessage", "empty");
            } else {
                response.put("resultList", list);
            }
            return response;
        }

        if (search.getWdtbCnfirmNo() != null && !search.getWdtbCnfirmNo().isEmpty()) {
            WdtbVO result = wdtbService.retrieve(search);
            if (result == null) {
                response.put("returnMessage", "empty");
            } else {
                response.put("result", result);
            }
        }
        return response;
    }

    @GetMapping("/{wdtbCnfirmNo}")
    public Map<String, Object> get(@PathVariable("wdtbCnfirmNo") String wdtbCnfirmNo) throws Exception {
        WdtbVO search = new WdtbVO();
        search.setWdtbCnfirmNo(wdtbCnfirmNo);
        WdtbVO wdtb = wdtbService.retrieve(search);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("wdtb", wdtb);

        if (wdtb != null) {
            SrvcRsponsVO srvcSearch = new SrvcRsponsVO();
            srvcSearch.setWdtbCnfirmNo(wdtbCnfirmNo);
            response.put("srvcRsponsList", srvcRsponsService.retrieveAllList(srvcSearch));
        }

        return response;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody WdtbRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        WdtbVO wdtb = request.getWdtb();
        if (actorUserId != null && !actorUserId.isEmpty()) {
            wdtb.setCreatId(actorUserId);
        }

        if (wdtb.getSolutConectflId() == null || wdtb.getSolutConectflId().isEmpty()) {
            wdtb.setSolutConectflId(UUID.randomUUID().toString());
        }
        if (wdtb.getOpertResultflId() == null || wdtb.getOpertResultflId().isEmpty()) {
            wdtb.setOpertResultflId(UUID.randomUUID().toString());
        }
        if (wdtb.getLoginResultflId() == null || wdtb.getLoginResultflId().isEmpty()) {
            wdtb.setLoginResultflId(UUID.randomUUID().toString());
        }
        if (wdtb.getServerOneLogflId() == null || wdtb.getServerOneLogflId().isEmpty()) {
            wdtb.setServerOneLogflId(UUID.randomUUID().toString());
        }
        if (wdtb.getServerTwoLogflId() == null || wdtb.getServerTwoLogflId().isEmpty()) {
            wdtb.setServerTwoLogflId(UUID.randomUUID().toString());
        }

        if (wdtb.getWdtbDt() == null && wdtb.getWdtbDtDateDisplay() != null
                && !wdtb.getWdtbDtDateDisplay().isEmpty()) {
            wdtb.makeWdtbDt();
        }
        wdtb.makeWdtbDtOne();
        wdtb.makeWdtbDtTwo();

        wdtbService.create(wdtb);

        if (request.getSrvcRsponsNos() != null) {
            SrvcRsponsVO srvc = new SrvcRsponsVO();
            for (String srvcRsponsNo : request.getSrvcRsponsNos()) {
                srvc.setSrvcRsponsNo(srvcRsponsNo);
                srvc.setWdtbCnfirmNo(wdtb.getWdtbCnfirmNo());
                srvc.setUpdtId(actorUserId);
                srvcRsponsService.updateWdtbCnfirm(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("wdtbCnfirmNo", wdtb.getWdtbCnfirmNo());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{wdtbCnfirmNo}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("wdtbCnfirmNo") String wdtbCnfirmNo,
            @RequestBody WdtbRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        WdtbVO wdtb = request.getWdtb();
        wdtb.setWdtbCnfirmNo(wdtbCnfirmNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            wdtb.setUpdtId(actorUserId);
        }

        if (wdtb.getWdtbDt() == null && wdtb.getWdtbDtDateDisplay() != null
                && !wdtb.getWdtbDtDateDisplay().isEmpty()) {
            wdtb.makeWdtbDt();
        }
        wdtb.makeWdtbDtOne();
        wdtb.makeWdtbDtTwo();

        wdtbService.update(wdtb);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("wdtbCnfirmNo", wdtbCnfirmNo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wdtbCnfirmNo}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("wdtbCnfirmNo") String wdtbCnfirmNo,
            @RequestBody(required = false) WdtbRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        WdtbVO wdtb = new WdtbVO();
        wdtb.setWdtbCnfirmNo(wdtbCnfirmNo);
        wdtb.setUpdtId(actorUserId);
        wdtbService.delete(wdtb);

        List<String> srvcRsponsNos = resolveSrvcRsponsNos(wdtbCnfirmNo, request != null ? request.getSrvcRsponsNos() : null);
        if (!srvcRsponsNos.isEmpty()) {
            SrvcRsponsVO srvc = new SrvcRsponsVO();
            srvc.setUpdtId(actorUserId);
            for (String srvcRsponsNo : srvcRsponsNos) {
                srvc.setSrvcRsponsNo(srvcRsponsNo);
                srvcRsponsService.deleteWdtbCnfirm(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("wdtbCnfirmNo", wdtbCnfirmNo);
        return ResponseEntity.ok(response);
    }

    private List<String> resolveSrvcRsponsNos(String wdtbCnfirmNo, List<String> requestNos) throws Exception {
        if (requestNos != null && !requestNos.isEmpty()) {
            return requestNos;
        }
        SrvcRsponsVO search = new SrvcRsponsVO();
        search.setWdtbCnfirmNo(wdtbCnfirmNo);
        List<SrvcRsponsVO> srvcList = srvcRsponsService.retrieveAllList(search);
        List<String> srvcNos = new ArrayList<String>();
        for (SrvcRsponsVO srvc : srvcList) {
            srvcNos.add(srvc.getSrvcRsponsNo());
        }
        return srvcNos;
    }

    public static class WdtbRequest {
        private WdtbVO wdtb;
        private List<String> srvcRsponsNos;

        public WdtbVO getWdtb() {
            return wdtb;
        }

        public void setWdtb(WdtbVO wdtb) {
            this.wdtb = wdtb;
        }

        public List<String> getSrvcRsponsNos() {
            return srvcRsponsNos;
        }

        public void setSrvcRsponsNos(List<String> srvcRsponsNos) {
            this.srvcRsponsNos = srvcRsponsNos;
        }
    }
}
