package kr.co.board.service;

import kr.co.board.model.Comment;
import kr.co.board.model.Member;
import kr.co.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Comment deleteById(Long id) {
        Comment deletedComment = this.findById(id);
        commentRepository.delete(deletedComment);
        return deletedComment;
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public List<Comment> findAllByMember(Member member) {
        return commentRepository.findAllByMember(member);
    }
}
