package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentVo {

    private Long id;

    private String content;

    private Long postId;
}