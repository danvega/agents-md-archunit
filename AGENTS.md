# AGENTS.md

## Project
- Bookstore REST API. Spring Boot 4.1, Java 25, Spring Data JDBC, H2.
- Use `./mvnw`, never `mvn`.

## Package by feature
- Each feature is one package under `dev.danvega.bookstore`, like `catalog` and `orders`.
- A feature package holds its own controller, service, repository and records.
- No `controller`, `service`, `repository` or `model` packages.
- A feature's repository is package-private. Other features call its public service (`Catalog`, `Orders`).
- Features never depend on each other in a loop.

## Controllers
- A controller calls its feature's service. It never calls a repository.
- No `@Transactional` on controllers. Transactions belong in the service.

## Security
- Deny by default. The last rule in the `SecurityFilterChain` is `anyRequest().authenticated()`.
- Never leave everything open and lock down a few routes like `/admin`.
- A public route goes in `SecurityConfig` and in `src/test/resources/public-routes.txt`. A person reviews that file.

## Code
- Use constructor injection. No `@Autowired` fields.
- Log with SLF4J. No `System.out` and no `java.util.logging`.
- Throw a specific exception, not `Exception` or `RuntimeException`.
- Don't use deprecated APIs.
- Use Jackson 3 (`tools.jackson`). No `com.fasterxml.jackson.core` or `databind` imports.

## Done means
- `./mvnw verify` passes.
