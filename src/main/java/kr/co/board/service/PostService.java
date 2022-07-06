package kr.co.board.service;

import kr.co.board.model.Post;
import kr.co.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post post) throws IOException {
        return postRepository.save(post);
    }

    public Page<Post> getPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage;
    }

    public Post findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    @Transactional
    public Post deleteById(Long id) {
        Post post = this.findById(id);
        postRepository.delete(post);
        return post;
    }
}