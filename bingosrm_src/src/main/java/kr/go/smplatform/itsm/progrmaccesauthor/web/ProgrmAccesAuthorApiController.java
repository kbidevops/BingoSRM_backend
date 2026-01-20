package kr.go.smplatform.itsm.progrmaccesauthor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@RestController
@RequestMapping("/api/v1/program-access")
public class ProgrmAccesAuthorApiController {
    private final ProgrmAccesAuthorService progrmAccesAuthorService;

    public ProgrmAccesAuthorApiController(ProgrmAccesAuthorService progrmAccesAuthorService) {
        this.progrmAccesAuthorService = progrmAccesAuthorService;
    }

    @GetMapping
    public List<ProgrmAccesAuthorVO> list(@RequestParam("authorCode") String authorCode) throws Exception {
        ProgrmAccesAuthorVO vo = new ProgrmAccesAuthorVO();
        vo.setProgrmAccesAuthorCode(authorCode);
        return progrmAccesAuthorService.retrieveList(vo);
    }

    @GetMapping("/{authorCode}/assigned")
    public List<ProgrmAccesAuthorVO> assigned(@PathVariable("authorCode") String authorCode) throws Exception {
        ProgrmAccesAuthorVO vo = new ProgrmAccesAuthorVO();
        vo.setProgrmAccesAuthorCode(authorCode);
        return progrmAccesAuthorService.retrieveAssignList(vo);
    }

    @PutMapping("/{authorCode}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("authorCode") String authorCode,
            @RequestBody ProgramAccessUpdateRequest request) throws Exception {
        ProgrmAccesAuthorVO vo = new ProgrmAccesAuthorVO();
        vo.setProgrmAccesAuthorCode(authorCode);
        vo.setProgrmSns(request.getProgrmSns() == null ? new String[0] : request.getProgrmSns());
        progrmAccesAuthorService.createList(vo);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("authorCode", authorCode);
        return ResponseEntity.ok(response);
    }

    public static class ProgramAccessUpdateRequest {
        private String[] progrmSns;

        public String[] getProgrmSns() {
            return progrmSns;
        }

        public void setProgrmSns(String[] progrmSns) {
            this.progrmSns = progrmSns;
        }
    }
}
