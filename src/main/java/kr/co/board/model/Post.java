package kr.co.board.model;

import kr.co.board.model.vo.PostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column
    private Instant updatedAt;

    private int hit;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private File file;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        LocalDateTime localDateTime = LocalDateTime.now();

        this.title = title;
        this.content = content;
        this.member = member;
        this.hit = 0;
        this.createdAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public void update(PostVo vo) {
        LocalDateTime localDateTime = LocalDateTime.now();

        this.title = vo.getTitle();
        this.content = vo.getContent();
        this.updatedAt = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public void deleteFile() {
        this.file = null;
    }

    public boolean isWriter(Member member) {
        return this.member.getId().equals(member.getId());
    }
}