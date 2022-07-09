package kr.co.board.repository;

import kr.co.board.model.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findById(Long id);

    List<Post> findAll();

    @Modifying
    @Query("UPDATE Post p SET p.hit = p.hit + 1 WHERE p.id = :id")
    void updateHit(Long id);

    @Query(value = "SELECT * FROM posts WHERE id < :id ORDER BY id DESC limit 1", nativeQuery = true)
    Post findPrePost(Long id);

    @Query(value = "SELECT * FROM posts WHERE id > :id ORDER BY id limit 1", nativeQuery = true)
    Post findNextPost(Long id);

}
