#!/bin/bash
# Stop hook. Claude Code runs this when the agent tries to finish a turn in this repo.
# Exit 0 lets it finish. Exit 2 blocks it and sends what we print to stderr back to the agent.
input=$(cat)

# After one forced retry, let the agent stop so a broken build can't loop forever.
if [ "$(printf '%s' "$input" | jq -r '.stop_hook_active // false')" = "true" ]; then
  exit 0
fi

cd "${CLAUDE_PROJECT_DIR:-.}" || exit 0

# Nothing changed, so there is nothing new to check.
[ -z "$(git status --porcelain)" ] && exit 0

# Only a Spring Security project needs this gate.
grep -q '<artifactId>spring-boot-starter-security</artifactId>' pom.xml 2>/dev/null || exit 0

# The gate has to exist before the build can prove anything.
if [ -z "$(find src/test -name SecurityGateTest.java 2>/dev/null)" ] || [ ! -f src/test/resources/public-routes.txt ]; then
  echo "The security gate is missing. AGENTS.md says deny by default. SecurityGateTest and src/test/resources/public-routes.txt check it. Put them back before you finish." >&2
  exit 2
fi

# The build runs the gate, the ArchUnit rules and the feature tests.
if ! output=$(./mvnw -q verify 2>&1); then
  echo "./mvnw verify failed. Fix it before you finish." >&2
  printf '%s\n' "$output" | grep -E 'Tests run:|FAILURE|violated|Expecting|expected:|without a login|COMPILATION ERROR|\.java:\[' | head -40 >&2
  exit 2
fi

exit 0
