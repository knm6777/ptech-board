package kr.co.board.repository;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {


    Optional<Post> findById(Long id);

    @Query(value ="SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments c \n" +
            "LEFT JOIN FETCH p.file f \n" +
            "WHERE  p.id = :id\n")
    Post findByIdJoin(Long id);

//    @Query(value ="SELECT * FROM posts p " +
//            "LEFT OUTER JOIN p.comments c \n" +
//            "LEFT JOIN p.file f \n", nativeQuery = true)
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByMember(Member member, Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE id < :id ORDER BY id DESC limit 1", nativeQuery = true)
    Post findPrePost(Long id);

    @Query(value = "SELECT * FROM posts WHERE id > :id ORDER BY id limit 1", nativeQuery = true)
    Post findNextPost(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.hit = p.hit + 1 WHERE p.id = :id")
    void updateHit(Long id);

    void deleteAllByMember(Member member);
}
