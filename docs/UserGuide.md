---
layout: page
title: User Guide
---

Talently is a **desktop app for managing job candidates, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Talently can get your candidate management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-T17-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your Talently app.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar talently.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all candidates.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a candidate named `John Doe`.

   * `remove 3` : Removes the 3rd candidate shown in the current list.

   * `clear` : Deletes all candidates.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [pr/PRIORITY]` can be used as `n/John Doe pr/yes` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[a/TAG]…​` can be used as ` ` (i.e. 0 times), `a/Shortlisted`, `a/Shortlisted a/Interviewed` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored or rejected.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a candidate : `add`

Adds a candidate to Talently.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]`

* `NAME` must contain only letters, spaces, hyphens (`-`), apostrophes (`'`), periods (`.`), and slashes (`/`). No digits allowed. Maximum 100 characters.
* `PHONE` must contain only digits with an optional `+` prefix (E.164 format). Must be between 3 and 15 digits long. e.g. `91234567` or `+6591234567`.
* `EMAIL` must be a valid email address in the format `example@domain.com`. Maximum 254 characters.
* `ADDRESS` is required.
* `PRIORITY` is optional. Use `yes` to flag a candidate as high priority, or `no` (default) for normal priority.

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy O'Brien e/betsy@example.com a/Newgate Prison p/+6591234567 pr/yes`

### Listing all candidates : `list`

Shows a list of all candidates in Talently, sorted alphabetically by name.

Format: `list`

* The list command does not accept any additional parameters.
* Displays the total number of candidates found.
* If no candidates exist, a message will prompt you to add one.

### Editing a candidate : `edit`

Edits an existing candidate in Talently.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`

* Edits the candidate at the specified `INDEX`. The index refers to the index number shown in the displayed candidate list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* `PRIORITY` accepts `yes` or `no`.

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` — Edits the phone number and email of the 1st candidate.
* `edit 2 n/Betsy Crower pr/yes` — Edits the name and sets priority to high for the 2nd candidate.

### Locating candidates : `find`

Finds candidates whose name, phone, or email contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Name, phone number, and email are searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Candidates matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`
* A maximum of 20 keywords is allowed per search.
* The entire command must not exceed 150 characters.
* Keywords may only contain letters, digits, and the following symbols: `-` `'` `.` `/` `@` `+` `_`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Filtering candidates by exact tag: `filter`

Finds candidates whose tags exactly match the given tag.

Format: `filter TAG`

* Tag matching is exact (not substring-based).
* Tag matching is case-insensitive.
* The tag must follow tag naming rules (alphanumeric, no spaces, 1 to 30 characters).

Examples:
* `filter friends` returns all candidates tagged `friends`.
* `filter Java` returns candidates tagged `Java`, but not candidates tagged `JavaScript`.

### Removing a candidate : `remove`

Removes the specified candidate from Talently.

Format: `remove INDEX`

* Removes the candidate at the specified `INDEX`.
* The index refers to the index number shown in the displayed candidate list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `remove 2` removes the 2nd candidate in Talently.
* `find Betsy` followed by `remove 1` removes the 1st candidate in the results of the `find` command.

### Rejecting a candidate : `reject`

Marks a candidate as rejected and records a rejection reason.

Format: `reject INDEX r/REASON`

* Rejects the candidate at the specified `INDEX`.
* The index refers to the index number shown in the displayed candidate list. The index **must be a positive integer** 1, 2, 3, …​
* `REASON` must be non-empty, at most 200 characters, and may contain letters, digits, spaces, and the following punctuation: `.` `,` `-` `'` `/`.
* Each call to `reject` appends a new rejection reason to the candidate's rejection history.
* The candidate card will display the total number of times rejected and the full list of past rejection reasons.
* If the same reason is given consecutively, a note will be shown.
* Cannot reject an archived candidate.

Examples:
* `reject 1 r/Failed technical interview`
* `reject 3 r/Insufficient experience`

### Sorting candidates by date : `sort`

Sorts all candidates by the date they were added to Talently, from newest to oldest.

Format: `sort date o/desc`

* Candidates are sorted with the most recently added appearing first.
* When two candidates share the same date, they are sorted alphabetically by name.

Examples:
* `sort date o/desc` — Sorts candidates from newest to oldest.

### Managing the tag pool : `tagpool`

Creates or deletes tags in the master tag registry. Tags must exist in the pool before they can be assigned to candidates.

Format: `tagpool [a/TAG_TO_CREATE]... [d/TAG_TO_DELETE]...`

* At least one `a/` or `d/` must be provided.
* A maximum of 10 tags can be processed per command.
* Deleting a tag from the pool also removes it from all candidates currently holding that tag.
* Cannot create a tag that already exists, or delete a tag that does not exist.
* Cannot create and delete the same tag in one command.

Examples:
* `tagpool a/Shortlisted a/Interviewed` — Creates two new tags.
* `tagpool d/Shortlisted` — Deletes the tag "Shortlisted" from the pool and all candidates.
* `tagpool a/Senior d/Junior` — Creates "Senior" and deletes "Junior" in one command.

### Tagging a candidate : `tag`

Adds or removes tags on a specific candidate.

Format: `tag INDEX[,INDEX]... [a/TAG_TO_ADD]... [d/TAG_TO_DELETE]...`

* Tags the candidate(s) at the specified `INDEX` or comma-separated list of indices.
* Each index refers to the index number shown in the displayed candidate list. Each index **must be a positive integer** 1, 2, 3, …​ Duplicate indices are not allowed.
* At least one `a/` or `d/` must be provided.
* A maximum of 10 tags can be processed per command.
* Tags must already exist in the tag pool (use [`tagpool`](#managing-the-tag-pool--tagpool) to create them first).
* Cannot add a tag the candidate already has, or remove a tag the candidate does not have.
* Cannot add and delete the same tag in one command.

Examples:
* `tag 1 a/Shortlisted` — Adds the "Shortlisted" tag to the 1st candidate.
* `tag 2 d/Interviewed` — Removes the "Interviewed" tag from the 2nd candidate.
* `tag 3 a/Senior d/Junior` — Adds "Senior" and removes "Junior" from the 3rd candidate.
* `tag 1,2,3 a/Shortlisted` — Adds the "Shortlisted" tag to candidates 1, 2, and 3.

### Clearing all entries : `clear`

Clears all candidates from Talently.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Talently data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Talently data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, Talently will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause Talently to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Talently home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665`
**Clear** | `clear`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Filter** | `filter TAG`<br> e.g., `filter friends`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Remove** | `remove INDEX`<br> e.g., `remove 3`
**Reject** | `reject INDEX r/REASON`<br> e.g., `reject 1 r/Failed technical interview`
**Sort** | `sort date o/desc`<br> e.g., `sort date o/desc`
**Tag** | `tag INDEX[,INDEX]... [a/TAG]... [d/TAG]...`<br> e.g., `tag 1 a/Shortlisted d/Applied`
**Tag Pool** | `tagpool [a/TAG]... [d/TAG]...`<br> e.g., `tagpool a/Shortlisted d/Rejected`
**Help** | `help`
