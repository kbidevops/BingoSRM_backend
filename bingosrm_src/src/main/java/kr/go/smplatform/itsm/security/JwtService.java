package kr.go.smplatform.itsm.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    private final Key signingKey;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    public JwtService(@Value("${itsm.security.jwt.secret}") String secret,
                      @Value("${itsm.security.jwt.access-token-exp-seconds:3600}") long accessTokenExpirySeconds,
                      @Value("${itsm.security.jwt.refresh-token-exp-seconds:604800}") long refreshTokenExpirySeconds) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public String generateAccessToken(String userId, String userTyCode) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim("userTyCode", userTyCode)
                .claim("tokenType", "access")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTokenExpirySeconds)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseAccessToken(String token) throws JwtException {
        Claims claims = parseToken(token);
        validateTokenType(claims, "access");
        return claims;
    }

    public String generateRefreshToken(String userId, String userTyCode) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim("userTyCode", userTyCode)
                .claim("tokenType", "refresh")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshTokenExpirySeconds)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseRefreshToken(String token) throws JwtException {
        Claims claims = parseToken(token);
        validateTokenType(claims, "refresh");
        return claims;
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring("Bearer ".length()).trim();
    }

    public long getAccessTokenExpirySeconds() {
        return accessTokenExpirySeconds;
    }

    public long getRefreshTokenExpirySeconds() {
        return refreshTokenExpirySeconds;
    }

    private Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void validateTokenType(Claims claims, String expectedType) throws JwtException {
        Object tokenType = claims.get("tokenType");
        if (tokenType == null || !expectedType.equals(tokenType.toString())) {
            throw new JwtException("Invalid token type.");
        }
    }
}
