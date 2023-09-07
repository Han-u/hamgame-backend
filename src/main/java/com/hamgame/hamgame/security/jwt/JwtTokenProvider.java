package com.hamgame.hamgame.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hamgame.hamgame.security.auth.CustomUserDetailsService;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Component
@Log4j2
public class JwtTokenProvider implements InitializingBean {

	@Value("${spring.jwt.access.expiration}")
	private long accessTokenExpire;

	@Value("${spring.jwt.refresh.expiration}")
	private long refreshTokenExpire;

	@Value("${spring.jwt.token.secret-key}")
	private String secretKey;

	private Key key;

	private final CustomUserDetailsService customUserDetailsService;

	/*
	 * Bean 생성되고 의존성 주입까지 끝낸 후 주입받은 secretkey를 base64 decode하여
	 * 키 바이트 배열로 SecretKey 인스턴스 생성
	 */
	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Claims claims, Long expire) {
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expire))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	// Access Token 생성
	public String createAccessToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Claims claims = Jwts.claims().setSubject(Long.toString(userPrincipal.getId()));
		claims.put("email", userPrincipal.getEmail());

		return createToken(claims, accessTokenExpire);
	}

	// Refresh Token 생성
	public String createRefreshToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Claims claims = Jwts.claims().setSubject(Long.toString(userPrincipal.getId()));

		return createToken(claims, refreshTokenExpire);
	}

	// Token에서 Id 추출
	public Long getUserIdFromToken(String token) {
		Claims claims = getClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	// Token에서 email 추출
	public String getUserEmailFromToken(String token) {
		Claims claims = getClaims(token);
		return (String)claims.get("email");
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	// JWT 토큰을 Decode하여 Authentication 반환
	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		Long userId = getUserIdFromToken(token);
		String email = getUserEmailFromToken(token);

		UserDetails userPrincipal = customUserDetailsService.loadUserByIdAndEmail(userId, email);
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			log.error("유효하지 않은 JWT 서명");
		} catch (MalformedJwtException e) {
			log.error("유효하지 않은 JWT");
		} catch (ExpiredJwtException e) {
			log.error("만료된 JWT");
		} catch (IllegalArgumentException e) {
			System.out.println("illegal");
			log.error("잘못된 토큰");
		} catch (UnsupportedJwtException e) {
			System.out.println("unsopprted");
			log.error("지원하지 않는 JWT");
		}
		return false;
	}
}
