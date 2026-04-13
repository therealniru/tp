---
layout: page
title: User Guide
---

Talently is a **desktop contact-management application** for recruiters and hiring managers who manage large volumes of job candidates. It uses a **Command Line Interface (CLI)** for speed, backed by a visual GUI for quick scanning. If you type quickly, Talently lets you manage candidates efficiently with just a few keystrokes.

**Who is this for?**

| Attribute | Details |
|---|---|
| **Role** | Recruiters and hiring managers at startups or small teams |
| **Technical level** | Comfortable typing commands in a terminal (no programming needed) |
| **Context** | Switching between terminal and candidate list, often under time pressure |
| **Goals** | Add, track, and search candidates quickly; maintain a clean audit trail |
| **Challenges addressed** | Forgetting command syntax, losing candidate history, slow mouse-based tools |

### Expectations

* Users are expected to be familiar with basic command syntax similar to search bars or messaging apps, and need tagging capabilities to organise candidates by hiring stage, role, or project without complex navigation.
* The application is optimised for structured, ASCII-based text entry — it is not intended as a rich-text editor or a multilingual CRM.

### Assumptions about user skills

* **Basic command-line familiarity:** opening a terminal, navigating folders (e.g. `cd`), and running the application with `java -jar [CS2103T-T17-4][Talently].jar`. Able to copy/paste commands correctly.
* **Understanding of simple data concepts used by the app:** 1-based indexes, tags, and candidate fields (name, phone, email, address, priority).
* **Comfortable reading short on-screen prompts** and the result-display feedback after each command.
* **Basic English literacy** sufficient to interpret command keywords and UI labels.
* **Basic awareness of data privacy and backup practices:** avoid storing sensitive credentials in notes, and back up the `data/talently.json` file before manual edits.

### Environment assumptions

* **Display:** Talently is designed for a **single-monitor desktop setup**. It has been tested at the default launch size up to a typical 1920×1080 display. Stretching the window across multiple monitors, onto ultra-wide displays, or to extreme aspect ratios is **not supported** and may cause the candidate list, detail panel, or help window to render with awkward spacing. If this happens, resize the window back to a normal single-monitor size and the layout will recover.
* **Window positioning:** If the saved window position is off-screen (e.g., because your display settings changed between launches), Talently automatically repositions the window to the primary screen. No manual intervention is needed.
* **Minimum window size:** Talently enforces a minimum main-window size of 800×600 and a minimum help-window size of 700×500. You cannot shrink either window below these dimensions — this guarantees that the command box, result display, candidate list, and command summary remain visible at all times.
* **Operating system:** Tested on Windows, macOS, and Linux with Java 17+.
* **Character input:** Commands and all text fields accept **printable ASCII characters only** (letters, digits, spaces, and punctuation listed per field below). Non-ASCII characters — including accented letters (`é`, `ñ`), CJK characters (中, 日本語), emojis, curly/smart quotes, and right-to-left scripts — are rejected by the field validators or by the find keyword parser. If you paste text and receive a validation error, re-type the value using plain ASCII.

--------------------------------------------------------------------------------------------------------------------

## Table of Contents

* [Quick start](#quick-start)
* [Features](#features)
  * [Field constraints quick reference](#field-constraints-quick-reference)
  * [Viewing help : `help`](#viewing-help--help)
  * [Adding a candidate : `add`](#adding-a-candidate--add)
  * [Listing all candidates : `list`](#listing-all-candidates--list)
  * [Editing a candidate : `edit`](#editing-a-candidate--edit)
  * [Showing candidate details : `show`](#showing-candidate-details--show)
  * [Locating candidates : `find`](#locating-candidates--find)
  * [Filtering candidates by tag : `filter`](#filtering-candidates-by-tag--filter)
  * [Removing a candidate : `remove`](#removing-a-candidate--remove)
  * [Adding a rejection reason : `addreject`](#adding-a-rejection-reason--addreject)
  * [Editing a rejection reason : `editreject`](#editing-a-rejection-reason--editreject)
  * [Deleting a rejection reason : `deletereject`](#deleting-a-rejection-reason--deletereject)
  * [Sorting candidates by date : `sort date`](#sorting-candidates-by-date--sort-date)
  * [Sorting candidates by priority : `sort pr`](#sorting-candidates-by-priority--sort-pr)
  * [Adding a note to a candidate : `addnote`](#adding-a-note-to-a-candidate--addnote)
  * [Editing a note : `editnote`](#editing-a-note--editnote)
  * [Deleting a note : `deletenote`](#deleting-a-note--deletenote)
  * [Managing the tag pool : `tagpool`](#managing-the-tag-pool--tagpool)
  * [Tagging a candidate : `tag`](#tagging-a-candidate--tag)
  * [Undoing the last modifying command : `undo`](#undoing-the-last-modifying-command--undo)
  * [Redoing the last undone command : `redo`](#redoing-the-last-undone-command--redo)
  * [Clearing all entries : `clear`](#clearing-all-entries--clear)
  * [Exiting the program : `exit`](#exiting-the-program--exit)
  * [Saving the data](#saving-the-data)
  * [Editing the data file](#editing-the-data-file)
* [FAQ](#faq)
* [Known issues](#known-issues)
* [Command summary](#command-summary)
* [Glossary](#glossary)

--------------------------------------------------------------------------------------------------------------------

## Quick start

**Step 1 — Install Java**
Ensure you have Java `17` or above installed.

```
java -version
```
Expected: output showing `17` or higher.

<div markdown="span" class="alert alert-info">
:information_source: **Mac users:** Install the exact JDK version from the [SE-Education Java Installation guide for Mac](https://se-education.org/guides/tutorials/javaInstallationMac.html).
</div>

**Step 2 — Download Talently**
Get the latest `[CS2103T-T17-4][Talently].jar` from the [Talently GitHub Releases page](https://github.com/AY2526S2-CS2103T-T17-4/tp/releases).

**Step 3 — Set up your home folder**
Copy `[CS2103T-T17-4][Talently].jar` into an **empty folder**. Talently stores its data files here — using an empty folder avoids accidental conflicts with existing files.

**Step 4 — Launch**

```
java -jar [CS2103T-T17-4][Talently].jar
```

Expected: Talently opens within a few seconds with sample candidate data pre-loaded (Note: The detail panel on the right is not shown when the app is launched).

![Ui.png](images/Ui.png)

**Step 5 — Overview of the interface**

| Area | Location | Purpose |
|---|---|---|
| Command Box | Top | Type commands here, press Enter to run |
| Result Display | Below command box | Feedback after every command |
| Candidate List | Left panel | Cards showing index, name, tags, contact info, rejection badge |
| Detail Panel | Right panel | Full candidate details (opened with `show`) |

**Step 6 — Try a realistic workflow**

Follow these commands in order to experience a typical recruiter workflow:

| Step | Command | What it does |
|---|---|---|
| 1 | `list` | View all candidates |
| 2 | `add n/Jane Smith p/91234567 e/jane@example.com a/10 Havelock Road` | Add a new candidate |
| 3 | `show 1` | Open the detail panel for the first candidate |
| 4 | `addnote 1 c/Strong portfolio, schedule follow-up. h/Initial Screen` | Attach a note |
| 5 | `tagpool at/Shortlisted` | Create a new tag in the tag pool |
| 6 | `tag 1 at/Shortlisted` | Assign the tag to the candidate |
| 7 | `filter Shortlisted` | View only candidates with that tag |
| 8 | `find Jane` | Search by name |
| 9 | `undo` | Undo the last change |

Ready for more? Continue to [Features](#features).

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Command format rules:**

* Words in `UPPER_CASE` are placeholders for values you supply. e.g. `add n/NAME` means you type `add n/John Doe`.
* `[square brackets]` = optional. e.g. `[pr/PRIORITY]` can be omitted.
* `…` after an item = repeatable. e.g. `[at/TAG]…` can be `at/Java at/Python`.
* `INDEX` must be a positive integer, e.g. `1, 2, 3`.
* Commands that take no parameters (such as `help`, `list`, `exit`, `clear`, `undo`, and `redo`) will reject any extra non-space text with an error. For example, `list abc` is not valid. However, trailing spaces are accepted — `list   ` (with spaces) is treated the same as `list`.
* For commands where **all** parameters are shown in square brackets (e.g. `edit`, `editnote`, `tag`), at least one bracketed parameter must still be provided — providing zero will result in an error.
* **Command words are case-insensitive** (e.g. `ADD`, `Add`, and `add` are all valid).
* **Prefixes are case-insensitive.** For example, `n/`, `N/`, `p/`, `P/`, `e/`, `E/`, `at/`, `AT/` are all recognised. Lowercase is recommended for consistency.
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

<div markdown="span" class="alert alert-warning">
:warning: **Caution: Manual Data Editing** <br>
The data file `[JAR file location]/data/talently.json` is in JSON format. While you can edit it manually, it is **not recommended**.
*   **Corrupted Data:** If the file is edited in a way that violates data constraints (e.g., an invalid email format, duplicate entries sharing the same phone or email, or a candidate holding a tag that does not exist in the master tag pool), the application will detect the corruption and **reset to an empty state** to prevent further data loss or crashes. **Note:** A single mistyped tag on one candidate is sufficient to trigger a full data reset — always verify tag names exactly match what is in the `"tags"` pool array.
*   **Healing:** In cases of missing optional metadata (like `dateAdded`), the application may "heal" the record by assigning a default value. For example, a missing `dateAdded` is silently replaced with the current timestamp, whereas an invalid field value causes a full reset. Future-dated timestamps are clamped to the current time on load, and the corrected data is saved immediately even without any further user command. Notes and rejections exceeding the per-candidate limits (50 and 20 respectively) are truncated to the limit on load.
*   **Backup:** Always keep a backup of your data file before performing manual edits.
</div>

* **Input normalization:** Names have extra whitespace collapsed (e.g., <code>John&nbsp;&nbsp;&nbsp;Doe</code> becomes `John Doe`). Emails are automatically lowercased (e.g., `John@Gmail.COM` becomes `john@gmail.com`). Addresses have leading and trailing whitespace stripped (e.g., `  123 Main St  ` is accepted as `123 Main St`). Phone numbers are compared by their digits only — the `+` prefix, spaces, hyphens, and parentheses are stripped before comparing, so `+65-9123-4567`, `+6591234567`, and `6591234567` are all treated as the same number for duplicate detection.
* **Providing duplicate prefixes** (e.g., `n/Alice n/Bob`) in a single command is not allowed and will be rejected with an error.
* **Display resets:** After any command that adds, edits, removes, or otherwise changes candidate data (`add`, `edit`, `remove`, `tag`, `addnote`, `editnote`, `deletenote`, `addreject`, `editreject`, `deletereject`), the candidate list returns to the default alphabetical order and any active filter or search is cleared.

</div>

---

### Field constraints quick reference

| Field | Prefix | Rules |
|---|---|---|
| NAME | `n/` | Letters (a-z, A-Z), digits (0-9), spaces, hyphens `-`, apostrophe `'`, periods `.`, slashes `/`, commas `,`, `@` symbols, backticks (`` ` ``), and parentheses `()`. Must start with a letter. 1–100 characters. Strips leading/trailing whitespace and normalizes internal spaces. **Do not include the sequences ` n/`, ` p/`, ` e/`, ` a/`, or ` pr/` (space + prefix) inside the name value** — the parser will treat them as new field prefixes, splitting your input incorrectly. |
| PHONE | `p/` | Optional `+` prefix, then digits with optional spaces, hyphens `-`, or parentheses `()` as separators. Must start with a `+` or digit, and must end with a digit. Must contain 3–15 digits (separators excluded). Strips leading/trailing whitespace. |
| EMAIL | `e/` | `local@domain` format. Max 254 characters. Automatically lowercased. The local part may contain letters, digits, and `+ _ . -`; it must **start and end with a letter or digit** (not a special character), and **consecutive special characters are not allowed** (e.g. `test..email@example.com` is invalid). The domain must have at least one `.` and a TLD of at least two letters. Strips leading/trailing whitespace. |
| ADDRESS | `a/` | Any non-empty printable ASCII text (no accented letters, emojis, or non-ASCII input). Max 200 characters. Strips leading/trailing whitespace. **Do not include the sequences ` n/`, ` p/`, ` e/`, ` a/`, or ` pr/` (space + prefix) inside the address value** — the parser will treat them as new field prefixes. |
| TAG | `at/` / `dt/` | Must start with a letter or number, followed by letters, numbers, or `. + - _ ( ) @ # ! ? '`. No spaces. 1–30 characters. Case-insensitive. Strips leading/trailing whitespace. |
| PRIORITY | `pr/` | Case-insensitive `yes` or `no`. Defaults to `no` if omitted during creation. Strips leading/trailing whitespace. |
| REJECTION REASON | *(none — positional)* | Letters, numbers, spaces, and the symbols `. , - ' / : ; ! ? ( ) & " # + % @ *`. Must not be blank. Max 200 characters. Strips leading/trailing whitespace. The reason is entered directly after the index (and rejection index for `editreject`) with no prefix — e.g. `addreject 1 Failed technical interview`. |
| NOTE | `c/`, `h/` | Heading (`h/`) is optional (defaults to `General Note`). Content (`c/`) is required. Printable ASCII only. Converts newlines to spaces and strips leading/trailing whitespace. **Do not include the sequences ` c/` or ` h/` (space + prefix) inside note content or headings** — the parser will treat them as new field delimiters, splitting your input incorrectly. |

All text fields accept **printable ASCII characters only** — non-ASCII input (accented letters, emojis, CJK characters) is rejected. See [Environment assumptions](#environment-assumptions) for details.

<div markdown="span" class="alert alert-info">
:information_source: **Data Verification:** Talently does **not** verify whether emails, phone numbers, or names exist in the real world. This application acts purely as a local record management system. Duplicate people (same names) can be entered twice if they have different phone numbers and emails.
</div>

---

### Viewing help : `help`

Opens a help window with a quick-reference command summary and a link to this full User Guide. Note that the help window always opens with a default window size, even if you resized it previously.

Format: `help`

Examples:
* `help`

![help.png](images/help.png)

> **Expected output:** A help window opens, displaying a quick-reference guide of allowed command formats and a link to the online User Guide. A confirmation message (`Opened help window.`) also appears in the result display. This applies whether you type `help`, press `F1`, or click the Help menu button — all three methods open the window and show the same confirmation.

---

### Adding a candidate : `add`

Adds a new candidate to Talently.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]`

**Parameters:**

| Parameter | Prefix | Required | Rules                                                                                                                                                                                                                                                                                                                                                                                                                |
|---|---|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| NAME | `n/` | Yes | Letters (a-z, A-Z), digits (0-9), spaces, hyphens `-`, apostrophe `'`, periods `.`, slashes `/`, commas `,`, `@` symbols, backticks (`` ` ``), and parentheses `()`. Must start with a letter. Strips leading/trailing whitespace and normalizes internal spaces. 1–100 characters. **Avoid the sequences ` n/`, ` p/`, ` e/`, ` a/`, ` pr/` (space + prefix) inside the value** — they are treated as new prefixes. |
| PHONE | `p/` | Yes | Optional `+` prefix, then digits with optional spaces, hyphens `-`, or parentheses `()` as separators. Must start with a `+` or digit, and must end with a digit. Must contain 3–15 digits (separators excluded). Strips leading/trailing whitespace.                                                                                                                                                                |
| EMAIL | `e/` | Yes | `local@domain` format. Max 254 characters. Automatically lowercased. The local part may contain letters, digits, and `+ _ . -`; must start and end with a letter or digit; no consecutive special characters (e.g. `a..b@x.com` is invalid). Strips leading/trailing whitespace.                                                                                                                                     |
| ADDRESS | `a/` | Yes | Any non-empty printable ASCII text (no accented letters, emojis, or non-ASCII input). Max 200 characters. Strips leading/trailing whitespace. **Avoid the sequences ` n/`, ` p/`, ` e/`, ` a/`, ` pr/` (space + prefix) inside the value** — they are treated as new prefixes.                                                                                                                                       |
| PRIORITY | `pr/` | No | Case-insensitive `yes` or `no`. Default: `no`. Strips leading/trailing whitespace.                                                                                                                                                                                                                                                                                                                                   |

<div markdown="span" class="alert alert-info">
:information_source: **Tags are not set at add time.** First create tags with `tagpool`, then assign them with `tag`. See [Managing the tag pool](#managing-the-tag-pool--tagpool) and [Tagging a candidate](#tagging-a-candidate--tag).
</div>

<div markdown="span" class="alert alert-info">
:information_source: All candidates start as active when added. Use tags to track hiring stages (e.g., `tagpool at/Hired at/Shortlisted`, then `tag INDEX at/Hired`). Use `addreject` to record formal rejection decisions with reasons.
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `pr/yes` to flag high-priority candidates. Surface them later with `sort pr o/desc`.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Duplicate detection is based on phone number **or** email — not name. If a new candidate shares the same phone **or** email as an existing one, it is rejected as a duplicate. Two candidates with the same name but different phone and email are allowed.
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy O'Brien e/betsy@example.com a/Newgate Prison p/+6591234567 pr/yes`

![add.png](images/add.png)

> **Expected output:** `New candidate added: John Doe | Phone: 98765432 | Email: johnd@example.com | Address: John street, block 123, #01-01`

---

### Listing all candidates : `list`

Displays every candidate, sorted alphabetically by name. Running `list` clears any active sorting or filtering and restores the default alphabetical order.

Format: `list`

* Shows total candidate count.
* If Talently is empty, a prompt appears to add candidates.
* Restores the default alphabetical listing, removing any previous `sort`, `find`, or `filter` results.

Examples:
* `list`


> **Expected output:** `Listed 1 candidate.` (singular) or `Listed X candidates.` (where X is the total number of candidates)

---

### Editing a candidate : `edit`

Updates one or more fields of an existing candidate.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]` *(at least one field required)*

* `INDEX` refers to the number shown in the current list. Must be a positive integer.
* At least one field must be provided.
* Unspecified fields are unchanged.
* All provided fields (including `INDEX`) have leading and trailing whitespace stripped before validation.
* `PRIORITY`: `yes` or `no` (case-insensitive — `YES`, `Yes`, `NO` are all accepted).
* `NAME`: Multiple internal spaces are compressed into one.
* If the new values are identical to the existing ones (including casing), a message indicating no changes were detected is shown and no modification is made. Case-only changes (e.g., `alice` → `Alice`) are treated as real edits, except for emails which are automatically lowercased and thus treated as case-insensitive during this check.
* **NAME / ADDRESS values:** Do not include the sequences ` n/`, ` p/`, ` e/`, ` a/`, or ` pr/` (space + prefix) inside a name or address value — the parser will treat them as the start of a new field, splitting your input incorrectly.
* **EMAIL:** The local part must start and end with a letter or digit; consecutive special characters (e.g. `a..b@x.com`) are not allowed.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Editing phone or email to match another existing candidate will fail — duplicates are not allowed.
</div>

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` — Updates phone and email.
* `edit 2 n/Betsy Crower pr/yes` — Updates name and sets high priority.

![edit.png](images/edit.png)

> **Expected output:** `Edited Candidate: Betsy Crower | Phone: 99272758 | Email: berniceyu@example.com | Address: Blk 30 Lorong 3 Serangoon Gardens, #07-18`

---

### Showing candidate details : `show`

Opens the full detail panel for a candidate on the right side of the screen.

Format: `show INDEX`

* `INDEX` must be a positive integer.
* Detail panel displays: name, phone, email, address, priority, date added, tags, all notes (each showing the heading and content), and full rejection history.
* If you make changes to a candidate (e.g. via `edit`, `addnote`, `tag`) while their detail panel is open, the panel updates automatically to reflect the latest information.
* If the candidate currently shown is removed (via `remove`), the detail panel clears automatically. If you then `undo` the removal, the detail panel repopulates automatically.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show` after `addnote` or `addreject` to verify your changes.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Known limitation:** When the detail panel refreshes (e.g. after adding a note or rejection), it scrolls back to the top. If you have many entries, you will need to scroll down again to see the newly added item.
</div>

Examples:
* `show 1` — Opens details for the 1st candidate.
* `find John` then `show 1` — Opens details for the first match.

> **Expected output:** `Showing candidate: John Doe`

---

### Locating candidates : `find`

Searches across all candidates by keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

**What is searched:**

| Field | Example |
|---|---|
| Name | `find john` matches `John Doe` |
| Phone | `find 9123` matches `91234567` |
| Email | `find gmail` matches `john@gmail.com` |
| Note headings and content | `find interview` matches notes titled `Tech Round 1` containing "interview" |
| Rejection reasons | `find overqualified` matches rejection records |

**Rules:**
* Case-insensitive. Partial matches supported.
* Candidates matching **any** keyword are returned (OR logic).
* Max 20 keywords. The total search input (including spaces between keywords) must not exceed 150 characters. Leading and trailing whitespace is not counted.
* Keywords may contain: letters, digits, `-` `'` `.` `/` `@` `+` `_` `:` `;` `!` `?` `(` `)` `&` `%` `"` `#` `*` `,` (non-ASCII characters such as accented letters or emojis are not supported).
* Duplicate keywords are automatically removed (e.g., `find john john` searches for `john` once).
* `find` always searches across **ALL** candidates in Talently, not just those currently shown by a previous `filter`. Running `find` after `filter` will show results from the full list.
* If no candidates match, `No matching candidates found.` is shown. Run `list` to return to the full candidate list.

<div markdown="span" class="alert alert-info">
:information_source: **Note:** `find` does not search the **address field** or **tags**. Use `filter` to search by tag. Phone number search is normalised — digits-only searches (e.g., `find 6591234567`) will match phone numbers stored with separators (e.g., `+65-9123-4567`).
</div>

Examples:
* `find John` — Matches `john`, `John Doe`.
* `find alex david` — Matches candidates whose name contains `alex` or `david`.
* `find overqualified` — Matches candidates whose rejection reasons include "overqualified".
* `find technical interview` — Matches candidates with notes or rejection reasons mentioning `technical` or `interview`.

> **Expected output:** `1 candidate listed.` (singular), `X candidates listed.` (where X is 2 or more), or `No matching candidates found.` if the search set is empty.

---

### Filtering candidates by tag : `filter`

Returns all candidates who have a specific tag assigned.

Format: `filter TAG`

* Exact match (not partial). `Java` does not match `JavaScript`.
* Case-insensitive. `java` matches `Java`.
* Tag must follow naming rules: must start with a letter or number, followed by letters, numbers, or the symbols `. + - _ ( ) @ # ! ? '`, no spaces, 1–30 characters.
* If no candidates have the specified tag (including if the tag does not exist in the tag pool at all), `No matching candidates found.` is shown. Run `list` to return to the full list.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `filter` to pull all candidates at a specific hiring stage, e.g. `filter Shortlisted`.
</div>

Examples:
* `filter Shortlisted` — Shows all candidates tagged `Shortlisted`.
* `filter Java` — Shows candidates tagged `Java`, not `JavaScript`.

> **Expected output:** `1 candidate listed.` (singular) or `X candidates listed.` (where X is the number of candidates with that tag), or `No matching candidates found.` if none match.

---

### Removing a candidate : `remove`

Permanently deletes a candidate and all their data.

Format: `remove INDEX`

* `INDEX` must be a positive integer referencing the currently displayed list.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** This deletes the candidate along with all their notes, tags, and rejection history. Use `undo` immediately to recover.
</div>

Examples:
* `list` then `remove 2` — Removes the 2nd candidate in the full list.
* `find Betsy` then `remove 1` — Removes the first result from the search.

> **Expected output:** `Removed Person: John Doe | Phone: 98765432 | Email: johnd@example.com | Address: John street, block 123, #01-01`

---

### Adding a rejection reason : `addreject`

Records a rejection reason against a candidate and appends it to their rejection history.

Format: `addreject INDEX REASON`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `REASON`: entered **directly after the index with no prefix** (e.g. `addreject 1 Failed interview` — there is no `rj/` or similar prefix). Non-empty, max 200 characters. Leading and trailing whitespace is automatically stripped.
  * **Allowed characters:** Letters, digits, spaces, and the symbols `. , - ' / : ; ! ? ( ) & " # + % @ *`.
* Each `addreject` call appends to the rejection history — previous entries are not overwritten. Max 20 rejection records per candidate.
* If the same reason is given consecutively, a warning is shown (the rejection is still recorded).

<div markdown="span" class="alert alert-info">
:information_source: **Note:** `addreject` is designed for recording historical rejection decisions and reasons — it is not a status tracker. If you want to track a candidate's current stage (e.g. Shortlisted, Interviewed), use the `tag` command instead.
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use tags to track hiring stages (e.g., `tag INDEX at/Blacklisted`). Use `addreject` to formally record the reason for rejection. Use `show INDEX` after rejecting to view the full rejection history in the detail panel.
</div>

Examples:
* `addreject 1 Failed technical interview`
* `addreject 3 Insufficient experience`

![addreject.png](images/addreject.png)

> **Expected output:** `Rejection reason recorded. New Reason added: Failed technical interview (Total rejections on record: 1)`

---

### Editing a rejection reason : `editreject`

Edits an existing rejection reason for a candidate.

Format: `editreject INDEX REJECT_INDEX NEW_REASON`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `REJECT_INDEX` is the rejection reason's position in the candidate's rejection history (positive integer). Use `show INDEX` to see rejection numbers.
* `NEW_REASON`: entered **directly after the rejection index with no prefix** (e.g. `editreject 1 1 Failed cultural fit` — there is no prefix before the reason). Non-empty, max 200 characters. Leading and trailing whitespace is automatically stripped. Same allowed characters as `addreject`.
* If the new reason is identical to the existing one, a message indicating no changes were detected is shown and no modification is made.
* The candidate must have at least one existing rejection reason.

Examples:
* `editreject 1 1 Failed cultural fit interview` — updates the 1st rejection reason for candidate 1.
* `editreject 2 2 Insufficient experience for senior role` — updates the 2nd rejection reason for candidate 2.

> **Expected output:** `Successfully edited rejection reason for candidate: John Doe`

---

### Deleting a rejection reason : `deletereject`

Deletes a rejection reason from a candidate's record.

Format: `deletereject INDEX REJECT_INDEX`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `REJECT_INDEX` is the rejection reason's position in the candidate's rejection history (positive integer). Use `show INDEX` to see rejection numbers.
* The candidate must have at least one existing rejection reason.

Examples:
* `deletereject 1 2` — deletes the 2nd rejection reason from candidate 1.
* `deletereject 3 1` — deletes the 1st rejection reason from candidate 3.

> **Expected output:** `Successfully deleted rejection reason from candidate: John Doe`

---

### Sorting candidates by date : `sort date`

Sorts all candidates by the date they were added.

Format: `sort date o/ORDER`

* `ORDER`: `asc` (oldest first) or `desc` (newest first). Case-insensitive (`ASC`, `Desc`, etc. are accepted).
* Candidates added at the **exact same date and time** are sorted alphabetically by name.
* After sorting, any active filter is cleared and all candidates are displayed in the new order.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Cannot sort an empty candidate list — an error is shown if Talently has no candidates.
</div>


Examples:
* `sort date o/asc` — Oldest candidates first.
* `sort date o/desc` — Newest candidates first.

> **Expected output:** `Sorted all candidates by date added in ascending order.` or `Sorted all candidates by date added in descending order.`

---

### Sorting candidates by priority : `sort pr`

Sorts all candidates by their priority flag.

Format: `sort pr o/ORDER`

* `ORDER`: `asc` (low-priority first) or `desc` (high-priority first). Case-insensitive (`ASC`, `Desc`, etc. are accepted).
* Secondary sort: by date and time added (newest first), then alphabetically by name.
* After sorting, any active filter is cleared and all candidates are displayed in the new order.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `sort pr o/desc` to immediately surface your most important candidates.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Cannot sort an empty candidate list — an error is shown if Talently has no candidates.
</div>


Examples:
* `sort pr o/asc` — Low-priority candidates first.
* `sort pr o/desc` — High-priority candidates first.

> **Expected output:** `Sorted all candidates by priority status (low-priority first).` or `Sorted all candidates by priority status (high-priority first).`

---

### Adding a note to a candidate : `addnote`

Adds a note to a candidate's record.

Format: `addnote INDEX c/CONTENT [h/HEADING]`

* `INDEX` must be a positive integer.
* `CONTENT` is required, must not be blank, printable ASCII only, and must not exceed 500 characters. Leading and trailing whitespace is automatically stripped.
* `HEADING` is optional — candidates may have structured interview rounds (e.g. `h/Tech Round 1`, `h/HR Interview`) but sometimes you just want to jot down a quick observation without a specific occasion. Defaults to `General Note` if omitted **or** if `h/` is followed by only whitespace (any number of spaces). Leading and trailing whitespace is automatically stripped. Must not exceed 50 characters (printable ASCII only).
* Max 50 notes per candidate. Attempting to add a 51st note shows an error and nothing is saved.
* Notes are appended in order — earlier notes are never overwritten.
* Newline characters (from multiline pastes) are automatically converted to spaces; literal text sequences like `\n` are not converted.
* Note content and headings must not contain the sequences ` c/` or ` h/` (space followed by a prefix), as these are interpreted as command prefixes.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use descriptive headings (e.g. `h/Tech Round 1`, `h/HR Interview`) to organise notes by hiring stage. View all notes with `show INDEX`.
</div>

Examples:
* `addnote 1 c/Passed the technical interview flawlessly. h/Tech Round 1`
* `addnote 2 c/Strong communication skills.`

![addnote.png](images/addnote.png)

> **Expected output:** `Successfully added note to candidate: John Doe`

---

### Editing a note : `editnote`

Edits the content and/or heading of an existing note.

Format: `editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]` *(at least one required)*

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `NOTE_INDEX` is the note's position in the candidate's notes list (positive integer). Use `show INDEX` to see note numbers.
* At least one of `c/CONTENT` or `h/HEADING` must be provided.
* `CONTENT` must not be blank, printable ASCII only, and must not exceed 500 characters. Leading and trailing whitespace is automatically stripped.
* `HEADING` is printable ASCII only and must not exceed 50 characters. Leading and trailing whitespace is automatically stripped. If `h/` is followed by only whitespace (any number of spaces), the heading defaults to `General Note`.
* If the new values are identical to the existing note, a message indicating no changes were detected is shown and no modification is made.
* Newline characters (from multiline pastes) are automatically converted to spaces; literal text sequences like `\n` are not converted.
* Note content and headings must not contain the sequences ` c/` or ` h/` (space followed by a prefix), as these are interpreted as command prefixes.

Examples:
* `editnote 1 1 c/Actually failed the interview.` — updates the content of note 1 for candidate 1, keeping the original heading.
* `editnote 2 3 h/Final Round` — updates only the heading of note 3 for candidate 2.
* `editnote 1 2 c/New content h/New heading` — updates both content and heading.

> **Expected output:** `Successfully edited note for candidate: Robert Lim`

---

### Deleting a note : `deletenote`

Deletes a note from a candidate's record.

Format: `deletenote INDEX NOTE_INDEX`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `NOTE_INDEX` is the note's position in the candidate's notes list (positive integer). Use `show INDEX` to see note numbers.

Examples:
* `deletenote 1 2` — deletes the 2nd note from candidate 1.
* `deletenote 3 1` — deletes the 1st note from candidate 3.

> **Expected output:** `Successfully deleted note from candidate: Robert Lim`

---

### Managing the tag pool : `tagpool`

Creates or deletes tags in the master tag registry.

**Tags must exist in the pool before they can be assigned to candidates.**

Format: `tagpool [at/TAG_TO_CREATE]... [dt/TAG_TO_DELETE]...`

* Running `tagpool` with no arguments lists all tags currently in the pool.
* To create or delete tags, at least one `at/` or `dt/` prefix is required.
* Max 10 tags per command — **adds and deletes counted together** (e.g., 6 adds + 5 deletes = 11 total, which exceeds the limit). The pool can hold at most 50 tags total. A command that would bring the net pool size above 50 is rejected — but a swap (e.g., `tagpool at/NewTag dt/OldTag`) is permitted even when the pool is already at 50, since the net size does not change.
* Tag names: must start with a letter or number, followed by letters, numbers, or the symbols `. + - _ ( ) @ # ! ? '`, 1–30 characters, case-insensitive (`Python` and `python` are the same). Any leading or trailing whitespace is automatically stripped. Internal spaces are not allowed.
* Cannot create a tag that already exists, or delete one that does not exist. If you try to create an existing tag, the error message will tell you it already exists — this is the quickest way to check if a tag is in the pool.
* Cannot create and delete the same tag in one command.
* Duplicate tags within the same add or delete list are not allowed (e.g., `tagpool at/Java at/java` is rejected because tags are case-insensitive).
* Tags assigned to candidates are visible on their cards in the list view. Use `list` to see all candidates and their tags at a glance.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Deleting a tag removes it from **all candidates** currently holding it. Use `undo` immediately to reverse. Undo fully restores the tag to the pool **and** to all candidates who had it.
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Workflow:** `tagpool at/Shortlisted` → `tag 1 at/Shortlisted` → `filter Shortlisted`
</div>

Examples:
* `tagpool` — Lists all tags in the pool (e.g., "Tag pool (3 tags): AI, Backend, Frontend").
* `tagpool at/Shortlisted at/Interviewed` — Creates two new tags.
* `tagpool dt/Shortlisted` — Deletes `Shortlisted` from the pool and all candidates.
* `tagpool at/Senior dt/Junior` — Creates `Senior` and deletes `Junior` in one command.

> **Expected output:**
> * **Mutating (with arguments):** `Tag pool updated. Created: 1 tag(s). Deleted: 0 tag(s).` When tags are deleted, an additional warning is shown: `Warning: Cascade deletion — candidates assigned to the deleted tag(s) have had those tags removed (if any such candidates exist).`
> * **Listing (no arguments):** `Tag pool (3 tags): Shortlisted, Interviewed, Selected` or `Tag pool is empty. Use tagpool at/TAG to create tags.` if the pool is empty.

---

### Tagging a candidate : `tag`

Adds or removes tags on one or more candidates.

Format: `tag INDEX[,INDEX]... [at/TAG_TO_ADD]... [dt/TAG_TO_REMOVE]...` *(at least one `at/` or `dt/` required)*

* Single index or comma-separated list (e.g. `1,2,3`). Duplicate indices not allowed.
* Each `INDEX` must be a positive integer.
* At least one `at/` or `dt/` prefix is required.
* Max 10 tags per command — **adds and deletes counted together**. A candidate can hold at most one of each tag in the pool (no duplicates), bounded by the 50-tag pool limit.
* Tags must already exist in the tag pool — use `tagpool` to create them first.
* Cannot add a tag the candidate already has, or remove one they do not have.
* Cannot add and delete the same tag in one command.

Examples:
* `tag 1 at/Shortlisted` — Adds `Shortlisted` to candidate 1.
* `tag 2 dt/Interviewed` — Removes `Interviewed` from candidate 2.
* `tag 3 at/Senior dt/Junior` — Adds `Senior` and removes `Junior` from candidate 3.
* `tag 1,2,3 at/Shortlisted` — Adds `Shortlisted` to candidates 1, 2, and 3.

> **Expected output:** A confirmation message listing the tags added and/or removed for the specified candidate(s).

---

### Undoing the last modifying command : `undo`

Reverts Talently to the state before the most recent data-changing command.

Format: `undo`

`undo` steps back one action at a time. You can keep typing `undo` to go further back through your history. `redo` steps forward again through actions you have undone. If you make any new change after undoing, you can no longer redo the undone actions.

* Applies to: `add`, `edit`, `remove`, `addreject`, `editreject`, `deletereject`, `tag`, `tagpool`, `addnote`, `editnote`, `deletenote`, `clear`.
* Does **not** apply to read-only commands (`find`, `filter`, `list`, `show`, `help`, `exit`). Typing `undo` after one of these steps back to the last data-changing action, not the last view change.
* If there is nothing to undo, an error is shown.


Examples:
* `remove 2` then `undo` — Restores the removed candidate.
* `undo` then `undo` — Steps back two actions.

> **Expected output:** `Undid the previous command.`

---

### Redoing the last undone command : `redo`

Re-applies the most recently undone data-changing command. This applies to the same set of commands as `undo`: `add`, `edit`, `remove`, `addreject`, `editreject`, `deletereject`, `tag`, `tagpool`, `addnote`, `editnote`, `deletenote`, and `clear`.

Format: `redo`

* Can only be used after `undo`. If there is no undone state, an error is shown.
* Any new modifying command after `undo` clears the redo history — once you make a new change, the previously undone actions can no longer be redone.


Examples:
* `remove 2` → `undo` → `redo` — Re-applies the removal.

> **Expected output:** `Redid the previously undone command.`

---

### Clearing all entries : `clear`

Deletes every candidate and all tags from Talently.

Format: `clear`

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Removes all candidate data **and the entire tag pool**. Use `undo` immediately to recover.
</div>

Examples:
* `clear`

> **Expected output:** `All candidates and tags have been cleared!`

---

### Exiting the program : `exit`

Closes Talently.

Format: `exit` (Alternatively, you can use `Cmd + Q` on macOS or `Ctrl + Q` on Windows/Linux)

Examples:
* `exit`

> **Expected output:** The application window closes and Talently exits.

---

### Saving the data

Talently saves automatically after every command that modifies data. No manual save is needed.

---

### Editing the data file

Data is stored at: `[JAR file location]/data/talently.json`

Advanced users may edit this file directly.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Invalid edits will cause Talently to discard all data and start empty on next launch. Back up the file before editing.
</div>

<div markdown="span" class="alert alert-info">
:information_source: **Note:** If a candidate's `dateAdded` is set to a future date (e.g. via manual editing), Talently will automatically clamp it to the current date and time on next launch.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q: How do I transfer data to another computer?**
A: Copy `data/talently.json` from your current home folder to the same location on the new machine after installing Talently there.

**Q: Can I undo a `clear`?**
A: Yes — run `undo` immediately after `clear` to restore all candidates.

**Q: Why can't I assign a tag to a candidate?**
A: Tags must first exist in the pool. Run `tagpool at/TAG_NAME`, then `tag INDEX at/TAG_NAME`.

**Q: What does the red badge on a candidate card mean?**
A: It shows the total number of times that candidate has been rejected. Run `show INDEX` to view the full rejection history.

**Q: How do I view all notes for a candidate?**
A: Use `show INDEX`. The detail panel lists all notes with headings and content. Note numbers shown correspond to the `NOTE_INDEX` used in `editnote` and `deletenote`.

**Q: Can two candidates have the same name?**
A: Yes — Talently identifies duplicates by phone **or** email, not name. Two candidates with the same name but different phone and email are allowed.



--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]` <br> e.g. `add n/James Ho p/22224444 e/jamesho@example.com a/123 Clementi Rd`
**Clear** | `clear`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]` *(at least one field required)*<br> e.g. `edit 2 n/James Lee e/jameslee@example.com`
**Exit** | `exit` (also `Cmd + Q` on macOS, `Ctrl + Q` on Windows/Linux)
**Filter** | `filter TAG`<br> e.g. `filter Shortlisted`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g. `find James Jake`
**Help** | `help`
**List** | `list`
**Add Note** | `addnote INDEX c/CONTENT [h/HEADING]`<br> e.g. `addnote 1 c/Passed interview h/Tech Round 1`
**Delete Note** | `deletenote INDEX NOTE_INDEX`<br> e.g. `deletenote 1 2`
**Edit Note** | `editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]` *(at least one required)*<br> e.g. `editnote 1 1 c/Updated content`
**Redo** | `redo`
**Add Reject** | `addreject INDEX REASON`<br> e.g. `addreject 1 Failed technical interview`
**Delete Reject** | `deletereject INDEX REJECT_INDEX`<br> e.g. `deletereject 1 2`
**Edit Reject** | `editreject INDEX REJECT_INDEX NEW_REASON`<br> e.g. `editreject 1 1 Failed cultural fit`
**Remove** | `remove INDEX`<br> e.g. `remove 3`
**Show** | `show INDEX`<br> e.g. `show 1`
**Sort (date)** | `sort date o/ORDER`<br> e.g. `sort date o/desc`
**Sort (priority)** | `sort pr o/ORDER`<br> e.g. `sort pr o/desc`
**Tag** | `tag INDEX[,INDEX]... [at/TAG]... [dt/TAG]...`<br> e.g. `tag 1,2 at/Shortlisted dt/Applied`
**Tag Pool** | `tagpool [at/TAG]... [dt/TAG]...`<br> e.g. `tagpool` (list all), `tagpool at/Shortlisted dt/Rejected`
**Undo** | `undo`

--------------------------------------------------------------------------------------------------------------------

## Glossary

| Term | Meaning |
|---|---|
| **Candidate** | A person managed in Talently, with fields name, phone, email, address, priority, date added, tags, notes, and rejection history. |
| **Command** | A specific instruction typed into the command box to perform an action (e.g. `add`, `find`, `tag`). |
| **Parameter** | A value supplied to a command, usually introduced by a prefix such as `n/`, `p/`, `e/`, `a/`, `pr/`, `h/`, `c/`, `o/`. |
| **Prefix** | The short marker (e.g. `n/`) that introduces a parameter in a command. Prefixes are case-insensitive. The real prefixes used in Talently are: `n/`, `p/`, `e/`, `a/`, `pr/`, `at/`, `dt/`, `c/`, `h/`, `o/`. Must be preceded by a space when not at the start of the command. |
| **Index** | The 1-based number shown next to each candidate in the currently displayed list. Used by commands such as `remove`, `edit`, `show`, `tag`, `addnote`, and `addreject`. The index refers to the **displayed** list, which may be filtered. |
| **Tag** | A user-defined label attached to a candidate (e.g. `Shortlisted`). Tags are case-insensitive and must first be created in the tag pool before being assigned. |
| **Tag pool** | The master registry of tags managed with `tagpool`. Only tags in the pool can be assigned to candidates. |
| **Note** | A free-form entry attached to a candidate, with optional heading. Notes are ordered and never overwritten by new additions. |
| **Rejection reason** | A formal, audit-style record of why a candidate was rejected, added with `addreject`. Each candidate maintains a numbered history; the red badge on a candidate card shows the total count. |
| **Priority** | A boolean flag (`yes` / `no`) that marks a candidate as high-priority. Surfaced by `sort pr o/desc`. |
| **Detail panel** | The right-side panel opened by `show INDEX`. Displays all candidate information including notes and full rejection history. |
| **Command box** | The text input at the top of the main window where you type commands. |
| **Result display** | The area directly below the command box that shows feedback and error messages after each command. |
| **Candidate list** | The scrollable list on the left of the main window showing candidate cards with index, name, contact fields, tags, and rejection badge. |
| **Home folder** | The folder containing `[CS2103T-T17-4][Talently].jar`. Talently reads and writes `data/talently.json` and `preferences.json` relative to this folder. |
| **Save file** | `data/talently.json` — the JSON file where candidate data is autosaved after every modifying command. |
| **Autosave** | The automatic write to the save file after any command that changes data. No manual save is needed. |
| **Duplicate candidate** | Any new or edited candidate whose phone number **or** email matches an existing candidate. Talently rejects duplicates. Name alone does not determine uniqueness. |
| **ASCII input** | Plain, printable characters in the US-ASCII range (letters, digits, spaces, common punctuation). Talently only accepts ASCII in text fields — non-ASCII characters are rejected. |
