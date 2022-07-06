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
    @Query("update Post p set p.hit = p.hit + 1 where p.id = :id")
    int updateHit(Long id);
}
