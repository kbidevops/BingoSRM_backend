package kr.go.smplatform.itsm.repAttd.web;

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
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.repAttd.service.RepAttdService;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

@RestController
@RequestMapping("/api/v1/report-attendance")
public class RepAttdApiController {
    private final RepAttdService repAttdService;

    public RepAttdApiController(RepAttdService repAttdService) {
        this.repAttdService = repAttdService;
    }

    @GetMapping
    public ResponseEntity<?> list(RepAttdVO search) throws Exception {
        if (!ensureAttdDt(search)) {
            return ResponseEntity.badRequest().body(error("attdDtDisplay (yyyy-MM-dd) is required"));
        }
        List<RepAttdVO> list = repAttdService.retrieveList(search);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> upsert(
            @RequestBody RepAttdVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (!ensureAttdDt(request) || request.getUserId() == null || request.getUserId().isEmpty()) {
            return ResponseEntity.badRequest().body(error("userId and attdDtDisplay (yyyy-MM-dd) are required"));
        }

        RepAttdVO existing = repAttdService.retrieve(request);
        if (existing != null) {
            if (actorUserId != null && !actorUserId.isEmpty()) {
                request.setUpdtId(actorUserId);
            }
            repAttdService.update(request);
        } else {
            if (actorUserId != null && !actorUserId.isEmpty()) {
                request.setCreatId(actorUserId);
            }
            repAttdService.createOne(request);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", existing != null);
        response.put("created", existing == null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> delete(RepAttdVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (!ensureAttdDt(request) || request.getUserId() == null || request.getUserId().isEmpty()) {
            return ResponseEntity.badRequest().body(error("userId and attdDtDisplay (yyyy-MM-dd) are required"));
        }
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }
        repAttdService.deleteOne(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }

    private boolean ensureAttdDt(RepAttdVO request) {
        if (request.getAttdDt() != null) {
            return true;
        }
        String attdDtDisplay = request.getAttdDtDisplay();
        if (attdDtDisplay == null || attdDtDisplay.isEmpty()) {
            return false;
        }
        request.setAttdDtDisplay(attdDtDisplay);
        return request.getAttdDt() != null;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("error", message);
        return response;
    }
}
