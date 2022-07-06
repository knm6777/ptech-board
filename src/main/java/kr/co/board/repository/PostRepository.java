package kr.co.board.repository;

import kr.co.board.model.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findById(Long id);

    List<Post> findAll();
}
