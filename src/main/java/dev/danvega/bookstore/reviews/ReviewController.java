package dev.danvega.bookstore.reviews;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
class ReviewController {

    private final Reviews reviews;

    ReviewController(Reviews reviews) {
        this.reviews = reviews;
    }

    @PostMapping
    ResponseEntity<Review> add(@PathVariable Long bookId, @Valid @RequestBody AddReview request) {
        return reviews.add(bookId, request.rating(), request.comment())
                .map(review -> ResponseEntity.created(URI.create("/api/books/" + bookId + "/reviews/" + review.id())).body(review))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    ResponseEntity<List<Review>> forBook(@PathVariable Long bookId) {
        return ResponseEntity.of(reviews.forBook(bookId));
    }

    record AddReview(@Min(1) @Max(5) int rating, @NotBlank String comment) {
    }
}
