package dev.danvega.bookstore.catalog;

import org.springframework.data.repository.ListCrudRepository;

interface BookRepository extends ListCrudRepository<Book, Long> {
}
