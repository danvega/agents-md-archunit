#!/bin/bash
# Stop hook. Claude Code runs this when the agent tries to finish a turn.
# Exit 0 lets it finish. Exit 2 blocks it and sends stderr back to the agent.
input=$(cat)

# After one blocked retry, let the agent stop so a broken build can't loop forever.
if [ "$(printf '%s' "$input" | jq -r '.stop_hook_active // false')" = "true" ]; then
  exit 0
fi

cd "${CLAUDE_PROJECT_DIR:-.}" || exit 0

# Nothing changed, so there is nothing new to check.
[ -z "$(git status --porcelain)" ] && exit 0

# The agent doesn't decide whether the build runs. This does.
if ! output=$(./mvnw -q verify 2>&1); then
  echo "./mvnw verify failed. Fix it before you finish." >&2
  printf '%s\n' "$output" | grep -E 'Tests run:|was violated|^Class <|COMPILATION ERROR|\.java:\[' | head -40 >&2
  exit 2
fi

exit 0
