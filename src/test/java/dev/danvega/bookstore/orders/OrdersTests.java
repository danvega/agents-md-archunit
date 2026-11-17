package dev.danvega.bookstore.orders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class OrdersTests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void placesAnOrderForABook() {
        assertThat(mvc.post().uri("/api/orders")
                .with(httpBasic("reader", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"bookId": 1, "quantity": 2}
                        """))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson().extractingPath("$.total").isEqualTo(99.98);
    }

    @Test
    void orderForUnknownBookIs404() {
        assertThat(mvc.post().uri("/api/orders")
                .with(httpBasic("reader", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"bookId": 999, "quantity": 1}
                        """))
                .hasStatus(HttpStatus.NOT_FOUND);
    }
}
