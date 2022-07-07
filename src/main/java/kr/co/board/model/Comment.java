package kr.co.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.board.model.vo.CommentVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    private Instant createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    public Comment(CommentVo vo) {
        LocalDateTime localDateTime = LocalDateTime.now();

        this.content = vo.getContent();
        this.createdAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public void assignMember(Member member) {
        this.member = member;
    }

    public void assignPost(Post post) {
        this.post = post;
    }
}