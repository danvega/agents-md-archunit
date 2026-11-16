package dev.danvega.bookstore.repository;

import dev.danvega.bookstore.model.Review;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ReviewRepository extends ListCrudRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);
}
