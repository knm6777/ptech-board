package kr.co.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="members")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "member_id")
    private Integer id;

    @Column(nullable=false) // null이 되면 안됨.
    private String password;

    @Column(nullable=false) // null이 되면 안됨.
    private String username;

    @Column(nullable=false, unique=true) //unique=true 해야함.
    private String email;

    @OneToMany(mappedBy = "member")
    private Set<Role> roles = new HashSet<>();


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

/*
    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime modifiedAt;


     */
    @Builder
    public Member(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
