package dev.danvega.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AGENTS.md asks for deny by default. ArchUnit can't check that, because it is behavior, not structure.
 * This test checks the running app instead.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityGateTest {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    RequestMappingHandlerMapping mappings;

    // With "permit all" the request gets past security and the answer is 404.
    // With deny by default, security stops it first and the answer is 401.
    @Test
    void a_route_that_does_not_exist_still_needs_login() {
        assertThat(mvc.get().uri("/no-such-route")).hasStatus(HttpStatus.UNAUTHORIZED);
    }

    // Checks every route the app has, including ones added after this test was written.
    @Test
    void every_route_not_on_the_public_list_needs_login() throws IOException {
        Set<String> publicRoutes = publicRoutes();
        List<String> open = new ArrayList<>();

        for (RequestMappingInfo info : mappings.getHandlerMethods().keySet()) {
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            for (RequestMethod method : methods.isEmpty() ? Set.of(RequestMethod.GET) : methods) {
                for (String pattern : info.getPatternValues()) {
                    String route = method + " " + pattern;
                    if (publicRoutes.contains(route)) {
                        continue;
                    }
                    String path = pattern.replaceAll("\\{[^}]+}", "1");
                    int status = mvc.method(HttpMethod.valueOf(method.name())).uri(path)
                            .exchange().getResponse().getStatus();
                    if (status != HttpStatus.UNAUTHORIZED.value()) {
                        open.add(route + " answered " + status + " without a login");
                    }
                }
            }
        }

        assertThat(open)
                .as("Routes that are open but not in public-routes.txt")
                .isEmpty();
    }

    private Set<String> publicRoutes() throws IOException {
        String text = new ClassPathResource("public-routes.txt").getContentAsString(StandardCharsets.UTF_8);
        return text.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .collect(Collectors.toSet());
    }
}
