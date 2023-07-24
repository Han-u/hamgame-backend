package com.hamgame.hamgame.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hamgame.hamgame.domain.time.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String nickname;

    private String bio;

    private String image;
}
