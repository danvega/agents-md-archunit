# AGENTS.md Is the Ask. ArchUnit Is the Check.

A small Spring Boot bookstore that shows one idea. A rule in AGENTS.md is a request. A test that fails the build is proof the rule was followed.

The rule here is "package by feature". [AGENTS.md](AGENTS.md) asks for it. [PackageByFeatureTest](src/test/java/dev/danvega/bookstore/PackageByFeatureTest.java) checks it with ArchUnit on every `./mvnw verify`.

## The rule and its check

| AGENTS.md asks | ArchUnit checks |
| --- | --- |
| No `controller`, `service`, `repository` or `model` packages | `no_layer_packages` |
| A feature's repository is package-private. Other features call its public service | `repositories_stay_in_their_feature` |
| Features never depend on each other in a loop | `features_have_no_cycles` |

## More rules on `main`

`main` adds two more test classes. [SpringRulesTest](src/test/java/dev/danvega/bookstore/SpringRulesTest.java) keeps controllers thin. [CodingRulesTest](src/test/java/dev/danvega/bookstore/CodingRulesTest.java) uses rules that ship with ArchUnit in `GeneralCodingRules`, plus one Jackson rule.

| AGENTS.md asks | ArchUnit checks |
| --- | --- |
| A controller never calls a repository | `controllers_do_not_use_repositories` |
| No `@Transactional` on controllers | `controllers_are_not_transactional`, `controller_methods_are_not_transactional` |
| Constructor injection only | `no_field_injection` |
| No `System.out` or `java.util.logging` | `no_system_out`, `no_java_util_logging` |
| Throw a specific exception | `no_generic_exceptions` |
| No deprecated APIs | `no_deprecated_api` |
| Jackson 3 only | `jackson_3_only` |

## Security: a gate ArchUnit can't be

AGENTS.md asks for deny by default. ArchUnit can't check that. It sees structure, like packages, dependencies and annotations. A `SecurityFilterChain` is behavior, so [SecurityGateTest](src/test/java/dev/danvega/bookstore/SecurityGateTest.java) checks the running app instead.

| AGENTS.md asks | The test checks |
| --- | --- |
| Deny by default | `a_route_that_does_not_exist_still_needs_login`. With "permit all" an unknown path gets 404. With deny by default it gets 401. |
| Only listed routes are public | `every_route_not_on_the_public_list_needs_login`. It asks Spring MVC for every route and calls each one without a login. |

The public routes live in [public-routes.txt](src/test/resources/public-routes.txt). A new endpoint fails the build until someone locks it down or adds it to that file on purpose.

Reading books and reviews is public. Placing an order or writing a review needs the demo login `reader` / `password`.

To see the gate fail, swap the rules in [SecurityConfig](src/main/java/dev/danvega/bookstore/security/SecurityConfig.java) for the anti-pattern and run `./mvnw verify`:

```java
.authorizeHttpRequests(auth -> auth
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll())
```

The feature tests still pass. Both security checks fail, and the sweep names `POST /api/orders` and `POST /api/books/{bookId}/reviews` as open.

## Branches

| Branch | What it is | `./mvnw verify` |
| --- | --- | --- |
| `start` | `catalog` and `orders` features, AGENTS.md and the gate. No reviews yet. | passes |
| `demo-red` | Reviews added the way an agent often does it: layer packages and a public `BookRepository`. The feature tests pass. | fails on the gate |
| `main` | Reviews added as a `reviews` feature that talks to the catalog through `Catalog`. | passes |

`demo-red` is staged by hand, so the red build is the same on every take. To run it for real, check out `start` and give your agent [prompts/add-reviews.md](prompts/add-reviews.md).

## Run it

You need Java 25 or later.

```bash
./mvnw verify
```

```bash
./mvnw spring-boot:run
```

[bookstore.http](bookstore.http) has sample requests.

## See the gate fail

```bash
git switch demo-red
./mvnw verify
```

The feature tests pass. `PackageByFeatureTest` fails and names each class that broke the rule.
