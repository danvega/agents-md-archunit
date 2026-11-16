package dev.danvega.bookstore.orders;

import dev.danvega.bookstore.catalog.Catalog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The front door to the orders feature. It talks to the catalog through Catalog.
 */
@Service
public class Orders {

    private final OrderRepository orders;
    private final Catalog catalog;

    Orders(OrderRepository orders, Catalog catalog) {
        this.orders = orders;
        this.catalog = catalog;
    }

    public Optional<BookOrder> place(Long bookId, int quantity) {
        return catalog.findById(bookId)
                .map(book -> book.price().multiply(BigDecimal.valueOf(quantity)))
                .map(total -> orders.save(new BookOrder(null, bookId, quantity, total)));
    }
}
