# AGENTS.md

## Project
- Bookstore REST API. Spring Boot 4.1, Java 25, Spring Data JDBC, H2.
- Use `./mvnw`, never `mvn`.

## Package by feature
- Each feature is one package under `dev.danvega.bookstore`, like `catalog` and `orders`.
- A feature package holds its own controller, service, repository and records.
- No `controller`, `service`, `repository` or `model` packages.
- A feature's repository is package-private. Other features call its public service (`Catalog`, `Orders`).

## Done means
- `./mvnw verify` passes.
