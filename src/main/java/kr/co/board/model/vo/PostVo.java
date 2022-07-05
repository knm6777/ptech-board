package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PostVo {
    private Long id;

    private String title;

    private String content;

}
