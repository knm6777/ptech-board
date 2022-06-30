package kr.co.board.model;

import kr.co.board.model.vo.PostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post")
//    private File file;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
//    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

//    public void update(PostVo vo) {
//        this.title = vo.getTitle();
//        this.content = vo.getContent();
//    }

//    public boolean isSameMember(Member member) {
//        return this.member.getId().equals(member.getId());
//    }
}