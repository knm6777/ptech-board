package kr.co.board.specification;

import kr.co.board.model.Post;
import kr.co.board.model.enums.SearchType;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification {
    public static Specification<Post> likeSearchWord(SearchType searchType, String searchWord) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (searchType == SearchType.WHOLE) {
                list.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("content")), "%" + searchWord.toUpperCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + searchWord.toUpperCase() + "%")
                ));
            } else if (searchType == SearchType.CONTENT) {
                list.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("content")), "%" + searchWord.toUpperCase() + "%"));
            } else if (searchType == SearchType.SUBJECT) {
                list.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + searchWord.toUpperCase() + "%"));
            }

            Predicate[] predicates = new Predicate[list.size()];
            return criteriaBuilder.or(list.toArray(predicates));
        };
    }

}
