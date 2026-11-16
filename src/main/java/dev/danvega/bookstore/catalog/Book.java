package dev.danvega.bookstore.catalog;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public record Book(@Id Long id, String title, String author, BigDecimal price) {
}
