package com.hamgame.hamgame.domain.gameNotice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.hamgame.hamgame.domain.BaseTimeEntity;
import com.hamgame.hamgame.domain.game.entity.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE game_notice SET is_deleted = true WHERE game_notice_id = ?")
public class GameNotice extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gameNoticeId;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private NoticeType noticeType;

	private String url;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

	private boolean isDeleted = Boolean.FALSE;
}
