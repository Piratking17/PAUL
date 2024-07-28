# Note-Taking Application

## Description

This JavaFX application allows users to manage their notes. Users can add, edit, delete, and search for notes. Additionally, users can sort notes by clicking on the table column headers.

## Features

- Add new notes
- Edit existing notes
- Delete notes
- Search notes
- Sort notes by title or description
- Change application language

## Technologies Used

- Java 11+
- JavaFX
- FXML

## Setup and Installation

### Prerequisites

- Java 11 or later
- Docker

### Clone the Repository

```bash
git clone https://github.com/your-username/note-taking-app.git
cd note-taking-app
docker-compose up --build
```

## Usage

- Add a New Note: Click on the "New Note" button and fill in the title and description fields, then click "Save".
- Edit a Note: Select a note from the table and click on the "Edit" button. Modify the fields as needed and click "Save".
- Delete a Note: Select a note from the table and click on the "Delete" button.
- Search for Notes: Enter a keyword in the search field to filter notes by title or description.
- Sort Notes: Click on the column headers "Title" or "Description" to sort notes in ascending or descending order.
- Change Language: Select a language from the dropdown menu to change the application's language.

## Structure

note-taking-app
├── src/
│   ├── ci/pigier/controllers
│   │   ├── BaseController.java
├── ├── ├── AlertController.java
│   │   ├── LanguagesController.java
│   │   └── ui
│   │       ├── AddEditUIController.java
│   │       └── ListNotesUIController.java
│   ├── ci/pigier/model
│   │   ├── LanguageOption.java
│   │   └── Note.java
│   ├── ci/pigier/i18n
│   │   ├── translation_en.properties
│   │   └── translation_fr.properties
│   ├── ci/pigier/ui
│   │   ├── fxml
│   │   │   ├── assets
│   │   │   │   └── logo
│   │   │   ├── AddEditUI.fxml
│   │   │   └── ListNotesUI.fxml
│   │   └── FXMLPage.java
│   ├── ci/pigier/tests
│   │   └── NoteCRUDSpec.java
│   └── ci/pigier
│       ├── NoteTakingApp.java
│       └── LocaleManager.java
├── src/ci
│   └── module-info
├── init.sql
└── docker-compose.yml

# Contributors

Yann ADJUMANY
