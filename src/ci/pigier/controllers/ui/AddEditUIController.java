package ci.pigier.controllers.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddEditUIController extends BaseController implements Initializable {

    @FXML
    private TextArea descriptionTxtArea;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField titleTxtFld;

    /**
     * Retourne à la liste des notes.
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void doBack(ActionEvent event) throws IOException {
        navigate(event, FXMLPage.LIST.getPage());
    }

    /**
     * Efface les champs de titre et de description.
     * 
     * @param event L'événement déclencheur
     */
    @FXML
    void doClear(ActionEvent event) {
        descriptionTxtArea.clear();
        titleTxtFld.clear();
    }

    /**
     * Enregistre la note (ajoute ou met à jour).
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void doSave(ActionEvent event) throws IOException {
        // Vérifie si le titre ou la description sont vides
        if (titleTxtFld.getText().trim().equals("") || descriptionTxtArea.getText().trim().equals("")) {
            alertController.handleWarning(bundle.getString("Warning Dialog"), bundle.getString("warning.emptyFields"),
                    null);
            return;
        }

        // Crée une nouvelle note ou met à jour la note existante
        Note newNote = new Note(editNote != null ? editNote.getId() : 0, titleTxtFld.getText(),
                descriptionTxtArea.getText());

        if (editNote != null) {
            // Met à jour la note existante
            if (updateNote(newNote)) {
                navigate(event, FXMLPage.LIST.getPage());
            } else {
                alertController.handleError(bundle.getString("error.update.alertTitle"), bundle.getString("error.update.content"), null);
            }
        } else { // Ajoute une nouvelle note
            // Si une note avec le même titre existe, on ne fait rien
            if (getOne(newNote, "title") != null) {
                Boolean res = alertController.handleValidation("", "", bundle.getString("info.existingNote"));

                if (res) {
                    handleAddNote(event, newNote);
                } else
                    return;
            } else {
                handleAddNote(event, newNote);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Si une note est en cours d'édition, on remplit les champs avec ses données
        if (Objects.nonNull(editNote)) {
            titleTxtFld.setText(editNote.getTitle());
            descriptionTxtArea.setText(editNote.getDescription());
            saveBtn.setText(bundle.getString("button.update"));
        }
    }

    private void handleAddNote(ActionEvent event, Note newNote) throws IOException {
        if (addNote(newNote)) {
            System.out.println("Note added successfully.");
            navigate(event, FXMLPage.LIST.getPage());
        } else {
            System.out.println("Error adding note.");
            alertController.handleError("Error Dialog", "Error adding note.", null);
        }
    }
}
