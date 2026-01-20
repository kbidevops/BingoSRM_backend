package kr.go.smplatform.itsm.progrm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.progrm.service.ProgrmService;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

@RestController
@RequestMapping("/api/v1/programs")
public class ProgrmApiController {
    private final ProgrmService progrmService;

    public ProgrmApiController(ProgrmService progrmService) {
        this.progrmService = progrmService;
    }

    @GetMapping
    public List<ProgrmVO> list(ProgrmVO search) throws Exception {
        if (search.getDeleteYn() == null || search.getDeleteYn().isEmpty()) {
            search.setDeleteYn("N");
        }
        return progrmService.retrieveList(search);
    }

    @GetMapping("/tree")
    public List<ProgrmVO> tree() throws Exception {
        ProgrmVO search = new ProgrmVO();
        search.setDeleteYn("N");
        return progrmService.retrieveList(search);
    }

    @GetMapping("/{progrmSn}")
    public ProgrmVO detail(@PathVariable("progrmSn") String progrmSn) throws Exception {
        ProgrmVO search = new ProgrmVO();
        search.setProgrmSn(progrmSn);
        return progrmService.retrieve(search);
    }

    @PostMapping
    public Map<String, Object> create(
            @RequestBody ProgrmVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setCreatId(actorUserId);
            request.setUpdtId(actorUserId);
        }
        if (request.getDeleteYn() == null || request.getDeleteYn().isEmpty()) {
            request.setDeleteYn("N");
        }

        progrmService.create(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("progrmSn", request.getProgrmSn());
        return response;
    }

    @PutMapping("/{progrmSn}")
    public Map<String, Object> update(
            @PathVariable("progrmSn") String progrmSn,
            @RequestBody ProgrmVO request,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        request.setProgrmSn(progrmSn);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        progrmService.update(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("progrmSn", progrmSn);
        return response;
    }

    @DeleteMapping("/{progrmSn}")
    public Map<String, Object> delete(
            @PathVariable("progrmSn") String progrmSn,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        ProgrmVO request = new ProgrmVO();
        request.setProgrmSn(progrmSn);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            request.setUpdtId(actorUserId);
        }

        progrmService.delete(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        response.put("progrmSn", progrmSn);
        return response;
    }
}
