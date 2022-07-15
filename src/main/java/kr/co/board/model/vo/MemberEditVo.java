package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberEditVo {

    //이미지도 추가할 것
    private Long id;

    private String password;

    private String selfIntroduction;
}

