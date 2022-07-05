package kr.co.board.model.dto;

import kr.co.board.model.Member;
import lombok.Getter;

@Getter
public class MemberDto {
    private Long id;

    private String email;

    private String username;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
    }
}