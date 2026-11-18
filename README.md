# AGENTS.md Is the Ask. ArchUnit Is the Check.

A small Spring Boot bookstore that shows one idea. A rule in AGENTS.md is a request. A test that fails the build is proof the rule was followed.

This is the `video-start` branch. It's where the video begins.

## Where this branch starts

[AGENTS.md](AGENTS.md) asks for package by feature. Nothing checks it yet.

Reviews were added the layered way. The code sits in `controller`, `service`, `repository` and `model` packages. `BookRepository` in `catalog` was made `public` so the review code could use it.

The build still passes:

```bash
./mvnw verify
```

Every test is green, and the rule is broken. That's the problem this video fixes.

## What you add

Bring in the ArchUnit test from `video-end`. ArchUnit is already in [pom.xml](pom.xml).

```bash
git checkout video-end -- src/test/java/dev/danvega/bookstore/PackageByFeatureTest.java
```

The test has two rules. Each one checks a line in AGENTS.md.

| AGENTS.md asks | ArchUnit rule |
| --- | --- |
| No `controller`, `service`, `repository` or `model` packages | `no_layer_packages` |
| A feature's repository is package-private. Other features call its public service | `repositories_stay_in_their_feature` |

Run `./mvnw verify` again. The code hasn't changed. Now the build fails and names each class that broke the rule.

To see the fix, switch to `video-end`:

```bash
git switch video-end
```

## A check is not a gate until something runs it

The agent can skip the test or ignore a failure. [.claude/settings.json](.claude/settings.json) adds a Claude Code Stop hook so it can't. When the agent tries to finish a turn, [verify-gate.sh](.claude/hooks/verify-gate.sh) runs:

1. If nothing changed, the agent may stop.
2. It runs `./mvnw verify`. If anything fails, the agent is blocked and gets the failing rules as feedback.
3. After one blocked retry, the agent may stop, so a broken build can't loop forever.

The hook only covers Claude Code. A required CI check is the gate for everything else.

## Branches

| Branch | What it is | `./mvnw verify` |
| --- | --- | --- |
| `video-start` | Reviews done the layered way, the Stop hook, no ArchUnit test yet. | passes, rule broken |
| `video-end` | Reviews as their own `reviews` feature that calls `Catalog`. The ArchUnit test and the Stop hook. | passes |
| `start` | `catalog` and `orders` features, AGENTS.md and the gate. No reviews yet. | passes |
| `demo-red` | `start` plus the layered reviews. | fails on the gate |
| `main` | The finished app, with more ArchUnit rules and a Spring Security gate. | passes |

The layered reviews were written by hand, so the starting point is the same on every take. To see what your own agent does, check out `start` and give it [prompts/add-reviews.md](prompts/add-reviews.md).

## Run it

You need Java 25 or later.

```bash
./mvnw spring-boot:run
```

[bookstore.http](bookstore.http) has sample requests.
