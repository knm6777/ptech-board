package kr.co.board.model;

import kr.co.board.model.vo.PostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.*;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        LocalDate localDate = LocalDate.now();

        this.title = title;
        this.content = content;
        this.member = member;
        this.createdAt = LocalDateTime.of(localDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
    }
}