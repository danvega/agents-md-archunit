package dev.danvega.bookstore.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
class BookController {

    private final Catalog catalog;

    BookController(Catalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    List<Book> findAll() {
        return catalog.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> findById(@PathVariable Long id) {
        return ResponseEntity.of(catalog.findById(id));
    }
}
