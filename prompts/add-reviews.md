Add book reviews to the bookstore.

- POST /api/books/{bookId}/reviews adds a review with a rating (1 to 5) and a comment.
- GET /api/books/{bookId}/reviews lists the reviews for a book.
- Both return 404 if the book does not exist.
- A bad rating or an empty comment is a 400.

Add tests for the new endpoints.
