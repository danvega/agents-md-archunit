package dev.danvega.bookstore.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private final Orders orders;

    OrderController(Orders orders) {
        this.orders = orders;
    }

    @PostMapping
    ResponseEntity<BookOrder> place(@Valid @RequestBody PlaceOrder request) {
        return orders.place(request.bookId(), request.quantity())
                .map(order -> ResponseEntity.created(URI.create("/api/orders/" + order.id())).body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    record PlaceOrder(@NotNull Long bookId, @Positive int quantity) {
    }
}
