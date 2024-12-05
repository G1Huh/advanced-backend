/*
    For JWT (JSON Web Token)
    정의 - 두 시스템 간에 인증 정보를 안전하게 전송하기 위한 토큰 기반 인증 방식
    Base64Url로 인코딩된 JSON 객체
    1. 의존성 추가 - pom.xml
    2. JWT 토큰 생성 및 검증 유틸리티 클래스 작성 (JwtTokenUtil)
    3. JWT 발급 - 로그인 요청이 성공하면 JWT를 발급하여 클라이언트에 전달하는 로직을 구현 (AuthenticationController)
    4. JWT 검증 - 요청이 들어올 때마다 토큰을 검증 (JwtRequestFilter)
    5. Configuration 수정 (SecurityConfig)
    6. AuthenticationRequest, AuthenticationResponse DTO 클래스 작성
 */
package com.lion.demo.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtTokenUtil {
    private String SECRET_KEY = "like_lion_advanced_backend";       // 서버에서만 알고 있는 비밀키
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token).getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // 토큰에서 만료시간 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // 토큰 만료여부 확인
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // 토큰 생성 로직
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))      // 10시간 만료
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    // 토큰 생성
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    // 토큰 유효성 검사
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}