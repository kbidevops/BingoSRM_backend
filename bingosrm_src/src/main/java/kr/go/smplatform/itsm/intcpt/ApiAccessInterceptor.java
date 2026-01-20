package kr.go.smplatform.itsm.intcpt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

@Component
public class ApiAccessInterceptor implements HandlerInterceptor {
    private final ProgrmAccesAuthorService progrmAccesAuthorService;

    @Value("${itsm.security.enforce-permissions:false}")
    private boolean enforcePermissions;

    public ApiAccessInterceptor(ProgrmAccesAuthorService progrmAccesAuthorService) {
        this.progrmAccesAuthorService = progrmAccesAuthorService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!enforcePermissions || isExcluded(request)) {
            return true;
        }

        String userTyCode = request.getHeader("X-User-Ty-Code");
        if (userTyCode == null || userTyCode.isEmpty()) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Missing X-User-Ty-Code header.");
            return false;
        }

        ProgrmAccesAuthorVO param = new ProgrmAccesAuthorVO();
        param.setProgrmAccesAuthorCode(userTyCode);
        List<ProgrmAccesAuthorVO> assigned = progrmAccesAuthorService.retrieveAssignList(param);
        if (assigned == null || assigned.isEmpty()) {
            writeError(response, HttpStatus.FORBIDDEN, "No program access assigned.");
            return false;
        }

        String path = normalizePath(request);
        for (ProgrmAccesAuthorVO progrm : assigned) {
            String progrmUri = progrm.getProgrmUri();
            if (progrmUri == null || progrmUri.isEmpty()) {
                continue;
            }
            if (path.startsWith(progrmUri)) {
                return true;
            }
        }

        writeError(response, HttpStatus.FORBIDDEN, "Access denied for path.");
        return false;
    }

    private boolean isExcluded(HttpServletRequest request) {
        String path = normalizePath(request);
        return path.startsWith("/api/v1/auth/")
                || path.startsWith("/api/v1/hist/")
                || path.startsWith("/actuator")
                || path.startsWith("/error");
    }

    private String normalizePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            return path.substring(contextPath.length());
        }
        return path;
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
