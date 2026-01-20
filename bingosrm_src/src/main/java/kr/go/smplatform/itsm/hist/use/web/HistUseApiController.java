package kr.go.smplatform.itsm.hist.use.web;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.hist.use.service.HistUseService;
import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;

@RestController
@RequestMapping("/api/v1/hist/use")
public class HistUseApiController {
    private final HistUseService histUseService;

    public HistUseApiController(HistUseService histUseService) {
        this.histUseService = histUseService;
    }

    @PostMapping
    public Map<String, Object> create(
            @RequestBody HistUseVO request,
            HttpServletRequest httpRequest,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId,
            @RequestHeader(value = "X-User-Ty-Code", required = false) String actorUserTyCode,
            @RequestHeader(value = "X-Request-Uri", required = false) String requestUriHeader,
            @RequestHeader(value = "X-Request-Method", required = false) String requestMethodHeader) throws Exception {
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
        if ((request.getRequestUri() == null || request.getRequestUri().isEmpty()) && requestUriHeader != null
                && !requestUriHeader.isEmpty()) {
            request.setRequestUri(requestUriHeader);
        }
        if ((request.getRequestMethod() == null || request.getRequestMethod().isEmpty()) && requestMethodHeader != null
                && !requestMethodHeader.isEmpty()) {
            request.setRequestMethod(requestMethodHeader);
        }
        if (request.getRequestUri() == null || request.getRequestUri().isEmpty()) {
            request.setRequestUri(httpRequest.getRequestURI());
        }
        if (request.getRequestMethod() == null || request.getRequestMethod().isEmpty()) {
            request.setRequestMethod(httpRequest.getMethod());
        }

        histUseService.create(request);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", true);
        response.put("sessionId", request.getSessionId());
        return response;
    }
}
