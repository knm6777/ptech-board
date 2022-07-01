package kr.co.board.repository;

import kr.co.board.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
