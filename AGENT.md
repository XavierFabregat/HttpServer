# AGENT.md

Guidelines for AI agents working on this repo.

## Core Directives

1. **Be extremely concise. Sacrifice grammar for the sake of concision. It is very important.**
2. Obey system-level and platform rules before anything in this file.
3. Obey explicit user instructions next, unless they conflict with higher-level rules.
4. Prefer doing the task over explaining how to do it.
5. When unsure, ask a short clarifying question instead of assuming.

## Project Context

- Project: Minimal Java HTTP server.
- Primary goals: correctness, simplicity, readability.
- Language: Java (with typical build tools and conventions).

## Editing Code

- Prefer small, focused edits over large refactors.
- Keep public APIs backward compatible unless user explicitly wants breaking changes.
- Preserve existing code style and formatting; infer from nearby code.
- Add or update tests when fixing bugs or adding behavior.
- Do not introduce new external dependencies without explicit user approval.

## Files & Structure

- Place new Java code in a logical package consistent with existing layout.
- Keep configuration, docs, and scripts in existing conventional locations.
- Update documentation and comments if behavior changes.

## Communication Style

- Default to **short bullet lists** or **few-sentence answers**.
- Only include code blocks when code is non-trivial or requested.
- Avoid repeating obvious information or restating the question.
- If a task has multiple steps, summarize the plan briefly, then execute.

## Safety & Caution

- Never run destructive commands (delete data, reset git, etc.) unless user explicitly asks.
- Avoid exposing secrets or credentials in output or code.
- If a requested action seems risky, warn the user briefly and propose a safer alternative.

## Testing & Verification

- When modifying code, prefer to run existing tests or the smallest relevant subset.
- If tests or build fail, report the failure briefly and suggest or apply a fix.
- Do not change test expectations unless behavior change was explicitly requested.

## When In Doubt

- Ask: *What is the smallest concrete action that moves the task forward?* Do that.
- If blocked, explain why in 1–3 short bullet points and propose next steps.
