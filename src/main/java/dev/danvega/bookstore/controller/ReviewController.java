package dev.danvega.bookstore.controller;

import dev.danvega.bookstore.model.Review;
import dev.danvega.bookstore.service.ReviewService;
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
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@PathVariable Long bookId, @Valid @RequestBody ReviewRequest request) {
        return reviewService.addReview(bookId, request.rating(), request.comment())
                .map(review -> ResponseEntity.created(URI.create("/api/books/" + bookId + "/reviews/" + review.id())).body(review))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long bookId) {
        return ResponseEntity.of(reviewService.getReviews(bookId));
    }

    public record ReviewRequest(@Min(1) @Max(5) int rating, @NotBlank String comment) {
    }
}
