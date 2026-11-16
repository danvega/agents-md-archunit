package dev.danvega.bookstore.catalog;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The front door to the catalog feature. Other features use this, never the repository.
 */
@Service
public class Catalog {

    private final BookRepository books;

    Catalog(BookRepository books) {
        this.books = books;
    }

    public List<Book> findAll() {
        return books.findAll();
    }

    public Optional<Book> findById(Long id) {
        return books.findById(id);
    }
}
