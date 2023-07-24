package com.hamgame.hamgame.domain;

import com.hamgame.hamgame.domain.time.BaseTimeEntity;
import com.hamgame.hamgame.domain.type.NoticeType;
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
