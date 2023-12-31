package com.hamgame.hamgame.domain.board.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.hamgame.hamgame.domain.BaseTimeEntity;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.user.entity.User;

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
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE board_id = ?")
public class Board extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	private String image;

	@Column(columnDefinition = "int(11) default 0")
	private int viewCount;

	@Enumerated(EnumType.STRING)
	private BoardCategory boardCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id")
	@NotNull
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@OneToMany(mappedBy = "board")
	private List<Comment> comment;

	@Column(columnDefinition = "tinyint(1) default 0")
	private boolean isDeleted;

	public void updateBoard(String title, String content, String image, BoardCategory boardCategory) {
		this.title = title;
		this.content = content;
		this.image = image;
		this.boardCategory = boardCategory;
	}
}
