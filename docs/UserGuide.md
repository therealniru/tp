---
layout: page
title: User Guide
---

Talently is a **desktop app for recruiters and hiring managers** who keep track of many job candidates. Instead of clicking through menus, you type short commands to add, find, and organise candidates — everything shows up in a clear, easy-to-read window.

**Who is this for?** Recruiters and hiring managers who want a simple, organised way to keep track of candidates without juggling spreadsheets or slow, cluttered tools.

**What can Talently do for you?**

* Keep all your candidates in one place — names, phone numbers, emails, and addresses
* Mark candidates as high priority so the important ones stand out
* Attach notes after interviews so you never forget the details
* Record rejection reasons for a clean hiring history
* Organise candidates into your own categories (tags) — like "Shortlisted", "Interviewed", or "Java"
* Quickly find any candidate by name, note, or rejection reason — or filter by tag
* Undo mistakes instantly

**What you need to know:** You should be comfortable using a computer and reading short instructions. No programming experience needed.

--------------------------------------------------------------------------------------------------------------------

## Table of contents

* [Quick start](#quick-start)
* [New to typing commands? Start here](#new-to-typing-commands-start-here)
* [Features](#features)
  * **The basics**
    * [Viewing help: `help`](#viewing-help--help)
    * [Adding a candidate: `add`](#adding-a-candidate--add)
    * [Listing all candidates: `list`](#listing-all-candidates--list)
    * [Editing a candidate: `edit`](#editing-a-candidate--edit)
    * [Showing candidate details: `show`](#showing-candidate-details--show)
    * [Removing a candidate: `remove`](#removing-a-candidate--remove)
  * **Storing more information about a candidate**
    * [Adding a note: `addnote`](#adding-a-note-to-a-candidate--addnote)
    * [Editing a note: `editnote`](#editing-a-note--editnote)
    * [Deleting a note: `deletenote`](#deleting-a-note--deletenote)
    * [Recording a rejection: `addreject`](#adding-a-rejection-reason--addreject)
    * [Editing a rejection: `editreject`](#editing-a-rejection-reason--editreject)
    * [Deleting a rejection: `deletereject`](#deleting-a-rejection-reason--deletereject)
    * [Managing your tag categories: `tagpool`](#managing-your-tag-categories--tagpool)
    * [Tagging a candidate: `tag`](#tagging-a-candidate--tag)
  * **Finding and organising candidates**
    * [Finding candidates: `find`](#finding-candidates--find)
    * [Filtering candidates by tag: `filter`](#filtering-candidates-by-tag--filter)
    * [Sorting by date: `sort date`](#sorting-candidates-by-date--sort-date)
    * [Sorting by priority: `sort pr`](#sorting-candidates-by-priority--sort-pr)
  * **Undo, redo, and clean-up**
    * [Undo: `undo`](#undoing-the-last-modifying-command--undo)
    * [Redo: `redo`](#redoing-the-last-undone-command--redo)
    * [Clearing all entries: `clear`](#clearing-all-entries--clear)
    * [Exiting the program: `exit`](#exiting-the-program--exit)
* [Saving the data](#saving-the-data)
* [FAQ](#faq)
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
Get the latest `talently.jar` from the [Talently GitHub Releases page](https://github.com/AY2526S2-CS2103T-T17-4/tp/releases).

**Step 3 — Set up your home folder**
Copy `talently.jar` into an **empty folder**. Talently stores its data files here — using an empty folder avoids accidental conflicts with existing files.

**Step 4 — Launch**

```
java -jar talently.jar
```

Expected: Talently opens within a few seconds with sample candidate data pre-loaded.

![Ui 2101.png](images/Ui%202101.png)

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

[↑ Back to top](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## New to typing commands? Start here

If you have never used a command-based app before, don't worry — Talently is designed to be simple once you know three small things.

**1. You type, you press Enter.**
The box at the top of the window is where you type. Press **Enter** to run whatever you typed. The result appears just below the box.

**2. Commands start with a short word, then details.**
Every command begins with a short action word like `add`, `find`, or `remove`. After that, you provide the details. For example:

```
add n/Jane Smith p/91234567 e/jane@example.com a/10 Havelock Road
```

Here, `add` is the action, and everything after it describes the candidate you want to add. The little markers like `n/`, `p/`, `e/`, `a/` tell Talently which piece is which (`n/` for name, `p/` for phone, and so on).

**3. If you make a mistake, just `undo`.**
Type `undo` and press Enter to reverse your last action. You can also type `help` any time to open a help window.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip for new users:** Before jumping into the features section, take a moment to glance at the **field rules** at the start of Features. They explain what a valid name, phone number, email, and so on looks like — following them will save you from most errors.
</div>

[↑ Back to top](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: How to read the command examples:**

* Words in `UPPER_CASE` are placeholders — you replace them with your own values. For example, `add n/NAME` means you type something like `add n/John Doe`.
* Anything in `[square brackets]` is optional and can be skipped.
* `…` after an item means you can repeat it. For example, `[at/TAG]…` lets you list several tags in a row.
* `INDEX` is the number next to a candidate in the list (1, 2, 3, and so on).
* Commands that don't need extra details — like `help`, `list`, `exit`, `clear`, `undo`, and `redo` — should be typed on their own. Adding extra text will cause an error.
* **Action words** (like `add`, `edit`) can be typed in any case — `Add`, `ADD`, and `add` all work.
* **Markers** (like `n/`, `at/`, `c/`) must always be lowercase.
* If you copy-paste a command from a PDF, double-check that no spaces went missing around line breaks.

</div>

**:information_source: Field rules — please skim this before you start:**

These are the rules Talently uses to check what you type. Following them prevents most errors.

| Field | Marker | What's allowed |
|---|---|---|
| Name | `n/` | Letters, digits, spaces, and common punctuation (hyphens, apostrophes, periods, commas, parentheses). Must start with a letter. Up to 100 characters. |
| Phone | `p/` | Digits, with optional `+` at the start and optional spaces, hyphens, or parentheses as separators. Must contain 3–15 digits and end with a digit. Examples: `91234567`, `+6591234567`, `+65-9123-4567`. |
| Email | `e/` | Standard email format (`name@example.com`). Up to 254 characters. |
| Address | `a/` | Any non-empty text. Up to 200 characters. |
| Priority | `pr/` | `yes` (high priority) or `no` (normal). Defaults to `no`. |
| Tag | `at/` / `dt/` | Letters, digits, and the symbols `. + - _ ( ) @ # ! ? '`. Must start with a letter or number. No spaces. Up to 30 characters. |
| Note heading | `h/` | Optional short label for a note, up to 50 characters. Defaults to `General Note`. |
| Note content | `c/` | The note itself, up to 500 characters. |
| Rejection reason | (no marker) | Up to 200 characters. Letters, digits, spaces, and common punctuation. |

<div markdown="span" class="alert alert-info">
:information_source: **A note on duplicates:** Talently treats two candidates as the same person if they share a phone number or email. Two people with the same name but different contact details are allowed.
</div>

[↑ Back to top](#table-of-contents)

---

## The basics

Start here. These are the everyday commands you'll use to add, view, update, and remove candidates.

---

### Viewing help : `help`

Opens a help window with a link to this User Guide. Note that the help window always opens with a default window size, even if you resized it previously.

Format: `help`

![help 2101.png](images/help%202101.png)

[↑ Back to top](#table-of-contents)

---

### Adding a candidate : `add`

Adds a new candidate to Talently.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]`

**Parameters:**

| Parameter | Prefix | Required | Rules |
|---|---|---|---|
| NAME | `n/` | Yes | Letters, digits, spaces, hyphens `-`, apostrophes (both `’` and `’`), periods `.`, slashes `/`, commas `,`, `@` symbols, backticks (`` ` ``), and parentheses `()`. Must start with a letter. Max 100 characters. |
| PHONE | `p/` | Yes | Optional `+` prefix, then digits with optional spaces, hyphens `-`, or parentheses `()` as separators. Must contain 3–15 digits (separators excluded). All-zero formatting (e.g. `000`) is allowed. Examples: `91234567`, `+6591234567`, `+65-9123-4567`, `+62 812 5555 1234`, `+1 (415) 555-2671`. |
| EMAIL | `e/` | Yes | `local@domain` format. Max 254 characters. |
| ADDRESS | `a/` | Yes | Any non-empty text. Max 200 characters. |
| PRIORITY | `pr/` | No | `yes` (high) or `no` (normal). Default: `no`. |

<div markdown="span" class="alert alert-info">
:information_source: **Tags are not set at add time.** You'll create your own categories (tags) in the [tag section](#storing-more-information-about-a-candidate) below and assign them separately. This keeps `add` simple and helps you reuse the same categories across candidates.
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

![add 2101.png](images/add%202101.png)

[↑ Back to top](#table-of-contents)

---

### Listing all candidates : `list`

Displays every candidate, sorted alphabetically by name. Running `list` clears any active sorting or filtering and restores the default alphabetical order.

Format: `list`

* Shows total candidate count.
* If Talently is empty, a prompt appears to add candidates.
* Restores the default alphabetical listing, removing any previous `sort` or `find` results.


[↑ Back to top](#table-of-contents)

---

### Editing a candidate : `edit`

Updates one or more fields of an existing candidate.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`

* `INDEX` refers to the number shown in the current list. Must be a positive integer.
* At least one field must be provided.
* Unspecified fields are unchanged.
* `PRIORITY`: `yes` or `no` (case-insensitive — `YES`, `Yes`, `NO` are all accepted).
* If the new values are identical to the existing ones (including casing), a message indicating no changes were detected is shown and no modification is made. Case-only changes (e.g., `alice` → `Alice`) are treated as real edits.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Editing phone or email to match another existing candidate will fail — duplicates are not allowed.
</div>

<div markdown="span" class="alert alert-info">
:information_source: **Note:** After a successful edit, the displayed list resets to show all candidates. This ensures you can always see the edited candidate even if the previous filter would have hidden it.
</div>

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` — Updates phone and email.
* `edit 2 n/Betsy Crower pr/yes` — Updates name and sets high priority.

![edit 2101.png](images/edit%202101.png)

[↑ Back to top](#table-of-contents)

---

### Showing candidate details : `show`

Opens the full detail panel for a candidate on the right side of the screen. This is the easiest way to see everything about a candidate — their notes, tags, and rejection history — in one place.

Format: `show INDEX`

* `INDEX` must be a positive integer.
* Detail panel displays: name, phone, email, address, priority, date added, tags, all notes (each showing the heading and content), and full rejection history.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show` after `addnote` or `addreject` to verify your changes.
</div>

Examples:
* `show 1` — Opens details for the 1st candidate.
* `find John` then `show 1` — Opens details for the first match.


[↑ Back to top](#table-of-contents)

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


[↑ Back to top](#table-of-contents)

---

## Storing more information about a candidate

Once a candidate exists, you'll want to record more than just their name and contact details. Talently gives you **three ways to capture extra information**, each with its own purpose:

* **Notes** — free-form text for anything you want to remember: interview feedback, follow-up reminders, portfolio highlights.
* **Rejection reasons** — a formal, audit-friendly record of why a candidate didn't move forward. Shown as a red badge on their card.
* **Tags** — short labels (your own categories) for grouping candidates. Use them for hiring stages like `Shortlisted`, skills like `Java`, or any other category you care about.

The commands in this section let you create, update, and remove each of these.

---

### Adding a note to a candidate : `addnote`

Attaches a free-form note to a candidate. Perfect for interview feedback, quick thoughts, or follow-up reminders.

Format: `addnote INDEX c/CONTENT [h/HEADING]`

* `INDEX` must be a positive integer.
* `CONTENT` is required, must not be blank, and must not exceed 500 characters.
* `HEADING` is optional. Defaults to `General Note` if omitted. Must not exceed 50 characters.
* Max 50 notes per candidate.
* Notes are appended in order — earlier notes are never overwritten.
* Newline characters in pasted content are automatically converted to spaces.
* Note content and headings must not contain the sequences ` c/` or ` h/` (space followed by a prefix), as these are interpreted as command prefixes.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use descriptive headings (e.g. `h/Tech Round 1`, `h/HR Interview`) to organise notes by hiring stage. View all notes with `show INDEX`.
</div>

Examples:
* `addnote 1 c/Passed the technical interview flawlessly. h/Tech Round 1`
* `addnote 2 c/Strong communication skills.`

![addnote 2101.png](images/addnote%202101.png)

[↑ Back to top](#table-of-contents)

---

### Editing a note : `editnote`

Edits the content and/or heading of an existing note.

Format: `editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `NOTE_INDEX` is the note's position in the candidate's notes list (positive integer). Use `show INDEX` to see note numbers.
* At least one of `c/CONTENT` or `h/HEADING` must be provided.
* `CONTENT` must not be blank and must not exceed 500 characters.
* `HEADING` must not be blank and must not exceed 50 characters.
* Newline characters in pasted content are automatically converted to spaces.

Examples:
* `editnote 1 1 c/Actually failed the interview.` — updates the content of note 1 for candidate 1, keeping the original heading.
* `editnote 2 3 h/Final Round` — updates only the heading of note 3 for candidate 2.
* `editnote 1 2 c/New content h/New heading` — updates both content and heading.

[↑ Back to top](#table-of-contents)

---

### Deleting a note : `deletenote`

Deletes a note from a candidate's record.

Format: `deletenote INDEX NOTE_INDEX`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `NOTE_INDEX` is the note's position in the candidate's notes list (positive integer). Use `show INDEX` to see note numbers.

Examples:
* `deletenote 1 2` — deletes the 2nd note from candidate 1.
* `deletenote 3 1` — deletes the 1st note from candidate 3.

[↑ Back to top](#table-of-contents)

---

### Adding a rejection reason : `addreject`

Records a formal rejection against a candidate and appends the reason to their rejection history. Unlike notes (which are free-form), rejection reasons give you a **clean audit trail** of every time a candidate was turned down — and why. The red badge on a candidate's card is a lifetime counter, so you can see at a glance who has been rejected and how often.

Format: `addreject INDEX REASON`

* `INDEX` must be a positive integer.
* `REASON`: non-empty, max 200 characters. Allowed characters: letters, digits, spaces, `.` `,` `-` `'` `/` `:` `;` `!` `?` `(` `)` `&` `"` `#` `+` `%` `@` `*`.
* Each `addreject` call appends to the rejection history — previous entries are not overwritten. Max 20 rejection records per candidate.
* The candidate's card shows a **red badge** with the total rejection count (lifetime counter).
* If the same reason is given consecutively, a warning is shown (the rejection is still recorded).

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Rejection reasons are about *why* someone was rejected. Tags are for *what stage* they're at. They work together — for example, tag a candidate with your `Rejected` category and record the specific reason with `addreject`.
</div>

Examples:
* `addreject 1 Failed technical interview`
* `addreject 3 Insufficient experience`

![addreject 2101.png](images/addreject%202101.png)

[↑ Back to top](#table-of-contents)

---

### Editing a rejection reason : `editreject`

Edits an existing rejection reason for a candidate.

Format: `editreject INDEX REJECT_INDEX NEW_REASON`

* `INDEX` is the candidate's position in the displayed list (positive integer).
* `REJECT_INDEX` is the rejection reason's position in the candidate's rejection history (positive integer). Use `show INDEX` to see rejection numbers.
* `NEW_REASON`: non-empty, max 200 characters. Same allowed characters as `addreject`.
* The candidate must have at least one existing rejection reason.

Examples:
* `editreject 1 1 Failed cultural fit interview` — updates the 1st rejection reason for candidate 1.
* `editreject 2 2 Insufficient experience for senior role` — updates the 2nd rejection reason for candidate 2.

[↑ Back to top](#table-of-contents)

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

[↑ Back to top](#table-of-contents)

---

### Managing your tag categories : `tagpool`

**Tags are your own custom categories.** Want to group candidates by hiring stage (`Shortlisted`, `Interviewed`, `Hired`), by skill (`Java`, `Python`, `Design`), or by project (`ProjectAlpha`)? You create the categories yourself, and you decide what they mean.

Before you can tag a candidate, the category must exist in the **tag pool** — a master list of every category you've created. This two-step approach (create the category first, then assign it) keeps your tags consistent and prevents near-duplicates like `Shortlisted` and `shortlist` from creeping in.

Format: `tagpool [at/TAG_TO_CREATE]... [dt/TAG_TO_DELETE]...`

* Running `tagpool` with no arguments lists all tags currently in the pool.
* To create or delete tags, at least one `at/` or `dt/` prefix is required.
* Max 10 tags per command. The pool can hold at most 50 tags total.
* Tag names: must start with a letter or number, followed by letters, numbers, or the symbols `. + - _ ( ) @ # ! ? '`, no spaces, 1–30 characters, case-insensitive (`Python` and `python` are the same).
* Cannot create a tag that already exists, or delete one that does not exist. If you try to create an existing tag, the error message will tell you it already exists — this is the quickest way to check if a tag is in the pool.
* Cannot create and delete the same tag in one command.
* Duplicate tags within the same add or delete list are not allowed (e.g., `tagpool at/Java at/java` is rejected because tags are case-insensitive).
* Tags assigned to candidates are visible on their cards in the list view. Use `list` to see all candidates and their tags at a glance.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Deleting a tag removes it from **all candidates** currently holding it. Use `undo` immediately to reverse. Undo fully restores the tag to the pool **and** to all candidates who had it.
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Typical workflow:** `tagpool at/Shortlisted` (create the category) → `tag 1 at/Shortlisted` (assign it) → `filter Shortlisted` (view everyone in that category).
</div>

Examples:
* `tagpool` — Lists all tags in the pool (e.g., "Tag pool (3 tags): AI, Backend, Frontend").
* `tagpool at/Shortlisted at/Interviewed` — Creates two new tags.
* `tagpool dt/Shortlisted` — Deletes `Shortlisted` from the pool and all candidates.
* `tagpool at/Senior dt/Junior` — Creates `Senior` and deletes `Junior` in one command.


[↑ Back to top](#table-of-contents)

---

### Tagging a candidate : `tag`

Adds or removes tags on one or more candidates. Remember — you must first create the tag in the pool with `tagpool` before you can assign it here.

Format: `tag INDEX[,INDEX]... [at/TAG_TO_ADD]... [dt/TAG_TO_REMOVE]...`

* Single index or comma-separated list (e.g. `1,2,3`). Duplicate indices not allowed.
* Each `INDEX` must be a positive integer.
* At least one `at/` or `dt/` prefix is required.
* Max 10 tags per command. A candidate can hold at most one of each tag in the pool (no duplicates), bounded by the 50-tag pool limit.
* Tags must already exist in the tag pool — use `tagpool` to create them first.
* Cannot add a tag the candidate already has, or remove one they do not have.
* Cannot add and delete the same tag in one command.

Examples:
* `tag 1 at/Shortlisted` — Adds `Shortlisted` to candidate 1.
* `tag 2 dt/Interviewed` — Removes `Interviewed` from candidate 2.
* `tag 3 at/Senior dt/Junior` — Adds `Senior` and removes `Junior` from candidate 3.
* `tag 1,2,3 at/Shortlisted` — Adds `Shortlisted` to candidates 1, 2, and 3.


[↑ Back to top](#table-of-contents)

---

## Finding and organising candidates

Once you have candidates and have recorded information about them, these commands help you find the right ones quickly and arrange the list in useful ways.

---

### Finding candidates : `find`

Searches across all candidates by keywords. `find` looks through names, phone numbers, emails, notes, and rejection reasons — so a single search can catch anyone who matches.

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
* Max 20 keywords. Search keywords max 150 characters total.
* Keywords may contain letters, digits, and common punctuation. Non-ASCII characters (accented letters, emojis) are not supported.
* Duplicate keywords are automatically removed (e.g., `find john john` searches for `john` once).
* `find` replaces any active `filter` — the results show matches from the full candidate list, not the currently filtered view.

<div markdown="span" class="alert alert-info">
:information_source: **Note:** `find` does not search the **address field** or **tags**. Searching by address is excluded as it frequently contains common terms (e.g. "Road", "Avenue", "Street") that create excessive noise and return irrelevant results, diluting the specificity of your search. To find candidates by tag, use the `filter` command instead.
</div>

Examples:
* `find John` — Matches `john`, `John Doe`.
* `find alex david` — Matches candidates whose name contains `alex` or `david`.
* `find overqualified` — Matches candidates whose rejection reasons include "overqualified".
* `find technical interview` — Matches candidates with notes or rejection reasons mentioning `technical` or `interview`.


[↑ Back to top](#table-of-contents)

---

### Filtering candidates by tag : `filter`

Returns all candidates who have a specific tag assigned. This is the fastest way to see everyone in a category you care about.

Format: `filter TAG`

* Exact match (not partial). `Java` does not match `JavaScript`.
* Case-insensitive. `java` matches `Java`.
* Tag must follow naming rules: must start with a letter or number, followed by letters, numbers, or the symbols `. + - _ ( ) @ # ! ? '`, no spaces, 1–30 characters.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `filter` to pull all candidates at a specific hiring stage, e.g. `filter Shortlisted`.
</div>

Examples:
* `filter Shortlisted` — Shows all candidates tagged `Shortlisted`.
* `filter Java` — Shows candidates tagged `Java`, not `JavaScript`.


[↑ Back to top](#table-of-contents)

---

### Sorting candidates by date : `sort date`

Sorts all candidates by the date they were added.

Format: `sort date o/ORDER`

* `ORDER`: `asc` (oldest first) or `desc` (newest first). Case-insensitive (`ASC`, `Desc`, etc. are accepted).
* Candidates added at the **exact same date and time** are sorted alphabetically by name.
* After sorting, any active filter is cleared and all candidates are displayed in the new order.
* This action is undoable with `undo`.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Cannot sort an empty candidate list — an error is shown if Talently has no candidates.
</div>

Examples:
* `sort date o/asc` — Oldest candidates first.
* `sort date o/desc` — Newest candidates first.


[↑ Back to top](#table-of-contents)

---

### Sorting candidates by priority : `sort pr`

Sorts all candidates by their priority flag.

Format: `sort pr o/ORDER`

* `ORDER`: `asc` (low-priority first) or `desc` (high-priority first). Case-insensitive (`ASC`, `Desc`, etc. are accepted).
* Secondary sort: by date and time added (newest first), then alphabetically by name.
* After sorting, any active filter is cleared and all candidates are displayed in the new order.
* This action is undoable with `undo`.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `sort pr o/desc` to immediately surface your most important candidates.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Cannot sort an empty candidate list — an error is shown if Talently has no candidates.
</div>

Examples:
* `sort pr o/asc` — Low-priority candidates first.
* `sort pr o/desc` — High-priority candidates first.


[↑ Back to top](#table-of-contents)

---

## Undo, redo, and clean-up

---

### Undoing the last modifying command : `undo`

Reverts Talently to the state before the most recent data-changing command. Talently remembers the exact sequence of your actions — each `undo` steps back one action at a time, so repeated use of `undo` continues stepping further back through your history.

Format: `undo`

* Applies to: `add`, `edit`, `remove`, `addreject`, `editreject`, `deletereject`, `tag`, `tagpool`, `addnote`, `editnote`, `deletenote`, `sort`, `clear`.
* If there is nothing to undo, an error is shown.

Examples:
* `remove 2` then `undo` — Restores the removed candidate.


[↑ Back to top](#table-of-contents)

---

### Redoing the last undone command : `redo`

Re-applies the most recently undone data-changing command. This applies to the same set of commands as `undo`: `add`, `edit`, `remove`, `addreject`, `editreject`, `deletereject`, `tag`, `tagpool`, `addnote`, `editnote`, `deletenote`, `sort`, and `clear`.

Format: `redo`

* Can only be used after `undo`. If there is no undone state, an error is shown.
* Any new modifying command after `undo` clears the redo history.

Examples:
* `remove 2` → `undo` → `redo` — Re-applies the removal.


[↑ Back to top](#table-of-contents)

---

### Clearing all entries : `clear`

Deletes every candidate and all tags from Talently.

Format: `clear`

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Removes all candidate data **and the entire tag pool**. Use `undo` immediately to recover.
</div>


[↑ Back to top](#table-of-contents)

---

### Exiting the program : `exit`

Closes Talently.

Format: `exit` (Alternatively, you can use `Cmd + Q` on macOS or `Ctrl + Q` on Windows/Linux)

[↑ Back to top](#table-of-contents)

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
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`<br> e.g. `edit 2 n/James Lee e/jameslee@example.com`
**Exit** | `exit`
**Filter** | `filter TAG`<br> e.g. `filter Shortlisted`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g. `find James Jake`
**Help** | `help`
**List** | `list`
**Add Note** | `addnote INDEX c/CONTENT [h/HEADING]`<br> e.g. `addnote 1 c/Passed interview h/Tech Round 1`
**Delete Note** | `deletenote INDEX NOTE_INDEX`<br> e.g. `deletenote 1 2`
**Edit Note** | `editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]`<br> e.g. `editnote 1 1 c/Updated content`
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
| **Parameter** | A value supplied to a command, usually introduced by a two-character prefix such as `n/`, `p/`, `e/`, `a/`, `pr/`, `h/`. |
| **Prefix** | The short marker (e.g. `n/`) that introduces a parameter in a command. Must be preceded by a space when not at the start of the command. |
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
| **Home folder** | The folder containing `talently.jar`. Talently reads and writes `data/talently.json` and `preferences.json` relative to this folder. |
| **Save file** | `data/talently.json` — the JSON file where candidate data is autosaved after every modifying command. |
| **Autosave** | The automatic write to the save file after any command that changes data. No manual save is needed. |
| **Undo history** | The internal record of data-modifying commands. `undo` steps back through this history one action at a time; `redo` replays the most recently undone action, until a new modifying command clears it. |
| **Duplicate candidate** | Any new or edited candidate whose phone number **or** email matches an existing candidate. Talently rejects duplicates. Name alone does not determine uniqueness. |
| **ASCII input** | Plain, printable characters in the US-ASCII range (letters, digits, spaces, common punctuation). Talently only accepts ASCII in text fields — non-ASCII characters are rejected. |
