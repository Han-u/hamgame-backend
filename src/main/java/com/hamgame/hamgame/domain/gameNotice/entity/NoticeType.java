package com.hamgame.hamgame.domain.gameNotice.entity;

public enum NoticeType {
	NOTICE("공지"),
	INSPECTION("소식"),
	EVENT("이벤트"),
	SHOP("상점"),
	ETC("기타");
	private final String label;

	NoticeType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
