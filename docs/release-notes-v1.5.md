---
layout: page
title: Talently v1.5 — Release Notes
---

## Talently v1.5 — What's New

This release overhauls how rejection history is tracked, completes the notes system with edit and delete support, removes the candidate status lifecycle in favour of a richer rejection log, and delivers a series of robustness and UI improvements.

---

### Redesigned: Rejection Tracking

The old `reject` command has been replaced by three dedicated commands that give you full, editable control over each candidate's rejection history.

#### `addreject` — Record a Rejection Reason

```
addreject INDEX REASON
```

- Appends a new rejection reason to the candidate's history. Earlier entries are never overwritten.
- Up to 20 rejection records per candidate.
- Shows a warning if the same reason is entered consecutively.

Examples:
- `addreject 1 Failed technical round`
- `addreject 3 Salary expectations too high`

<!-- paste addreject screenshot here -->

---

#### `editreject` — Correct a Rejection Reason

```
editreject INDEX REJECT_INDEX REASON
```

- `REJECT_INDEX` is the 1-based position of the entry to edit (visible in the detail panel via `show`).

Example:
- `editreject 2 1 Poor culture fit`

<!-- paste editreject screenshot here -->

---

#### `deletereject` — Remove a Rejection Reason

```
deletereject INDEX REJECT_INDEX
```

Example:
- `deletereject 2 1`

<!-- paste deletereject screenshot here -->

---

### Removed: Candidate Status Lifecycle

The `s/STATUS` field (`active`, `rejected`, `hired`, `blacklisted`) has been removed from `add` and `edit`. Candidate state is now captured entirely through the rejection history — use `addreject` to log why a candidate was turned down, and `undo` to reverse any accidental changes.

**Before (v1.4):**
```
add n/Jane Smith p/91234567 e/jane@example.com a/Clementi Ave 3 s/hired
edit 1 s/active
```

**Now (v1.5):**
```
add n/Jane Smith p/91234567 e/jane@example.com a/Clementi Ave 3
addreject 1 Did not pass final round
```

---

### Redesigned: Notes System

The old `note` command has been renamed to `addnote` with an updated prefix, and is now joined by `editnote` and `deletenote` for full note lifecycle management.

#### `addnote` — Add a Note *(was `note`)*

The command word and content prefix have changed:

| | v1.4 | v1.5 |
|---|---|---|
| Command | `note` | `addnote` |
| Content prefix | `n/` | `c/` |
| Heading prefix | `h/` | `h/` (unchanged) |

```
addnote INDEX c/CONTENT [h/HEADING]
```

- If `h/` is omitted **or** left blank, the heading defaults to `General Note`.
- Each note is automatically timestamped.
- View all notes with `show INDEX`.

Examples:
- `addnote 1 c/Strong system design instincts. h/Tech Round 2`
- `addnote 2 c/Good communication, follow up next quarter.`

<!-- paste addnote screenshot here -->

---

#### `editnote` — Edit an Existing Note

```
editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]
```

- At least one of `c/` or `h/` must be provided.
- The original timestamp is preserved.

Example:
- `editnote 1 2 c/Revised: exceptional system design skills. h/Tech Round 2`

<!-- paste editnote screenshot here -->

---

#### `deletenote` — Delete a Note

```
deletenote INDEX NOTE_INDEX
```

Example:
- `deletenote 1 2`

<!-- paste deletenote screenshot here -->

---

### Enhanced: Tag Pool Listing

Running `tagpool` with no arguments now lists every tag currently in the pool, sorted alphabetically. Previously, bare `tagpool` was not a valid command.

```
tagpool
```

<!-- paste tagpool list screenshot here -->

---

### UI Improvements

- **Minimum window size** — the main window cannot be shrunk below 800 × 600, and the help window below 700 × 500, preventing the interface from becoming unusable on small or tiled displays.
- **Improved Help window** — redesigned for readability with a cleaner layout and updated command summary.

<!-- paste help window screenshot here -->

---

### Bug Fixes

- **Input validation tightened** — name, phone, and email fields now reject edge-case inputs that previously slipped through.
- **Note newline sanitisation** — pasting multi-line text into a note field no longer breaks the display; newlines are collapsed to a single space.
- **Future-date clamping** — notes with timestamps in the future (e.g. from a hand-edited JSON file) are now clamped to the current date on load instead of causing a load failure.
- **`ModelManager` hashCode** — `ModelManager` previously violated the Java `equals`/`hashCode` contract. Fixed.

---

### Changes from v1.4

| Area | v1.4 | v1.5 |
|---|---|---|
| Rejection tracking | `reject INDEX r/REASON` (single command, sets status) | `addreject` / `editreject` / `deletereject` with full editable history |
| Candidate status | `s/STATUS` on `add` and `edit` (active / rejected / hired / blacklisted) | Removed |
| Notes — add | `note INDEX n/CONTENT [h/HEADING]` | `addnote INDEX c/CONTENT [h/HEADING]` |
| Notes — edit/delete | Not available | `editnote`, `deletenote` |
| Tag pool listing | `tagpool` required arguments | Bare `tagpool` lists the pool |
| Window sizing | No minimum enforced | 800 × 600 main / 700 × 500 help |

---

**Requirements:** Java 17 or above.
```
java -jar talently.jar
```
