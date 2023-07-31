package com.hamgame.hamgame.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hamgame.hamgame.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.user.entity.User;

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

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {

	@Value("${spring.jwt.access.expiration}")
	private long accessTokenExpire;

	@Value("${spring.jwt.refresh.expiration}")
	private long refreshTokenExpire;

	@Value("${spring.jwt.token.secret-key}")
	private String secretKey;

	private Key key;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createAccessToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Date now = new Date();
		Date accessValidity = new Date(now.getTime() + accessTokenExpire);
		Claims claims = Jwts.claims().setSubject(Long.toString(userPrincipal.getId()));
		claims.put("email", userPrincipal.getEmail());

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(accessValidity)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Date now = new Date();
		Date refreshValidity = new Date(now.getTime() + refreshTokenExpire);

		return Jwts.builder()
			.setSubject(Long.toString(userPrincipal.getId()))
			.setExpiration(refreshValidity)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = getClaims(token);
		return Long.parseLong(claims.getSubject());
	}

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

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		Long userId = getUserIdFromToken(token);
		String email = getUserEmailFromToken(token);
		// List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		// User principal = new User(userId, "", authorities);
		UserPrincipal userPrincipal = UserPrincipal.create(userId, email);
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}

	// 나중에 지울것
	public UsernamePasswordAuthenticationToken getAuthenticationByUser(User user) {
		UserPrincipal userPrincipal = UserPrincipal.create(user.getId(), user.getEmail());
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			System.out.println("Signature");
			//            log.error("유효하지 않은 JWT 서명");
		} catch (MalformedJwtException e) {
			System.out.println("Malforned");
			//            log.error("유효하지 않은 JWT");
		} catch (ExpiredJwtException e) {
			System.out.println("expired");
			//            log.error("만료된 JWT");
		} catch (IllegalArgumentException e) {
			System.out.println("illegal");
			//            log.error("잘못된 토큰");
		} catch (UnsupportedJwtException e) {
			System.out.println("unsopprted");
			//            log.error("지원하지 않는 JWT");
		}
		return false;
	}
}
