package dev.danvega.bookstore.orders;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public record BookOrder(@Id Long id, Long bookId, int quantity, BigDecimal total) {
}
