package dev.danvega.bookstore.service;

import dev.danvega.bookstore.catalog.BookRepository;
import dev.danvega.bookstore.model.Review;
import dev.danvega.bookstore.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public Optional<Review> addReview(Long bookId, int rating, String comment) {
        if (!bookRepository.existsById(bookId)) {
            return Optional.empty();
        }
        return Optional.of(reviewRepository.save(new Review(null, bookId, rating, comment)));
    }

    public Optional<List<Review>> getReviews(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return Optional.empty();
        }
        return Optional.of(reviewRepository.findByBookId(bookId));
    }
}
