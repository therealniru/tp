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

**Assumed knowledge:** You can open a terminal and type commands. No programming experience required.

* Table of Contents
{:toc}

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

![Ui](images/Ui.png)

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
| 4 | `note 1 n/Strong portfolio, schedule follow-up. h/Initial Screen` | Attach a note |
| 5 | `tagpool a/Shortlisted` | Create a new tag in the tag pool |
| 6 | `tag 1 a/Shortlisted` | Assign the tag to the candidate |
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
* `…` after an item = repeatable. e.g. `[a/TAG]…` can be `a/Java a/Python`.
* Parameters can be in any order.
* Commands with no parameters (e.g. `help`, `list`, `exit`, `clear`, `undo`, `redo`) do not take any arguments.
* Avoid copying multi-line commands from a PDF — spaces around line breaks may be lost.
* Prefixes like `n/`, `p/`, `e/`, `a/` are recognised when preceded by a space. If your input text naturally contains these sequences (e.g., an address containing ` a/` or ` n/`), the text after them may be mis-parsed. Rephrase if needed (e.g., `a/Blk 123 North Rd` instead of `a/Blk 123 n/orth Rd`).
* **Input normalization:** Names have extra whitespace collapsed (e.g., `John    Doe` becomes `John Doe`). Emails are automatically lowercased (e.g., `John@Gmail.COM` becomes `john@gmail.com`). Phone numbers with or without the `+` prefix are treated as the same number for duplicate detection (e.g., `+6591234567` and `6591234567` are duplicates).
* **Providing duplicate prefixes** (e.g., `n/Alice n/Bob`) in a single command is not allowed and will be rejected with an error.

</div>

---

### Viewing help : `help`

Opens a help window with a link to this User Guide.

Format: `help`

![help message](images/helpMessage.png)

---

### Adding a candidate : `add`

Adds a new candidate to Talently.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY] [s/STATUS]`

**Parameters:**

| Parameter | Prefix | Required | Rules |
|---|---|---|---|
| NAME | `n/` | Yes | Letters, spaces, hyphens, apostrophes, periods, slashes. No digits. Max 100 characters. |
| PHONE | `p/` | Yes | Digits only, optional `+` prefix. 3–15 digits. |
| EMAIL | `e/` | Yes | `local@domain` format. Max 254 characters. |
| ADDRESS | `a/` | Yes | Any non-empty text. |
| PRIORITY | `pr/` | No | `yes` (high) or `no` (normal). Default: `no`. |
| STATUS | `s/` | No | `active`, `rejected`, `hired`, `blacklisted`. Default: `active`. |

<div markdown="span" class="alert alert-info">
:information_source: **Tags are not set at add time.** First create tags with `tagpool`, then assign them with `tag`. See [Managing the tag pool](#managing-the-tag-pool--tagpool) and [Tagging a candidate](#tagging-a-candidate--tag).
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `pr/yes` to flag high-priority candidates. Surface them later with `sort pr o/asc`.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Duplicate detection is based on phone number **or** email — not name. If a new candidate shares the same phone **or** email as an existing one, it is rejected as a duplicate. Two candidates with the same name but different phone and email are allowed.
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy O'Brien e/betsy@example.com a/Newgate Prison p/+6591234567 pr/yes`
* `add n/Jane Smith p/91234567 e/jane@example.com a/Clementi Ave 3 s/hired`

<p align="center"><img src="images/add%20command.png" alt="Expected result after running the add command" width="730"/></p>

---

### Listing all candidates : `list`

Displays every candidate, sorted alphabetically by name.

Format: `list`

* Shows total candidate count.
* If Talently is empty, a prompt appears to add candidates.

<p align="center"><img src="images/list%20command.png" alt="Expected result after running the list command" width="730"/></p>

---

### Editing a candidate : `edit`

Updates one or more fields of an existing candidate.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY] [s/STATUS]`

* `INDEX` refers to the number shown in the current list. Must be a positive integer.
* At least one field must be provided.
* Unspecified fields are unchanged.
* `PRIORITY`: `yes` or `no` (case-insensitive — `YES`, `Yes`, `NO` are all accepted).
* `STATUS`: `active`, `rejected`, `hired`, `blacklisted` (case-insensitive).
* If the new values are identical to the existing ones, a message indicating no changes were detected is shown and no modification is made.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** To re-activate a previously rejected candidate, use `edit INDEX s/active`. Their rejection history is preserved.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Editing phone or email to match another existing candidate will fail — duplicates are not allowed.
</div>

<div markdown="span" class="alert alert-info">
:information_source: **Note:** After a successful edit, the displayed list resets to show all candidates. This ensures you can always see the edited candidate even if the previous filter would have hidden it.
</div>

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` — Updates phone and email.
* `edit 2 n/Betsy Crower pr/yes` — Updates name and sets high priority.
* `edit 1 s/hired` — Marks candidate as hired.
* `edit 3 s/active` — Re-activates a rejected candidate.

<p align="center"><img src="images/edit%20command.png" alt="Expected result after running the edit command" width="730"/></p>

---

### Showing candidate details : `show`

Opens the full detail panel for a candidate on the right side of the screen.

Format: `show INDEX`

* `INDEX` must be a positive integer.
* Detail panel displays: name, phone, email, address, status, priority, date added, tags, all notes (with headings, content, and timestamps), and full rejection history.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show` after `note` or `reject` to verify your changes.
</div>

Examples:
* `show 1` — Opens details for the 1st candidate.
* `find John` then `show 1` — Opens details for the first match.

<p align="center"><img src="images/show%20command.png" alt="Expected result after running the show command" width="730"/></p>

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
* Max 20 keywords. Total command length max 150 characters.
* Keywords may contain: letters, digits, `-` `'` `.` `/` `@` `+` `_` (non-ASCII characters such as accented letters or emojis are not supported)
* Duplicate keywords are automatically removed (e.g., `find john john` searches for `john` once).
* `find` replaces any active `filter` — the results show matches from the full candidate list, not the currently filtered view.

<div markdown="span" class="alert alert-info">
:information_source: **Note:** `find` does not search the address field. To locate a candidate by address, scroll through the list or use `show` on individual candidates.
</div>

Examples:
* `find John` — Matches `john`, `John Doe`.
* `find alex david` — Matches candidates whose name contains `alex` or `david`.
* `find overqualified` — Matches candidates whose rejection reasons include "overqualified".
* `find technical interview` — Matches candidates with notes or rejection reasons mentioning `technical` or `interview`.

<p align="center"><img src="images/find%20command.png" alt="Expected result after running the find command" width="730"/></p>

---

### Filtering candidates by tag : `filter`

Returns all candidates who have a specific tag assigned.

Format: `filter TAG`

* Exact match (not partial). `Java` does not match `JavaScript`.
* Case-insensitive. `java` matches `Java`.
* Tag must follow naming rules: must start with a letter or number, followed by letters, numbers, hyphens, dots, or `+` signs, no spaces, 1–30 characters.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `filter` to pull all candidates at a specific hiring stage, e.g. `filter Shortlisted`.
</div>

Examples:
* `filter Shortlisted` — Shows all candidates tagged `Shortlisted`.
* `filter Java` — Shows candidates tagged `Java`, not `JavaScript`.

<p align="center"><img src="images/filter%20command.png" alt="Expected result after running the filter command" width="730"/></p>

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

<p align="center"><img src="images/remove%20command.png" alt="Expected result after running the remove command" width="730"/></p>

---

### Rejecting a candidate : `reject`

Records a rejection against a candidate and appends a reason to their history.

Format: `reject INDEX r/REASON`

* `INDEX` must be a positive integer.
* `REASON`: non-empty, max 200 characters. Allowed characters: letters, digits, spaces, `.` `,` `-` `'` `/` `:` `;` `!` `?` `(` `)`.
* **Automatically sets the candidate's status to `rejected`.**
* Each `reject` call appends to the rejection history — previous entries are not overwritten.
* The candidate's card shows a **red badge** with the total rejection count.
* If the same reason is given consecutively, a warning is shown.
* Blacklisted candidates cannot be rejected.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show INDEX` after rejecting to view the full rejection history in the detail panel.
</div>

Examples:
* `reject 1 r/Failed technical interview`
* `reject 3 r/Insufficient experience`

<p align="center"><img src="images/reject%20command.png" alt="Expected result after running the reject command" width="730"/></p>

---

### Candidate status reference

Talently tracks each candidate's stage using a `status` field.

| Status | Colour in detail panel | Meaning |
|---|---|---|
| `active` | Blue | In the active pipeline |
| `rejected` | Red | Has been rejected |
| `hired` | Green | Successfully hired |
| `blacklisted` | Grey | Blocked from future consideration |

**How status is set:**
* Defaults to `active` on `add`.
* `reject` automatically sets it to `rejected`.
* Change status at any time with `edit INDEX s/STATUS`.
* The detail panel auto-refreshes when a displayed candidate's data changes.

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

<p align="center"><img src="images/sort%20command.png" alt="Expected result after running sort date" width="730"/></p>

---

### Sorting candidates by priority : `sort pr`

Sorts all candidates by their priority flag.

Format: `sort pr o/ORDER`

* `ORDER`: `asc` (high-priority first) or `desc` (high-priority last). Case-insensitive (`ASC`, `Desc`, etc. are accepted).
* Secondary sort: by date added (newest first), then alphabetically by name.
* After sorting, any active filter is cleared and all candidates are displayed in the new order.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `sort pr o/asc` to immediately surface your most important candidates.
</div>

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Cannot sort an empty candidate list — an error is shown if Talently has no candidates.
</div>

Examples:
* `sort pr o/asc` — High-priority candidates first.
* `sort pr o/desc` — High-priority candidates last.

<p align="center"><img src="images/sort%20pr%20command.png" alt="Expected result after running sort pr" width="730"/></p>

---

### Adding a note to a candidate : `note`

Adds a timestamped note to a candidate's record.

Format: `note INDEX n/CONTENT [h/HEADING]`

* `INDEX` must be a positive integer.
* `CONTENT` is required, must not be blank, and must not exceed 500 characters.
* `HEADING` is optional. Defaults to `General Note` if omitted. Must not exceed 50 characters.
* Each note is automatically stamped with the current date and time.
* Notes are appended in order — earlier notes are never overwritten.
* Note content and headings must not contain the sequences ` n/` or ` h/` (space followed by a prefix), as these are interpreted as command prefixes.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use descriptive headings (e.g. `h/Tech Round 1`, `h/HR Interview`) to organise notes by hiring stage. View all notes with `show INDEX`.
</div>

Examples:
* `note 1 n/Passed the technical interview flawlessly. h/Tech Round 1`
* `note 2 n/Strong communication skills.`

<p align="center"><img src="images/note%20command.png" alt="Expected result after running the note command" width="730"/></p>

---

### Managing the tag pool : `tagpool`

Creates or deletes tags in the master tag registry.

**Tags must exist in the pool before they can be assigned to candidates.**

Format: `tagpool [a/TAG_TO_CREATE]... [d/TAG_TO_DELETE]...`

* At least one `a/` or `d/` prefix is required.
* Max 10 tags per command.
* Tag names: must start with a letter or number, followed by letters, numbers, hyphens, dots, or `+` signs, no spaces, 1–30 characters, case-insensitive (`Python` and `python` are the same).
* Cannot create a tag that already exists, or delete one that does not exist.
* Cannot create and delete the same tag in one command.
* Duplicate tags within the same add or delete list are not allowed (e.g., `tagpool a/Java a/java` is rejected because tags are case-insensitive).

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Deleting a tag removes it from **all candidates** currently holding it. Use `undo` immediately to reverse.
</div>

<div markdown="span" class="alert alert-primary">
:bulb: **Workflow:** `tagpool a/Shortlisted` → `tag 1 a/Shortlisted` → `filter Shortlisted`
</div>

Examples:
* `tagpool a/Shortlisted a/Interviewed` — Creates two new tags.
* `tagpool d/Shortlisted` — Deletes `Shortlisted` from the pool and all candidates.
* `tagpool a/Senior d/Junior` — Creates `Senior` and deletes `Junior` in one command.

<p align="center"><img src="images/tagpool%20command.png" alt="Expected result after running the tagpool command" width="730"/></p>

---

### Tagging a candidate : `tag`

Adds or removes tags on one or more candidates.

Format: `tag INDEX[,INDEX]... [a/TAG_TO_ADD]... [d/TAG_TO_REMOVE]...`

* Single index or comma-separated list (e.g. `1,2,3`). Duplicate indices not allowed.
* Each `INDEX` must be a positive integer.
* At least one `a/` or `d/` prefix is required.
* Max 10 tags per command.
* Tags must already exist in the tag pool — use `tagpool` to create them first.
* Cannot add a tag the candidate already has, or remove one they do not have.
* Cannot add and delete the same tag in one command.

Examples:
* `tag 1 a/Shortlisted` — Adds `Shortlisted` to candidate 1.
* `tag 2 d/Interviewed` — Removes `Interviewed` from candidate 2.
* `tag 3 a/Senior d/Junior` — Adds `Senior` and removes `Junior` from candidate 3.
* `tag 1,2,3 a/Shortlisted` — Adds `Shortlisted` to candidates 1, 2, and 3.

<p align="center"><img src="images/tag%20command.png" alt="Expected result after running the tag command" width="730"/></p>

---

### Undoing the last modifying command : `undo`

Reverts Talently to the state before the most recent data-changing command.

Format: `undo`

* Applies to: `add`, `edit`, `remove`, `reject`, `tag`, `tagpool`, `note`, `sort`, `clear`.
* If there is nothing to undo, an error is shown.

Examples:
* `remove 2` then `undo` — Restores the removed candidate.

<p align="center"><img src="images/undo%20command.png" alt="Expected result after running the undo command" width="730"/></p>

---

### Redoing the last undone command : `redo`

Re-applies the most recently undone command.

Format: `redo`

* Can only be used after `undo`. If there is no undone state, an error is shown.
* Any new modifying command after `undo` clears the redo history.

Examples:
* `remove 2` → `undo` → `redo` — Re-applies the removal.

<p align="center"><img src="images/redo%20command.png" alt="Expected result after running the redo command" width="730"/></p>

---

### Clearing all entries : `clear`

Deletes every candidate and all tags from Talently.

Format: `clear`

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Removes all candidate data **and the entire tag pool**. Use `undo` immediately to recover.
</div>

<p align="center"><img src="images/clear%20command.png" alt="Expected result after running the clear command" width="730"/></p>

---

### Exiting the program : `exit`

Closes Talently.

Format: `exit`

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

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q: How do I transfer data to another computer?**
A: Copy `data/talently.json` from your current home folder to the same location on the new machine after installing Talently there.

**Q: Can I undo a `clear`?**
A: Yes — run `undo` immediately after `clear` to restore all candidates.

**Q: Why can't I assign a tag to a candidate?**
A: Tags must first exist in the pool. Run `tagpool a/TAG_NAME`, then `tag INDEX a/TAG_NAME`.

**Q: What does the red badge on a candidate card mean?**
A: It shows the total number of times that candidate has been rejected. Run `show INDEX` to view the full rejection history.

**Q: How do I view all notes for a candidate?**
A: Use `show INDEX`. The detail panel lists all notes with headings, content, and timestamps.

**Q: Can two candidates have the same name?**
A: Yes — Talently identifies duplicates by phone **or** email, not name. Two candidates with the same name but different phone and email are allowed.

**Q: What happens when I open a save file from an older version?**
A: The legacy status value `NONE` is automatically migrated to `active`. Save files with other unrecognised status values will fail to load — back up your data file before upgrading.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **Multiple screens:** If Talently was last used on a secondary screen that is now disconnected, the window may open off-screen.
   **Fix:** Delete `preferences.json` from the home folder before relaunching.

2. ~~**Minimised Help Window:**~~ **Fixed.** The help window is now automatically restored and brought to front when `help` is run again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY] [s/STATUS]` <br> e.g. `add n/James Ho p/22224444 e/jamesho@example.com a/123 Clementi Rd`
**Clear** | `clear`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY] [s/STATUS]`<br> e.g. `edit 2 n/James Lee e/jameslee@example.com s/hired`
**Exit** | `exit`
**Filter** | `filter TAG`<br> e.g. `filter Shortlisted`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g. `find James Jake`
**Help** | `help`
**List** | `list`
**Note** | `note INDEX n/CONTENT [h/HEADING]`<br> e.g. `note 1 n/Passed interview h/Tech Round 1`
**Redo** | `redo`
**Reject** | `reject INDEX r/REASON`<br> e.g. `reject 1 r/Failed technical interview`
**Remove** | `remove INDEX`<br> e.g. `remove 3`
**Show** | `show INDEX`<br> e.g. `show 1`
**Sort (date)** | `sort date o/ORDER`<br> e.g. `sort date o/desc`
**Sort (priority)** | `sort pr o/ORDER`<br> e.g. `sort pr o/asc`
**Tag** | `tag INDEX[,INDEX]... [a/TAG]... [d/TAG]...`<br> e.g. `tag 1,2 a/Shortlisted d/Applied`
**Tag Pool** | `tagpool [a/TAG]... [d/TAG]...`<br> e.g. `tagpool a/Shortlisted d/Rejected`
**Undo** | `undo`
