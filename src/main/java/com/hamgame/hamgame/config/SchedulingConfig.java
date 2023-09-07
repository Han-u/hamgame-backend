package com.hamgame.hamgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
	/*
	 * 서버 실행시 스케줄링 자동 실행 설정한다.
	 * - @Scheduled: 메소드에 선언해 스케줄 지정
	 *
	 * 실행주기 설정
	 * 1. cron: (* * * * * *) 초 분 시간 일 월 요일순으로 지정한다
	 * 2. fixedDelay: 이전 수행 종류 후 n초 뒤에 호출된다
	 * 3. fixedRate: 이전 수행 시작 후 n초 뒤에 호출된다.
	 *
	 * Thread pool
	 * - 스케줄 작업은 한 개의 스레드풀에서 실행되어 한 스케줄이 끝나야 다음 스케줄이 실행된다.
	 * - 스케줄 스레드 풀 생성해 다중 작업 실행 설정 가능하다.
	 */
}
