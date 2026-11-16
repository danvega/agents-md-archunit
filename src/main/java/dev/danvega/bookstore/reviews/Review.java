package dev.danvega.bookstore.reviews;

import org.springframework.data.annotation.Id;

public record Review(@Id Long id, Long bookId, int rating, String comment) {
}
