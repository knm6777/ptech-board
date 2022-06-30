package kr.co.board.service;

import kr.co.board.model.Post;
import kr.co.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    //private final FileService fileService;

    @Transactional
    public Post save(Post post) throws IOException {
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    @Transactional
    public Post deleteById(Long id) {
        Post post = this.findById(id);
//        if (!Objects.isNull(post.getFile())) {
//            fileService.deleteFileById(post.getFile().getId());
//        }
        postRepository.delete(post);
        return post;
    }
//
//    public Page<Post> findAll(Pageable pageable) {
//        return postRepository.findAllWithMemberAndFile(pageable);
//    }
}