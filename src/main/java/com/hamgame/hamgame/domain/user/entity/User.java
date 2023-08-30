package com.hamgame.hamgame.domain.user.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(nullable = false)
	private String email;

	@JsonIgnore
	private String password;

	private String nickname;

	private String bio;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	private boolean is_deleted;

	@ManyToMany(
		fetch = FetchType.LAZY,
		cascade = {
			CascadeType.MERGE,
			CascadeType.PERSIST
		}
	)
	@JoinTable(name = "user_games",
		joinColumns = {@JoinColumn(name = "user_id")},
		inverseJoinColumns = {@JoinColumn(name = "game_id")})
	private Set<Game> games = new HashSet<>();

	public void updateInfo(String nickname, String bio, String imageUrl) {
		this.nickname = nickname;
		this.bio = bio;
		this.imageUrl = imageUrl;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void addGames(List<Game> newGames) {
		this.games.addAll(newGames);
	}

	public void removeGame(Long id) {
		this.games.stream()
			.filter(g -> Objects.equals(g.getGameId(), id))
			.findFirst()
			.ifPresent(game -> games.remove(game));
	}

	public void updateGames(List<Game> updateGames) {
		games.clear();
		games.addAll(updateGames);
	}

}
