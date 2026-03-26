---
layout: page
title: User Guide
---

Talently is a desktop application for recruiters and hiring managers who need to efficiently track and manage job candidates throughout the hiring pipeline. Optimised for fast typists, Talently uses a Command Line Interface (CLI) for speed while providing a Graphical User Interface (GUI) for visual feedback. If you can type quickly, Talently lets you manage candidates faster than traditional point-and-click applications.

**Target Users**
Talently is designed for tech-savvy recruiters and hiring managers who:
* Handle a high volume of job applicants and need quick access to candidate information.
* Are comfortable typing commands and prefer keyboard-driven workflows over mouse-based navigation.
* Need to track candidate progress through multiple hiring stages using tags, notes, and rejection records.

**Assumed Prior Knowledge**
Basic familiarity with using a command terminal (e.g., navigating directories with the `cd` command). No programming experience is required.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. **Install Java**
   Ensure you have Java `17` or above installed on your computer.
   <div markdown="span" class="alert alert-info">
   **:information_source: Note:** Mac users must install the precise JDK version prescribed in the [SE-Education Java Installation guide for Mac](https://se-education.org/guides/tutorials/javaInstallationMac.html).
   </div>
   To verify your Java version, open a terminal and run: `java -version`. You should see output indicating version 17 or higher.

2. **Download Talently**
   Download the latest `talently.jar` file from the [Talently GitHub Releases page](https://github.com/AY2526S2-CS2103T-T17-4/tp/releases).

3. **Set Up the Application**
   Copy the downloaded `.jar` file into the folder you want to use as the home folder for Talently. This folder will also store your candidate data.

4. **Launch Talently**
   Open a command terminal, navigate to the folder containing the `.jar` file, and run the `java -jar talently.jar` command.<br>
   The GUI should appear within a few seconds. The app comes pre-loaded with sample data so you can explore its features immediately.<br>
   ![Ui](images/Ui.png)

5. **Overview of the User Interface**
   The Talently interface consists of four main areas:
   * **Command Box (Top):** Type commands here and press Enter to execute.
   * **Result Display (Below command box):** Shows feedback messages after each command.
   * **Candidate List (Left panel):** Displays candidates with index, name, phone, email, address, tags, and priority. Each card displays an index number, name (with a ⭐ indicator if high priority), date added, tags, contact info, and a rejection count badge.
   * **Candidate Detail Panel (Right panel):** Shows full details of a selected candidate.

6. **Try Your First Commands**
   Type these commands in the command box and press Enter after each one:
   * `list` : View all candidates.
   * `add n/Jane Smith p/91234567 e/jane@example.com a/10 Havelock Road` : Add a new candidate.
   * `find Jane` : Search for candidates matching `Jane`.
   * `help` : Open the help window.

   Once you are comfortable, refer to the [Features](#features) section below for the full list of commands.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes About the Command Format:**<br>

* Words in `UPPER_CASE` are parameters to be supplied by you.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter: `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [pr/PRIORITY]` can be used as `n/John Doe pr/yes` or `n/John Doe`.

* Items with `…`​ after them can be used multiple times (including zero times).<br>
  e.g. `[a/TAG]…​` can be used as ` ` (nothing), `a/Shortlisted`, or `a/Shortlisted a/Interviewed`.

* Parameters can be in any order.<br>
  e.g. `n/NAME p/PHONE` and `p/PHONE n/NAME` are both acceptable.

* Commands without parameters (e.g. `help`, `list`, `exit`, `clear`) ignore extraneous input.<br>
  e.g. `help 123` is treated as `help`.
  
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Opens a help window with a link to this User Guide.

![help message](images/helpMessage.png)

Format: `help`

### Adding a candidate : `add`

Adds a new candidate to Talently.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]`

**Parameter Constraints:**

| Parameter | Prefix | Required | Rules |
|-----------|--------|----------|-------|
| NAME | `n/` | Yes | Letters, spaces, hyphens (`-`), apostrophes (`'`), periods (`.`), and slashes (`/`) only. No digits. Max 100 characters. |
| PHONE | `p/` | Yes | Digits only, with optional `+` prefix (E.164 format). Between 3 and 15 digits. |
| EMAIL | `e/` | Yes | Valid email in `local@domain` format. Max 254 characters. |
| ADDRESS | `a/` | Yes | Any non-empty text. |
| PRIORITY | `pr/` | No | `yes` (high priority) or `no` (normal). Defaults to `no`. |

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `pr/yes` to flag high-priority candidates so you can sort them to the top later with `sort pr o/asc`.
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy O'Brien e/betsy@example.com a/Newgate Prison p/+6591234567 pr/yes`

### Listing all candidates : `list`

Displays all candidates in Talently, sorted alphabetically by name.

Format: `list`

* Shows the total number of candidates.
* If no candidates exist, a message prompts you to add one.

### Editing a candidate : `edit`

Modifies an existing candidate's details.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`

* Edits the candidate at the specified `INDEX` (the index shown in the currently displayed list).
* `INDEX` must be a positive integer (1, 2, 3, …).
* At least one optional field must be provided.
* Only the specified fields are updated; all other fields remain unchanged.
* `PRIORITY` accepts `yes` or `no`.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Editing a candidate's name or phone to match an existing candidate will result in an error, as Talently does not allow duplicate candidates.
</div>

Examples:
* `edit 1 p/91234567 e/johndoe@example.com`<br> Updates the phone and email of the 1st candidate.
* `edit 2 n/Betsy Crower pr/yes`<br> Updates the name and sets high priority for the 2nd candidate.

### Showing candidate details : `show`

Displays the full details of a candidate in the detail panel on the right side of the interface.

Format: `show INDEX`

* Shows the candidate at the specified `INDEX`.
* `INDEX` must be a positive integer (1, 2, 3, …).
* The detail panel displays: name, phone, email, address, priority, date added, all tags, notes (with headings and content), and rejection history.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show` after adding notes or rejections to verify your changes in the detail panel.
</div>

Examples:
* `show 1`<br> Displays full details of the 1st candidate.
* `find John` followed by `show 1`<br> Shows the first candidate from the search results.

### Locating candidates : `find`

Finds candidates whose name, phone, email, notes, or rejection reasons contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* Search is case-insensitive. e.g. `hans` matches `Hans`.
* Partial matches are supported. e.g. `Han` matches `Hans`.
* Name, phone, email, note headings, note content, and rejection reasons are all searched.
* Candidates matching at least one keyword are returned (`OR` search). e.g. `Hans Bo` returns both `Hans Gruber` and `Bo Yang`.
* Maximum of 20 keywords per search.
* Total command length must not exceed 150 characters.
* Keywords may contain: letters, digits, and the symbols `-` `'` `.` `/` `@` `+` `_`

Examples:
* `find John` returns `john` and `John Doe`.
* `find alex david` returns `Alex Yeoh` and `David Li`.<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)
* `find technical interview` returns candidates with notes or rejection reasons mentioning `technical` or `interview`.

### Filtering candidates by exact tag: `filter`

Finds candidates whose tags exactly match the given tag name.

Format: `filter TAG`

* Matching is exact (not substring-based). `Java` does not match `JavaScript`.
* Matching is case-insensitive. `java` matches `Java`.
* The tag must follow tag naming rules: alphanumeric, no spaces, 1–30 characters.

Examples:
* `filter Shortlisted` returns all candidates tagged `Shortlisted`.
* `filter Java` returns candidates tagged `Java`, but not `JavaScript`.

### Removing a candidate : `remove`

Permanently removes a candidate from Talently.

Format: `remove INDEX`

* Removes the candidate at the specified `INDEX`.
* `INDEX` refers to the number shown in the currently displayed list.
* `INDEX` must be a positive integer (1, 2, 3, …).

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** This action permanently deletes the candidate and all their associated data (notes, tags, rejection history). Use `undo` immediately if you remove a candidate by mistake.
</div>

Examples:
* `list` followed by `remove 2` removes the 2nd candidate in the full list.
* `find Betsy` followed by `remove 1` removes the 1st candidate from the search results.

### Rejecting a candidate : `reject`

Records a rejection against a candidate with a stated reason.

Format: `reject INDEX r/REASON`

* Rejects the candidate at the specified `INDEX`.
* `INDEX` must be a positive integer (1, 2, 3, …).
* `REASON` must be non-empty, at most 200 characters, and may contain: letters, digits, spaces, and the punctuation `.` `,` `-` `'` `/`.
* Each `reject` call appends a new entry to the candidate's rejection history — previous entries are preserved.
* The candidate's card displays a red badge showing the total rejection count.
* If the same reason is given consecutively, a warning is shown.
* Cannot reject an archived candidate.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `show INDEX` after rejecting a candidate to view their full rejection history in the detail panel.
</div>

Examples:
* `reject 1 r/Failed technical interview`
* `reject 3 r/Insufficient experience`

### Sorting candidates by date : `sort date`

Sorts all candidates by the date they were added to Talently.

Format: `sort date o/ORDER`

* `ORDER` must be `asc` (oldest first) or `desc` (newest first).
* Candidates with the same date are sorted alphabetically by name.

Examples:
* `sort date o/asc` — Oldest candidates first.
* `sort date o/desc` — Newest candidates first.

### Sorting candidates by priority : `sort pr`

Sorts all candidates by their priority status.

Format: `sort pr o/ORDER`

* `ORDER` must be `asc` (high-priority candidates first) or `desc` (high-priority candidates last).
* Secondary sort: by date added (newest first), then alphabetically by name.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use `sort pr o/asc` to quickly surface your high-priority candidates at the top of the list.
</div>

Examples:
* `sort pr o/asc` — High-priority candidates appear first.
* `sort pr o/desc` — High-priority candidates appear last.

### Adding a note to a candidate : `note`

Appends a timestamped note to a candidate's record.

Format: `note INDEX n/CONTENT [h/HEADING]`

* Adds a note to the candidate at the specified `INDEX`.
* `INDEX` must be a positive integer (1, 2, 3, …).
* `CONTENT` is required and must not be blank.
* `HEADING` is optional. If omitted, it defaults to `General Note`.
* Each note is automatically stamped with the current date and time.
* Notes are appended chronologically — earlier notes are never overwritten.

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:** Use descriptive headings (e.g., `h/Tech Round 1`, `h/HR Interview`) to organise notes by hiring stage. View all notes with `show INDEX`.
</div>

Examples:
* `note 1 n/Passed the technical interview flawlessly. h/Tech Round 1`
* `note 2 n/Strong communication skills.`

### Managing the tag pool : `tagpool`

Creates or deletes tags in the master tag registry. Tags must exist in the pool before they can be assigned to candidates.

Format: `tagpool [a/TAG_TO_CREATE]... [d/TAG_TO_DELETE]...`

* At least one `a/` or `d/` prefix must be provided.
* A maximum of 10 tags can be processed per command.
* Tag names must be alphanumeric, no spaces, 1–30 characters.
* Tag names are case-insensitive (`Python` and `python` are the same tag).
* Cannot create a tag that already exists, or delete a tag that does not exist.
* Cannot create and delete the same tag in one command.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** Deleting a tag from the pool also removes it from all candidates currently holding that tag. This action is irreversible unless you use `undo` immediately.
</div>

Examples:
* `tagpool a/Shortlisted a/Interviewed` — Creates two new tags.
* `tagpool d/Shortlisted` — Deletes `Shortlisted` from the pool and all candidates.
* `tagpool a/Senior d/Junior` — Creates `Senior` and deletes `Junior` in one command.

### Tagging a candidate : `tag`

Adds or removes tags on one or more candidates.

Format: `tag INDEX[,INDEX]... [a/TAG_TO_ADD]... [d/TAG_TO_REMOVE]...`

* Accepts a single index or a comma-separated list of indices (e.g., `1,2,3`).
* Each `INDEX` must be a positive integer (1, 2, 3, …). Duplicate indices are not allowed.
* At least one `a/` or `d/` prefix must be provided.
* A maximum of 10 tags can be processed per command.
* Tags must already exist in the tag pool — use `tagpool` to create them first.
* Cannot add a tag the candidate already has, or remove a tag the candidate does not have.
* Cannot add and delete the same tag in one command.

Examples:
* `tag 1 a/Shortlisted` — Adds `Shortlisted` to the 1st candidate.
* `tag 2 d/Interviewed` — Removes `Interviewed` from the 2nd candidate.
* `tag 3 a/Senior d/Junior` — Adds `Senior` and removes `Junior` from the 3rd candidate.
* `tag 1,2,3 a/Shortlisted` — Adds `Shortlisted` to candidates 1, 2, and 3.

### Undoing the last modifying command : `undo`

Reverts Talently's data to the state before the most recent command that modified the candidate list.

Format: `undo`

* Only applies to commands that changed data (e.g., `add`, `edit`, `remove`, `reject`, `tag`, `clear`).
* If there is no previous state to restore, an error message is shown.
* Does not accept additional arguments.

Examples:
* `remove 2` followed by `undo` restores the removed candidate.

### Redoing the last undone command : `redo`

Re-applies the most recently undone command.

Format: `redo`

* Can only be used after an `undo`. If there is no undone state, an error message is shown.
* Does not accept additional arguments.
* Performing any new modifying command after `undo` clears the redo history.

Examples:
* `remove 2` then `undo` then `redo` re-applies the removal.

### Clearing all entries : `clear`

Deletes all candidates from Talently.

Format: `clear`

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** This removes all candidate data permanently. Use `undo` immediately if executed by mistake.
</div>

### Exiting the program : `exit`

Closes the Talently application.

Format: `exit`

### Saving the data

Talently saves your data to disk automatically after every command that modifies the candidate list. There is no need to save manually.

### Editing the data file

Talently data is stored as a JSON file at: `[JAR file location]/data/talently.json`

Advanced users may edit this file directly.

<div markdown="span" class="alert alert-warning">
:warning: **Warning:** If your edits make the data file invalid, Talently will discard all data and start with an empty file on the next launch. Back up the file before editing. Certain invalid values (e.g., out-of-range fields) may cause unexpected behaviour.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q: How do I transfer my data to another computer?**<br>
**A:** Install Talently on the other computer, then copy the `data/talently.json` file from your current home folder to the new installation's home folder, replacing the empty data file.

**Q: Can I undo a clear command?**<br>
**A:** Yes. Use `undo` immediately after `clear` to restore all candidates.

**Q: What happens if I add a candidate with the same name as an existing one?**<br>
**A:** Talently identifies candidates by phone number and email. Two candidates may share the same name as long as they have different phone numbers and email addresses.

**Q: Why can't I assign a tag to a candidate?**<br>
**A:** Tags must first be created in the tag pool using `tagpool a/TAG_NAME`. Only tags that exist in the pool can be assigned to candidates.

**Q: How do I view a candidate's notes and rejection history?**<br>
**A:** Use `show INDEX` to display the candidate's full details — including all notes and rejection records — in the detail panel.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **Multiple-screen setups:** If you move Talently to a secondary screen and later disconnect that screen, the application window may open off-screen.
   **Fix:** Delete the `preferences.json` file in the home folder before relaunching.

2. **Minimised Help Window:** If the Help Window is minimised and you run `help` again, the minimised window will not automatically restore.
   **Fix:** Manually restore the minimised Help Window from the taskbar.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123 Clementi Rd`
**Clear** | `clear`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Filter** | `filter TAG`<br> e.g., `filter Shortlisted`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Note** | `note INDEX n/CONTENT [h/HEADING]`<br> e.g., `note 1 n/Passed interview. h/Tech Round 1`
**Redo** | `redo`
**Remove** | `remove INDEX`<br> e.g., `remove 3`
**Reject** | `reject INDEX r/REASON`<br> e.g., `reject 1 r/Failed technical interview`
**Show** | `show INDEX`<br> e.g., `show 1`
**Sort (date)** | `sort date o/ORDER`<br> e.g., `sort date o/desc`
**Sort (priority)** | `sort pr o/ORDER`<br> e.g., `sort pr o/asc`
**Tag Pool** | `tagpool [a/TAG]... [d/TAG]...`<br> e.g., `tagpool a/Shortlisted d/Rejected`
**Tag** | `tag INDEX[,INDEX]... [a/TAG]... [d/TAG]...`<br> e.g., `tag 1,2 a/Shortlisted d/Applied`
**Undo** | `undo`
**Help** | `help`
**Exit** | `exit`
