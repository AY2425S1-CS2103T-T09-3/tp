---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Appointment` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects and `Appointment` objects (which are contained in a `UniquePersonList` and a `UniqueAppointmentList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S1-CS2103T-T09-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Our target user is a receptionist at a small clinic who:
* has a need to manage a significant number of patients and doctors contacts
* has a need to manage appointments for these contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* values tools that allow for rapid data entry and retrieval
* requires robust search capabilities to quickly find patient or doctor information

**Value proposition**: streamlines clinic operations by organizing personnel records and simplifying appointment scheduling


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`



| Priority | As a …​                                  | I want to …​                                                                                     | So that I can…​                                                                                                                 |
|----------|------------------------------------------|--------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| `* * *`  | receptionist                             | add new patient records                                                                          | maintain an up-to-date list of patients                                                                                         |
| `* * *`  | receptionist                             | delete outdated patient records                                                                  | keep the patient database clean and relevant                                                                                    |
| `* * *`  | receptionist                             | search for a patient's record by their name                                                      | quickly retrieve their information                                                                                              |
| `* * *`  | receptionist                             | mark appointments done or undone                                                                 | keep track of the appointments                                                                                                  |
| `* * *`  | receptionist                             | add appointments for both patients and doctors                                                   | keep track of their respective appointments                                                                                     |
| `* * *`  | receptionist                             | delete appointments for both patients and doctors                                                | keep track of their respective appointments                                                                                     |
| `* * *`  | user                                     | add doctor records                                                                               | manage a list of all doctors working in the clinic                                                                              |
| `* * *`  | user                                     | delete doctor records                                                                            | manage a list of all doctors working in the clinic                                                                              |
| `* *`    | receptionist                             | update existing patient details                                                                  | keep up with the latest information                                                                                             |
| `* *`    | receptionist                             | view a summary of all patient records                                                            | prepare for upcoming consultations                                                                                              |
| `* *`    | receptionist                             | view upcoming appointments for each doctor                                                       | manage the clinic's daily schedule effectively                                                                                  |
| `* *`    | receptionist                             | tag certain patients according to their needs                                                    | search for patients based on their care requirements                                                                            |
| `* *`    | receptionist                             | tag certain patients according to their priorities                                               | contact them when necessary                                                                                                     |
| `* *`    | receptionist                             | view which patients need to be called on the current date                                        | so that I easily find out who to I need to contact them                                                                         |
| `* *`    | receptionist                             | add reminders for certain days                                                                   | keep track of all reminders/tasks for the given date                                                                            |
| `* *`    | receptionist                             | update reminders I made                                                                          | keep up with the latest information                                                                                             |
| `* *`    | receptionist                             | delete reminders I made                                                                          | remove entries that have the wrong details.                                                                                     |
| `* *`    | receptionist                             | search for patients based on the tag given to them                                               | easily find and identify patients                                                                                               |
| `*`      | receptionist                             | fetch the history of missed appointments                                                         | contact the patient and inform the doctor                                                                                       |
| `* *`    | receptionist                             | search for patient based on their name/contact number                                            | easily find their contact details and records                                                                                   |
| `* *`    | receptionist                             | generate a list of all upcoming appointments for the current date (or a specified one)           | to assist in daily scheduling (which patient consult which doctor) and preparation                                              |
| `* *`    | receptionist                             | filter patient records by the type of doctor/specialist they are meeting/have already consulted  | manage referrals and specialist appointments effectively                                                                        | 
| `*`      | receptionist                             | set recurring appointments for patients                                                          | streamline the process for those who require regular consultations (instead of them having to repeatedly schedule appointments) |
| `* *`    | receptionist                             | add/link a patient appointment to a specific doctor after checking the doctor's availability     |                                                                                                                                 |
| `* *`    | receptionist                             | change/update a patient's linked doctor if there is a sudden change in the doctor's availability |                                                                                                                                 |
| `* *`    | user                                     | view records in a calendar view                                                                  | get an organised overview of all appointments                                                                                   |
| `*`      | user                                     | sort and view records based on their dates                                                       |                                                                                                                                 |
| `* *`    | user                                     | update the status of doctors (available, on leave, etc)                                          |                                                                                                                                 |
| `* *`    | user                                     | track the availability of doctors and when they are free based on patient records                | to help create appointments to patients                                                                                         |
| `* * *`  | user                                     | list all the doctors stored in the address book.                                                 | see all doctors’ contact details in the address book                                                                            |
| `* * *`  | user                                     | find a specific doctor in the address book                                                       | check if a certain doctor's details are stored in the address book                                                              |
| `* *`    | healthcare provider                      | add notes to patient records                                                                     |                                                                                                                                 |
| `*`      | healthcare provider                      | view a patient's past appointments in this clinic                                                | better understand their medical history and prepare their doctor for consultations                                              |

### Use cases

(For all use cases below, the **System** is the `MediContacts` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add a patient**

**MSS**

1.  User requests to add a patient
2.  MediContacts adds the patient

    Use case ends.

**Extensions**

* 1a. The given patient is a duplicate already in MediContacts.

    * 1a1. MediContacts shows an error message.

      Use case ends.

* 1b. The given name uses the wrong format.

    * 1b1. MediContacts shows an error message.

      Use case ends.

* 1c. The given phone number uses the wrong format.

    * 1c1. MediContacts shows an error message.

      Use case ends.

* 1d. The given email uses the wrong format.

    * 1d1. MediContacts shows an error message.

      Use case ends.

* 1e. The given address uses the wrong format.

    * 1e1. MediContacts shows an error message.

      Use case ends.

* 1f. The given date of birth uses the wrong format.

    * 1f1. MediContacts shows an error message.

      Use case ends.

* 1g. The given gender uses the wrong format.

    * 1g1. MediContacts shows an error message.

      Use case ends.

* 1h. The given tag(s) uses the wrong format.

    * 1h1. MediContacts shows an error message.

      Use case ends.

**Use case: UC02 - Add a doctor**

**MSS**

1.  User requests to add a doctor
2.  MediContacts adds the doctor

    Use case ends.

**Extensions**

* 1a. The given doctor is a duplicate already in the MediContacts.

    * 1a1. MediContacts shows an error message.

      Use case ends.

* 1b. The given name uses the wrong format.

    * 1b1. MediContacts shows an error message.

      Use case ends.

* 1c. The given phone number uses the wrong format.

    * 1c1. MediContacts shows an error message.

      Use case ends.

* 1d. The given email uses the wrong format.

    * 1d1. MediContacts shows an error message.

      Use case ends.

* 1e. The given address uses the wrong format.

    * 1e1. MediContacts shows an error message.

      Use case ends.

* 1f. The given speciality uses the wrong format.

    * 1f1. MediContacts shows an error message.

      Use case ends.

* 1g. The given tag(s) uses the wrong format.

    * 1g1. MediContacts shows an error message.
    
        Use case ends.

<a name="list-patient-anchor-point"></a>
**Use case: UC03 - List all patients**

**MSS**

1.  User requests to list patients
2.  MediContacts shows a list of all patients previously added

    Use case ends.

<a name="list-doctor-anchor-point"></a>
**Use case: UC04 - List all doctors**

**MSS**

1.  User requests to list doctors
2.  MediContacts shows a list of all doctors previously added

    Use case ends.

<a name="list-patient-anchor-point"></a>
**Use case: UC05 - List all doctors and patients**

**MSS**

1.  User requests to list all doctors and patients
2.  MediContacts shows a list of all doctors and patients previously added

    Use case ends.

<a name="find-patient-anchor-point"></a>
**Use case: UC06 - Find a patient**

**MSS**

1.  User requests to find a specific patient
2.  MediContacts shows a list of all patients with matching names

    Use case ends.

**Extensions**

* 1a. The given name uses the wrong format.

    * 1a1. MediContacts shows an error message.

      Use case ends.

<a name="find-doctor-anchor-point"></a>
**Use case: UC07 - Find a doctor**

**MSS**

1.  User requests to find a specific doctor
2.  MediContacts shows a list of all doctors with matching names

    Use case ends.

**Extensions**

* 1a. The given name uses the wrong format.

    * 1a1. MediContacts shows an error message.

      Use case ends.

**Use case: UC08 - Delete a patient**

**MSS**

1.  User requests to either [list patients (UC03)](#list-patient-anchor-point) or [find a patient (UC06)](#find-patient-anchor-point)
2.  MediContacts shows a list of patients
3.  User requests to delete a specific patient in the list
4.  MediContacts deletes the patient

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. MediContacts shows an error message.

      Use case resumes at step 2.

**Use case: UC09 - Delete a doctor**

**MSS**

1.  User requests to either [list doctors (UC04)](#list-doctor-anchor-point) or [find a doctor (UC07)](#find-doctor-anchor-point)
2.  MediContacts shows a list of doctors
3.  User requests to delete a specific doctor in the list
4.  MediContacts deletes the doctor

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. MediContacts shows an error message.

      Use case resumes at step 2.

**Use case: UC10 - Add an appointment**

**MSS**

1.  User requests to add an appointment
2.  MediContacts adds the appointment

    Use case ends.

**Extensions**

* 1a. The given appointment is a duplicate already in the MediContacts.

    * 1a1. MediContacts shows an error message.

      Use case ends.

* 1b. The given appointment clashes with an existing appointment with the given doctor or patient.

    * 1b1. MediContacts shows an error message.

      Use case ends.

* 1b. The given patient name uses the wrong format.

    * 1b1. MediContacts shows an error message.

      Use case ends.

* 1c. The given doctor name uses the wrong format.

    * 1c1. MediContacts shows an error message.

      Use case ends.

* 1d. The given date uses the wrong format.

    * 1d1. MediContacts shows an error message.

      Use case ends.

* 1e. The given time uses the wrong format.

    * 1e1. MediContacts shows an error message.

      Use case ends.

**Use case: UC11 - Delete an appointment**

**MSS**

1. User requests to delete an appointment with the given unique ID.
2. MediContacts deletes the appointment.

    Use case ends.

**Extensions**

* 1a. The given unique ID is invalid.

  * 3a1. MediContacts shows an error message.

   Use case ends.

*  1b. The appointment with the given unique ID does not exist.

  * 3b1. MediContacts shows an error message.

   Use case ends.

*{More to be added as more features get added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. The system should be designed to allow the addition of new features, such as supporting other user types (e.g., nurses, staff) or integrating external systems, with minimal changes to the core codebase.
3. The system must securely store patient and doctor information to comply with healthcare data privacy regulations, such as HIPAA.
4. The system should log all user actions, such as adding, deleting, or modifying records. Logs should be stored for a minimum of 6 months and be accessible to authorized administrators for auditing purposes.
5. The system should provide a response time of less than 2 seconds for any user interaction under normal load (i.e., up to 1000 patients and 500 doctors).

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Healthcare Data Privacy Regulations**: Laws and standards that govern the storage and access to patient and doctor data. Examples include HIPAA.
* **HIPAA**: The Health Insurance Portability and Accountability Act, a regulation in the U.S. that mandates secure handling of personal health information.
* **Audit**: A record of all changes made in the system, including who made the changes and when.

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Planned Enhancements**
Team Size: 5
1. **Update `add-patient` and `add-doctor` commands to check the date of birth and phone number input for logical inputs**
   1. Currently, both commands do not check whether the given data is a valid date of birth.
   2. We plan to make the commands check whether the data of birth given is not just in the correct format given, but also to check that it is:
      1. In the past (dates that are in the future from current date are illogical)
      2. Not too far in the past (e.g. 200 years ago from current date are illogical)
   3. We also plan to make the commands check whether the phone number given is not just in the correct format given, but also to check that it is:
      1. A valid phone number (e.g. 7 - 15 digits, handling the shortest and longest phone numbers internationally)

2. **Make 'successfully added patient/doctor' message more detailed**
   1. Both commands success message do not show all the details of the patient/doctor added. Specifically:
      1. The `add-patient` command does not show the patient's date of birth and gender
      2. The `add-doctor` command does not show the doctor's speciality
   2. We plan to make the success message show all the details of the patient/doctor added, including those above.
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. Deleting a person while a filtered list is being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list. Filter the list using the `find` command.

   1. Test case: `delete 1`<br>
      Expected: First contact in the filtered list is deleted. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

3. Deleting a person which has appointments linked to it

   1. Prerequisites: Add an appointment for the person that is first in the list using the `add-appt` command.

   1. Test case: `delete 1`<br>
      Expected: Error message indicating the person has appointments linked to it. No person is deleted.
   
### Adding an appointment

1. Adding an appointment for a doctor and patient

    1. Prerequisites: Ensure at least one doctor and one patient are added to the system. The patient or doctor to be deleted should not have any appointments.

    1. Test case: `add-appt pn/John Doe dn/Jane Doe d/23-04-1987 t/1100`  
       Expected: Appointment is successfully added for the doctor and patient. Confirmation message shows details of the appointment.

    1. Test case: `add-appt pn/John Doe dn/Jane Doe d/23-04-1987 t/1100` (Duplicate appointment)  
       Expected: Error message indicating the appointment already exists at the same date and time for the doctor and patient.

2. Adding an appointment with an invalid date or time 
   1. Test case: `add-appt pn/John Doe dn/Jane Doe d/23-04-1987 t/`  
      Expected: Error message indicating command format is wrong. No appointment is added.

### Deleting an appointment

1. Deleting an appointment that exists

    1. Prerequisites: Add an appointment for a doctor and patient using the `add-appt` command or appointment already exists.

    1. Test case: `delete-appt UNIQUE_ID`  
       Expected: Appointment is successfully deleted. Confirmation message shows details of the deleted appointment.

    1. Test case: `delete-appt UNIQUE_ID` (Appointment already deleted)  
       Expected: Error message indicating the appointment does not exist.

1. Deleting an appointment using an invalid id.

   1. Test case: `delete-appt UNIQUE_ID` (`UNIQUE_ID` is not a positive integer)  
   Expected: Error message indicating an invalid id. No appointment is deleted.


### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
