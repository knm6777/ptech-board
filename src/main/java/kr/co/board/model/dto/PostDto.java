package kr.co.board.model.dto;

import kr.co.board.model.Post;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostDto {
    private Long id;

    private String title;

    private String content;

    private MemberDto member;

    private String createdAt;

    private String updatedAt;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.member = new MemberDto(post.getMember());
        this.createdAt = String.valueOf(post.getCreatedAt());
        this.updatedAt = String.valueOf(post.getUpdatedAt());
    }
}
