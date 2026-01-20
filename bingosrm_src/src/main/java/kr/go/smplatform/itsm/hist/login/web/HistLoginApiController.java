package kr.go.smplatform.itsm.hist.login.web;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.hist.login.service.HistLoginService;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;

@RestController
@RequestMapping("/api/v1/hist/login")
public class HistLoginApiController {
    private final HistLoginService histLoginService;

    public HistLoginApiController(HistLoginService histLoginService) {
        this.histLoginService = histLoginService;
    }

    @PostMapping
    public Map<String, Object> create(
            @RequestBody HistLoginVO request,
            HttpServletRequest httpRequest,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId,
            @RequestHeader(value = "X-User-Ty-Code", required = false) String actorUserTyCode) throws Exception {
        if (request.getSessionId() == null || request.getSessionId().isEmpty()) {
            request.setSessionId(httpRequest.getSession(true).getId());
        }
        if ((request.getUserId() == null || request.getUserId().isEmpty()) && actorUserId != null && !actorUserId.isEmpty()) {
            request.setUserId(actorUserId);
        }
        if ((request.getUserTyCode() == null || request.getUserTyCode().isEmpty())
                && actorUserTyCode != null && !actorUserTyCode.isEmpty()) {
            request.setUserTyCode(actorUserTyCode);
        }
        if (request.getConectIp() == null || request.getConectIp().isEmpty()) {
            request.setConectIp(httpRequest.getRemoteAddr());
        }

        histLoginService.create(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("sessionId", request.getSessionId());
        return response;
    }

    @PutMapping("/logout")
    public Map<String, Object> logout(
            @RequestBody(required = false) HistLoginVO request,
            HttpServletRequest httpRequest) throws Exception {
        HistLoginVO payload = request != null ? request : new HistLoginVO();
        if (payload.getSessionId() == null || payload.getSessionId().isEmpty()) {
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                payload.setSessionId(session.getId());
            }
        }
        if (payload.getLogoutSttusCode() == null || payload.getLogoutSttusCode().isEmpty()) {
            payload.setLogoutSttusCode(HistLoginVO.LOGOUT_STTUS_CODE_NORMAL);
        }

        int updated = 0;
        if (payload.getSessionId() != null && !payload.getSessionId().isEmpty()) {
            updated = histLoginService.update(payload);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("updated", updated > 0);
        response.put("sessionId", payload.getSessionId());
        return response;
    }
}
