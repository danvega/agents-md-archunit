package dev.danvega.bookstore.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogTests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void listsBooks() {
        assertThat(mvc.get().uri("/api/books"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.length()").isEqualTo(3);
    }

    @Test
    void unknownBookIs404() {
        assertThat(mvc.get().uri("/api/books/999")).hasStatus(HttpStatus.NOT_FOUND);
    }
}
