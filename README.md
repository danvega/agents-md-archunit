# AGENTS.md Is the Ask. ArchUnit Is the Check.

A small Spring Boot bookstore that shows one idea. A rule in AGENTS.md is a request. A test that fails the build is proof the rule was followed.

The rule here is "package by feature". [AGENTS.md](AGENTS.md) asks for it. [PackageByFeatureTest](src/test/java/dev/danvega/bookstore/PackageByFeatureTest.java) checks it with ArchUnit on every `./mvnw verify`.

## The rule and its check

| AGENTS.md asks | ArchUnit checks |
| --- | --- |
| No `controller`, `service`, `repository` or `model` packages | `no_layer_packages` |
| A feature's repository is package-private. Other features call its public service | `repositories_stay_in_their_feature` |

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
