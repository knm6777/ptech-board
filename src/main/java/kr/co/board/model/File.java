package kr.co.board.model;

import kr.co.board.model.enums.SaveType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String originalName;

    private BigInteger size;

    private String extension;

    private String relativePath;

    private Integer sequence;

    private String fileType;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private SaveType saveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public File(String name, String originalName, BigInteger size, String extension,
                String relativePath, Integer sequence, String fileType, Post post) {
        this.name = name;
        this.originalName = originalName;
        this.size = size;
        this.extension = extension;
        this.relativePath = relativePath;
        this.sequence = sequence;
        this.fileType = fileType;
        this.post = post;
    }

    public void assignPost(Post post) {
        this.post = post;
    }

    public void updateSaveType(SaveType saveType) {
        this.saveType = saveType;
    }
}