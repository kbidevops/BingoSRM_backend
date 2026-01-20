package kr.go.smplatform.itsm.syscharger.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

@RestController
@RequestMapping("/api/v1/sys-chargers")
public class SysChargerApiController {
    private final SysChargerService sysChargerService;

    public SysChargerApiController(SysChargerService sysChargerService) {
        this.sysChargerService = sysChargerService;
    }

    @GetMapping
    public ResponseEntity<List<SysChargerVO>> list(@RequestParam(value = "userId", required = false) String userId)
            throws Exception {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        SysChargerVO vo = new SysChargerVO();
        vo.setUserId(userId);
        return ResponseEntity.ok(sysChargerService.retrieveList(vo));
    }

    @GetMapping("/assigned")
    public List<SysChargerVO> assigned(@RequestParam(value = "userId", required = false) String userId) throws Exception {
        SysChargerVO vo = new SysChargerVO();
        vo.setUserId(userId);
        return sysChargerService.retrieveAssignList(vo);
    }

    @GetMapping("/by-system")
    public List<SysChargerVO> chargersBySystem(@RequestParam("sysCode") String sysCode) throws Exception {
        SysChargerVO vo = new SysChargerVO();
        vo.setSysCode(sysCode);
        return sysChargerService.retrieveChargerList(vo);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("userId") String userId,
            @RequestBody SysChargerUpdateRequest request) throws Exception {
        SysChargerVO vo = new SysChargerVO();
        vo.setUserId(userId);
        vo.setSysCodes(request.getSysCodes() == null ? new String[0] : request.getSysCodes());
        sysChargerService.createList(vo);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", true);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    public static class SysChargerUpdateRequest {
        private String[] sysCodes;

        public String[] getSysCodes() {
            return sysCodes;
        }

        public void setSysCodes(String[] sysCodes) {
            this.sysCodes = sysCodes;
        }
    }
}
