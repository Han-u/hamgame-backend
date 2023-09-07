package com.hamgame.hamgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	/*
	 * Swagger
	 * API 문서를 쉽게 작성 가능하고 Swagger를 통해 테스트를 진행할 수 있다
	 * OAS - 소스코드 없이 이해할 수 있도록 시각화하기 위한 인터페이스
	 */

	/*
	 * Docket: Spring API에 대한 기본 인터페이스 설정 위한 빌더
	 * OAS_30: swagger 3.0버전
	 * useDefaultResponseMessages: Swagger에 미리 정의된 기본 응답 메시지 표시 여부
	 * apiInfo: Swagger 문서 정보
	 * apis: Swagger 문서화 대상 API의 패키지 경로 지정
	 * path: apis에 지정된 경로중 원하는 경로만 문서화
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
			.useDefaultResponseMessages(true)
			.apiInfo(apiInfo())
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build();
	}

	/*
	 * 상단에 표시될 Swagger 문서 정보
	 * title, version, description, contact, license
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Hamgame Swagger")
			.version("0.1")
			.build();
	}
}
