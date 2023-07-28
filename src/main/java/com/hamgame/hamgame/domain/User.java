package com.hamgame.hamgame.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hamgame.hamgame.domain.time.BaseTimeEntity;
import com.hamgame.hamgame.domain.type.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
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

	public void updateName(String name) {
		this.name = name;
	}

	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
