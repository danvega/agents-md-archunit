package dev.danvega.bookstore.reviews;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

interface ReviewRepository extends ListCrudRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);
}
