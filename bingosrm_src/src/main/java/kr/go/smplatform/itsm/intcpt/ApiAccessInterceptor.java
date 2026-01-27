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
import kr.go.smplatform.itsm.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class ApiAccessInterceptor implements HandlerInterceptor {
    private final ProgrmAccesAuthorService progrmAccesAuthorService;
    private final JwtService jwtService;

    @Value("${itsm.security.enforce-permissions:false}")
    private boolean enforcePermissions;

    public ApiAccessInterceptor(ProgrmAccesAuthorService progrmAccesAuthorService, JwtService jwtService) {
        this.progrmAccesAuthorService = progrmAccesAuthorService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isExcluded(request)) {
            return true;
        }

        String token = jwtService.resolveToken(request);
        if (token == null) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Missing Authorization token.");
            return false;
        }

        String userTyCode = null;
        try {
            Claims claims = jwtService.parseAccessToken(token);
            Object claimValue = claims.get("userTyCode");
            if (claimValue != null) {
                userTyCode = claimValue.toString();
            }
        } catch (JwtException ex) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Invalid or expired token.");
            return false;
        }

        if ((userTyCode == null || userTyCode.isEmpty()) && enforcePermissions) {
            userTyCode = request.getHeader("X-User-Ty-Code");
        }
        if (userTyCode == null || userTyCode.isEmpty()) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Missing user type code.");
            return false;
        }

        if (!enforcePermissions) {
            return true;
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
