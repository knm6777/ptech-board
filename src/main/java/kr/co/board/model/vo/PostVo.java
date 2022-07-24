package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
public class PostVo {
    private Long id;

    @NotBlank(message = "제목을 적으세요.")
    private String title;

    @NotBlank(message = "내용을 적으세요.")
    private String content;
    private MultipartFile file;

    private Long[] deleteFileIds;

    public Long[] getDeleteFileIds() {
        if (Objects.isNull(this.deleteFileIds)) {
            return null;
        }
        return this.deleteFileIds;
    }

    public boolean hasFile() {
        return !this.file.isEmpty();
    }
}
