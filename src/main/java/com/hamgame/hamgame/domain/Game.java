package com.hamgame.hamgame.domain;

import com.hamgame.hamgame.domain.time.BaseTimeEntity;
import com.hamgame.hamgame.domain.type.GameCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE game SET is_deleted = true WHERE game_id = ?")
public class Game extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private GameCategory category;

    private String imageUrl;

    private String homepageUrl;

    private boolean isDeleted = Boolean.FALSE;
}
