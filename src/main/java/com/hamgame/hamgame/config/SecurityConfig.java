package com.hamgame.hamgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hamgame.hamgame.security.cookie.CookieAuthorizationRequestRepository;
import com.hamgame.hamgame.security.handler.OAuth2AuthenticationFailureHandler;
import com.hamgame.hamgame.security.handler.OAuth2AuthenticationSuccessHandler;
import com.hamgame.hamgame.security.jwt.JwtAccessDeniedHandler;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationEntryPoint;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;
import com.hamgame.hamgame.security.oauth.OAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2UserService oAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/v3/api-docs", "/configuration/ui",
			"/swagger-resources", "/configuration/security",
			"/swagger-ui.html", "/webjars/**", "/swagger/**", "/swagger-ui/**");
	}

	@Bean
	AuthenticationManager authenticationManager(
		AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// cors 설정
			.cors()
			.and()

			// token 사용하는 방식으로 csrf disable
			.csrf().disable()

			.formLogin().disable()
			.httpBasic().disable()

			// 세션 사용하지 않아 STATELESS로 설정
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			// api 경로
			.authorizeRequests() // HttpServletRequest 사용한는 요청들에 대한 접근 제한 설정
			.antMatchers("/login/**").permitAll()
			.antMatchers("/auth/**").permitAll()
			.antMatchers("/oauth2/**").permitAll()
			.antMatchers("/swagger-resources/**").permitAll()
			.anyRequest().authenticated() // 나머지 요청들은 인증없이 접근 X
			.and()

			// 인증 인가 실패 핸들링
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패
			.accessDeniedHandler(jwtAccessDeniedHandler) // 인가 실패
			.and()

			//소셜 로그인 설정
			.oauth2Login()
			.authorizationEndpoint().baseUri("/oauth2/authorize") // 소셜 로그인 url
			.authorizationRequestRepository(cookieAuthorizationRequestRepository) // 인증 요청을 cookie에 저장
			.and()
			.redirectionEndpoint().baseUri("/oauth2/callback/*") // 소셜 인증 후 redirect url
			.and()
			.userInfoEndpoint() // oauth2 로그인하면 최종 응답을 회원 정보로 바로 받음 --> 코드 X 액세스 토큰X
			.userService(oAuth2UserService) // 유저 정보 불러오는 userService 설정
			.and()
			.successHandler(oAuth2AuthenticationSuccessHandler) // 인증 성공시 호출되는 handler
			.failureHandler(oAuth2AuthenticationFailureHandler) // 인증 실패시 호출되는 handler
			.and()

			// jwt 필터 설정
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
