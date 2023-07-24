package com.hamgame.hamgame.domain;

import com.hamgame.hamgame.domain.time.BaseTimeEntity;
import com.hamgame.hamgame.domain.type.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

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

    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment;

    private boolean isDeleted = Boolean.FALSE;
}
