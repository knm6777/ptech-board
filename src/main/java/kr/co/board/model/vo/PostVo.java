package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
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

    @NotEmpty(message = "제목을 적으세요.")
    private String title;

    @NotEmpty(message = "내용을 적으세요.")
    private String content;
    private MultipartFile file;

    private Long deleteFileId;

    public Long getDeleteFileId() {
        if (Objects.isNull(this.deleteFileId)) {
            return null;
        }
        return this.deleteFileId;
    }

    public boolean hasFile() {
        return !this.file.isEmpty();
    }
}
