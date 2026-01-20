package kr.go.smplatform.itsm.funcimprvm.web;

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

import kr.go.smplatform.itsm.funcimprvm.service.FuncImprvmService;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

@RestController
@RequestMapping("/api/v1/func-improvements")
public class FuncImprvmApiController {
    private final FuncImprvmService funcImprvmService;
    private final SrvcRsponsService srvcRsponsService;

    public FuncImprvmApiController(FuncImprvmService funcImprvmService, SrvcRsponsService srvcRsponsService) {
        this.funcImprvmService = funcImprvmService;
        this.srvcRsponsService = srvcRsponsService;
    }

    @GetMapping
    public Map<String, Object> list(FuncImprvmVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setDeleteYn("N");
        search.setProgrmUpdtYn("Y");
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        List<SrvcRsponsVO> list = srvcRsponsService.retrievefnctImprvmPagingList(search);
        int totalCount = srvcRsponsService.retrievePagingListCnt(search);

        Map<String, FuncImprvmVO> resultMap = new HashMap<String, FuncImprvmVO>();
        for (SrvcRsponsVO srvc : list) {
            if (srvc.getFnctImprvmNo() == null || srvc.getFnctImprvmNo().isEmpty()) {
                continue;
            }
            FuncImprvmVO retrieveVO = new FuncImprvmVO();
            retrieveVO.setFnctImprvmNo(srvc.getFnctImprvmNo());
            resultMap.put(srvc.getSrvcRsponsNo(), funcImprvmService.retrieve(retrieveVO));
        }

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("resultMap", resultMap);
        response.put("pagination", pagination);
        return response;
    }

    @GetMapping("/check")
    public Map<String, Object> check(FuncImprvmVO search) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        if (search.getProcessMt() != null && !search.getProcessMt().isEmpty()) {
            List<FuncImprvmVO> list = funcImprvmService.retrieveList(search);
            if (list.isEmpty()) {
                response.put("returnMessage", "empty");
            } else {
                response.put("resultList", list);
            }
            return response;
        }

        if (search.getFnctImprvmNo() != null && !search.getFnctImprvmNo().isEmpty()) {
            FuncImprvmVO result = funcImprvmService.retrieve(search);
            if (result == null) {
                response.put("returnMessage", "empty");
            } else {
                response.put("result", result);
            }
        }
        return response;
    }

    @GetMapping("/{fnctImprvmNo}")
    public FuncImprvmVO get(@PathVariable("fnctImprvmNo") String fnctImprvmNo) throws Exception {
        FuncImprvmVO vo = new FuncImprvmVO();
        vo.setFnctImprvmNo(fnctImprvmNo);
        return funcImprvmService.retrieve(vo);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody FuncImprvmVO funcImprvm,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (actorUserId != null && !actorUserId.isEmpty()) {
            funcImprvm.setCreatId(actorUserId);
        }

        if (funcImprvm.getFiAtchmnflId() == null || funcImprvm.getFiAtchmnflId().isEmpty()) {
            funcImprvm.setFiAtchmnflId(UUID.randomUUID().toString());
        }
        if (funcImprvm.getAsisAtchmnflId() == null || funcImprvm.getAsisAtchmnflId().isEmpty()) {
            funcImprvm.setAsisAtchmnflId(UUID.randomUUID().toString());
        }
        if (funcImprvm.getTobeAtchmnflId() == null || funcImprvm.getTobeAtchmnflId().isEmpty()) {
            funcImprvm.setTobeAtchmnflId(UUID.randomUUID().toString());
        }

        if (funcImprvm.getApplyPlanDt() == null && funcImprvm.getApplyPlanDtDisplay() != null
                && !funcImprvm.getApplyPlanDtDisplay().isEmpty()) {
            funcImprvm.makeApplyPlanDt();
        }
        if (funcImprvm.getApplyRDt() == null && funcImprvm.getApplyRDtDisplay() != null
                && !funcImprvm.getApplyRDtDisplay().isEmpty()) {
            funcImprvm.makeApplyRDt();
        }

        funcImprvmService.create(funcImprvm);

        if (funcImprvm.getSrvcRsponsNo() != null && !funcImprvm.getSrvcRsponsNo().isEmpty()) {
            SrvcRsponsVO search = new SrvcRsponsVO();
            search.setSrvcRsponsNo(funcImprvm.getSrvcRsponsNo());
            SrvcRsponsVO srvc = srvcRsponsService.retrieve(search);
            if (srvc != null) {
                srvc.setFnctImprvmNo(funcImprvm.getFnctImprvmNo());
                if (actorUserId != null && !actorUserId.isEmpty()) {
                    srvc.setUpdtId(actorUserId);
                }
                srvcRsponsService.update(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("fnctImprvmNo", funcImprvm.getFnctImprvmNo());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{fnctImprvmNo}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("fnctImprvmNo") String fnctImprvmNo,
            @RequestBody FuncImprvmVO funcImprvm,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        funcImprvm.setFnctImprvmNo(fnctImprvmNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            funcImprvm.setUpdtId(actorUserId);
        }
        if (funcImprvm.getApplyPlanDt() == null && funcImprvm.getApplyPlanDtDisplay() != null
                && !funcImprvm.getApplyPlanDtDisplay().isEmpty()) {
            funcImprvm.makeApplyPlanDt();
        }
        if (funcImprvm.getApplyRDt() == null && funcImprvm.getApplyRDtDisplay() != null
                && !funcImprvm.getApplyRDtDisplay().isEmpty()) {
            funcImprvm.makeApplyRDt();
        }

        funcImprvmService.update(funcImprvm);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("fnctImprvmNo", fnctImprvmNo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fnctImprvmNo}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("fnctImprvmNo") String fnctImprvmNo,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        SrvcRsponsVO search = new SrvcRsponsVO();
        search.setFnctImprvmNo(fnctImprvmNo);
        SrvcRsponsVO srvc = srvcRsponsService.retrieve(search);
        if (srvc != null) {
            srvc.setFnctImprvmNo(null);
            if (actorUserId != null && !actorUserId.isEmpty()) {
                srvc.setUpdtId(actorUserId);
            }
            srvcRsponsService.update(srvc);
        }

        FuncImprvmVO funcImprvm = new FuncImprvmVO();
        funcImprvm.setFnctImprvmNo(fnctImprvmNo);
        funcImprvmService.delete(funcImprvm);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("fnctImprvmNo", fnctImprvmNo);
        return ResponseEntity.ok(response);
    }
}
