package kr.co.board.model;

import kr.co.board.model.vo.PostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.*;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        LocalDateTime localDateTime = LocalDateTime.now();

        this.title = title;
        this.content = content;
        this.member = member;
        this.createdAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        this.updatedAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

    }

    public void update(PostVo vo) {
        LocalDateTime localDateTime = LocalDateTime.now();

        this.title = vo.getTitle();
        this.content = vo.getContent();
        this.updatedAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public boolean isSameMember(Member member) {
        return this.member.getId().equals(member.getId());
    }
}