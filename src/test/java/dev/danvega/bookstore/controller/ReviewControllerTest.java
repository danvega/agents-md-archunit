package dev.danvega.bookstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void addsAndListsReviewsForABook() {
        assertThat(mvc.post().uri("/api/books/2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"rating": 5, "comment": "Still the best Java book."}
                        """))
                .hasStatus(HttpStatus.CREATED);

        assertThat(mvc.get().uri("/api/books/2/reviews"))
                .hasStatusOk()
                .bodyJson().extractingPath("$[0].rating").isEqualTo(5);
    }

    @Test
    void reviewForUnknownBookIs404() {
        assertThat(mvc.post().uri("/api/books/999/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"rating": 4, "comment": "Good"}
                        """))
                .hasStatus(HttpStatus.NOT_FOUND);
        assertThat(mvc.get().uri("/api/books/999/reviews")).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void badRatingOrEmptyCommentIs400() {
        assertThat(mvc.post().uri("/api/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"rating": 6, "comment": "Too good"}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(mvc.post().uri("/api/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"rating": 3, "comment": " "}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
