package dev.danvega.bookstore.orders;

import org.springframework.data.repository.ListCrudRepository;

interface OrderRepository extends ListCrudRepository<BookOrder, Long> {
}
