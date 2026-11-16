package dev.danvega.bookstore.model;

import org.springframework.data.annotation.Id;

public record Review(@Id Long id, Long bookId, int rating, String comment) {
}
