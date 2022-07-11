package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentVo {

    @Size(max=500, message = "댓글은 1-500자로 제한됩니다.")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    private Long postId;
}