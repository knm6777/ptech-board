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
import java.util.Objects;

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

    @OneToMany(
            mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private final List<File> files = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

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

//    public void deleteFile() {
//        this.file = null;
//    }

    public void deleteFileById(Long id) {
        for(int i=0; i<this.files.size(); i++) {
            if(Objects.equals(this.files.get(i).getId(), id)){
                this.files.remove(i);
                return;
            }
        }
    }

    public boolean isWriter(Member member) {
        return this.member.getId().equals(member.getId());
    }
}