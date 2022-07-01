package kr.co.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name="members")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "member")
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Member(String email, String username, String password) {
        LocalDate localDate = LocalDate.now();

        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.of(localDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
    }
}
