package kr.go.smplatform.itsm.repcharger.web;

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

import kr.go.smplatform.itsm.repcharger.service.RepChargerService;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;

@RestController
@RequestMapping("/api/v1/report-chargers")
public class RepChargerApiController {
    private final RepChargerService repChargerService;

    public RepChargerApiController(RepChargerService repChargerService) {
        this.repChargerService = repChargerService;
    }

    @GetMapping
    public List<RepChargerVO> list(@RequestParam("userId") String userId) throws Exception {
        RepChargerVO vo = new RepChargerVO();
        vo.setUserId(userId);
        return repChargerService.retrieveList(vo);
    }

    @GetMapping("/assigned")
    public List<RepChargerVO> assigned(@RequestParam(value = "userId", required = false) String userId)
            throws Exception {
        RepChargerVO vo = new RepChargerVO();
        vo.setUserId(userId);
        return repChargerService.retrieveAssignList(vo);
    }

    @GetMapping("/users")
    public List<RepChargerVO> users(
            @RequestParam(value = "userLocat", required = false) String userLocat,
            @RequestParam(value = "reportCharger", required = false, defaultValue = "false") boolean reportCharger)
            throws Exception {
        RepChargerVO vo = new RepChargerVO();
        vo.setUserLocat(userLocat);
        vo.setReportCharger(reportCharger);
        return repChargerService.retrieveUsers(vo);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("userId") String userId,
            @RequestBody RepChargerUpdateRequest request) throws Exception {
        RepChargerVO vo = new RepChargerVO();
        vo.setUserId(userId);
        vo.setSysCodes(request.getSysCodes());
        repChargerService.create(vo);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    public static class RepChargerUpdateRequest {
        private List<String> sysCodes;

        public List<String> getSysCodes() {
            return sysCodes;
        }

        public void setSysCodes(List<String> sysCodes) {
            this.sysCodes = sysCodes;
        }
    }
}
