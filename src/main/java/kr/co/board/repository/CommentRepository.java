package kr.co.board.repository;

import kr.co.board.model.Comment;
import kr.co.board.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}