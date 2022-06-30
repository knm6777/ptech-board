package kr.co.board.model;

import kr.co.board.model.enums.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    @Builder
    public Role(Author author, Member member) {
        this.author = author;
        this.member = member;
    }
}
