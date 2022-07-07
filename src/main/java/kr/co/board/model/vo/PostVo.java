package kr.co.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PostVo {
    private Long id;

    private String title;

    private String content;
    private MultipartFile file;

    private Long[] deleteFileIds;

    public List<Long> getDeleteFileIds() {
        if (Objects.isNull(this.deleteFileIds)) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.deleteFileIds);
    }

    public boolean hasFile() {
        return !this.file.isEmpty();
    }
}
