package kr.co.board.service;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    public Page<Post> findAllByMember(Member member, Pageable pageable) {
        return postRepository.findAllByMember(member, pageable);
    }

    public Post findById(Long id) {
//        return postRepository.findById(id).orElse(null);
        return postRepository.findByIdJoin(id);
    }

    @Transactional
    public Post findNextPost(Long id){
        return postRepository.findNextPost(id);
    }

    @Transactional
    public Post findPrePost(Long id){
        return postRepository.findPrePost(id);
    }

    public void save(Post post) throws IOException {
        postRepository.save(post);
    }

    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteByMember(Member member) {
        postRepository.deleteAllByMember(member);
    }

    @Transactional
    public void updateHit(Long id) {
        postRepository.updateHit(id);
    }

}