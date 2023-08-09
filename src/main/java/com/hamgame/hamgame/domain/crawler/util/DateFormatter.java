package com.hamgame.hamgame.domain.crawler.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DateFormatter {

	public static DateTimeFormatter getTimeFormatter(String timeFormat) {
		return new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern(timeFormat)
			.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			.toFormatter();
	}

	public static DateTimeFormatter getDateFormatter(String dateFormat) {
		return new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern(dateFormat)
			.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
			.parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalDate.now().getMonthValue())
			.parseDefaulting(ChronoField.DAY_OF_MONTH, LocalDate.now().getDayOfMonth())
			.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			.toFormatter();
	}

	/**
	 * hh:mm 등의 정해진 시간 포맷에 맞는지 확인합니다.
	 */
	public static boolean isValidTimeFormat(String time, String timeFormat) {
		try {
			DateTimeFormatter timeFormatter = getTimeFormatter(timeFormat);
			LocalTime.parse(time, timeFormatter);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * yyyy-MM-dd hh:mm:ss 등의 정해진 날짜 포맷에 맞는지 확인합니다.
	 */
	public static boolean isValidDateFormat(String datetime, String dateFormat) {
		try {
			DateTimeFormatter dateFormatter = getDateFormatter(dateFormat);
			LocalDateTime.parse(datetime, dateFormatter);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * GMT 시간을 KST 기준으로 변환합니다.
	 */
	public static LocalDateTime gmtToKst(LocalDateTime gmtTime) {
		return gmtTime.plusHours(9);
	}

	/**
	 * 일정 기간 사이의 날짜인지 확인합니다.
	 */
	public static boolean isBetweenDay(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
		// return (target.isAfter(start) || target.isEqual(start)) && (target.isBefore(end) || target.isEqual(end));
		return (target.isAfter(start) || target.isEqual(start)) && target.isBefore(end);
	}

	/**
	 * hh:mm 등의 시간을 LocalDateTime으로 바꿉니다.
	 */
	public static LocalDateTime getTime(String time, String timeFormat) {
		try {
			DateTimeFormatter timeFormatter = getTimeFormatter(timeFormat);
			LocalDate today = LocalDate.now();
			return LocalTime.parse(time, timeFormatter).atDate(today);
		} catch (DateTimeParseException e) {
			throw new DateTimeException("why???");
		}
	}

	/**
	 * yyyy-MM-dd hh:mm:ss 등의 날짜를 LocalDateTime으로 바꿉니다.
	 */
	public static LocalDateTime getDate(String date, String dateFormat) {
		try {
			DateTimeFormatter dateFormatter = getDateFormatter(dateFormat);
			return LocalDateTime.parse(date, dateFormatter);
		} catch (DateTimeParseException e) {
			throw new DateTimeException("");
		}
	}

	/**
	 * dd일 전의 형식을 LocalDateTime으로 바꿉니다.
	 */
	public static LocalDateTime getAgoDate(String date, String dateFormat) {
		LocalDateTime ago = getDate(date, dateFormat);
		return LocalDateTime.now().minusDays(ago.getDayOfMonth());
	}

	/**
	 * hh시간 전의 형식을 LocalDateTime으로 바꾸니다.
	 */
	public static LocalDateTime getAgoTime(String time, String timeFormat) {
		LocalDateTime ago = getTime(time, timeFormat);
		return LocalDateTime.now().minusHours(ago.getHour());
	}

}
