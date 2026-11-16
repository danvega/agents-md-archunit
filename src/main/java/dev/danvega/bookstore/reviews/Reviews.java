package dev.danvega.bookstore.reviews;

import dev.danvega.bookstore.catalog.Catalog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The front door to the reviews feature. It checks books through Catalog, not BookRepository.
 */
@Service
public class Reviews {

    private final ReviewRepository reviews;
    private final Catalog catalog;

    Reviews(ReviewRepository reviews, Catalog catalog) {
        this.reviews = reviews;
        this.catalog = catalog;
    }

    public Optional<Review> add(Long bookId, int rating, String comment) {
        return catalog.findById(bookId)
                .map(book -> reviews.save(new Review(null, book.id(), rating, comment)));
    }

    public Optional<List<Review>> forBook(Long bookId) {
        return catalog.findById(bookId)
                .map(book -> reviews.findByBookId(book.id()));
    }
}
