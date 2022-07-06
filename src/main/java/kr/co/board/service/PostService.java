package kr.co.board.service;

import kr.co.board.model.Post;
import kr.co.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post save(Post post) throws IOException {
        return postRepository.save(post);
    }

    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}