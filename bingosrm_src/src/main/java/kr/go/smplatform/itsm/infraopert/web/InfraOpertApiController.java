package kr.go.smplatform.itsm.infraopert.web;

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

import kr.go.smplatform.itsm.infraopert.service.InfraOpertService;
import kr.go.smplatform.itsm.infraopert.vo.InfraOpertVO;
import kr.go.smplatform.itsm.srvcrspons.service.SrvcRsponsService;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

@RestController
@RequestMapping("/api/v1/infra-operations")
public class InfraOpertApiController {
    private final InfraOpertService infraOpertService;
    private final SrvcRsponsService srvcRsponsService;

    public InfraOpertApiController(InfraOpertService infraOpertService, SrvcRsponsService srvcRsponsService) {
        this.infraOpertService = infraOpertService;
        this.srvcRsponsService = srvcRsponsService;
    }

    @GetMapping
    public Map<String, Object> list(SrvcRsponsVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setDeleteYn("N");
        search.setInfraOpertYn("Y");
        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        List<SrvcRsponsVO> list = srvcRsponsService.retrieveInfraOpertPagingList(search);
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

    @GetMapping("/{infraOpertNo}")
    public InfraOpertVO get(@PathVariable("infraOpertNo") String infraOpertNo) throws Exception {
        InfraOpertVO vo = new InfraOpertVO();
        vo.setInfraOpertNo(infraOpertNo);
        return infraOpertService.retrieve(vo);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody InfraOpertRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        InfraOpertVO infra = request.getInfraOpert();
        if (actorUserId != null && !actorUserId.isEmpty()) {
            infra.setCreatId(actorUserId);
        }
        infraOpertService.create(infra);

        if (request.getSrvcRsponsNos() != null) {
            SrvcRsponsVO srvc = new SrvcRsponsVO();
            for (String srvcRsponsNo : request.getSrvcRsponsNos()) {
                srvc.setSrvcRsponsNo(srvcRsponsNo);
                srvc.setInfraOpertNo(infra.getInfraOpertNo());
                srvc.setUpdtId(actorUserId);
                srvcRsponsService.updateInfraOpert(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("infraOpertNo", infra.getInfraOpertNo());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{infraOpertNo}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("infraOpertNo") String infraOpertNo,
            @RequestBody InfraOpertRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        InfraOpertVO infra = request.getInfraOpert();
        infra.setInfraOpertNo(infraOpertNo);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            infra.setUpdtId(actorUserId);
        }
        infraOpertService.update(infra);

        String linkInfraNo = infra.getInfraOpertNo_sub() != null && !infra.getInfraOpertNo_sub().isEmpty()
                ? infra.getInfraOpertNo_sub()
                : infraOpertNo;

        if (request.getSrvcRsponsNos() != null) {
            SrvcRsponsVO srvc = new SrvcRsponsVO();
            for (String srvcRsponsNo : request.getSrvcRsponsNos()) {
                srvc.setSrvcRsponsNo(srvcRsponsNo);
                srvc.setInfraOpertNo(linkInfraNo);
                srvc.setUpdtId(actorUserId);
                srvcRsponsService.updateInfraOpert(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("infraOpertNo", infraOpertNo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{infraOpertNo}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("infraOpertNo") String infraOpertNo,
            @RequestBody InfraOpertRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        InfraOpertVO infra = new InfraOpertVO();
        infra.setInfraOpertNo(infraOpertNo);
        infra.setUpdtId(actorUserId);
        infraOpertService.delete(infra);

        if (request.getSrvcRsponsNos() != null) {
            SrvcRsponsVO srvc = new SrvcRsponsVO();
            srvc.setUpdtId(actorUserId);
            for (String srvcRsponsNo : request.getSrvcRsponsNos()) {
                srvc.setSrvcRsponsNo(srvcRsponsNo);
                srvcRsponsService.deleteInfraOpert(srvc);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("infraOpertNo", infraOpertNo);
        return ResponseEntity.ok(response);
    }

    public static class InfraOpertRequest {
        private InfraOpertVO infraOpert;
        private List<String> srvcRsponsNos;

        public InfraOpertVO getInfraOpert() {
            return infraOpert;
        }

        public void setInfraOpert(InfraOpertVO infraOpert) {
            this.infraOpert = infraOpert;
        }

        public List<String> getSrvcRsponsNos() {
            return srvcRsponsNos;
        }

        public void setSrvcRsponsNos(List<String> srvcRsponsNos) {
            this.srvcRsponsNos = srvcRsponsNos;
        }
    }
}
