package ci.pigier.controllers.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import ci.pigier.controllers.BaseController;
import ci.pigier.controllers.LanguagesController;
import ci.pigier.model.LanguageOption;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class ListNotesUIController extends BaseController implements Initializable {
    FilteredList<Note> filteredData;
    SortedList<Note> sortedData;

    @FXML
    private TableColumn<?, ?> descriptionTc;

    @FXML
    private Label notesCount;

    @FXML
    private TableView<Note> notesListTable;

    @FXML
    private ComboBox<LanguageOption> languageComboBox;

    @FXML
    private TextField searchNotes;

    @FXML
    private TableColumn<?, ?> titleTc;

    @FXML
    void doDelete(ActionEvent event) throws IOException {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedNote)) {
            removeNote(selectedNote.getId());
            navigate(event, FXMLPage.LIST.getPage());
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("alert.title.noSelection"));
            alert.setHeaderText(bundle.getString("warning.noNoteSelected"));
            alert.setContentText(bundle.getString("warning.noNoteSelected.message"));
            alert.showAndWait();
        }
    }

    @FXML
    void doEdit(ActionEvent event) throws IOException {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            editNote = selectedNote;
            navigate(event, FXMLPage.ADD.getPage());
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("alert.title.noSelection"));
            alert.setHeaderText(bundle.getString("warning.noNoteSelected"));
            alert.setContentText(bundle.getString("warning.noNoteSelected.message"));
            alert.showAndWait();
        }
    }

    @FXML
    void newNote(ActionEvent event) throws IOException {
        editNote = null;
        navigate(event, FXMLPage.ADD.getPage());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        LanguagesController languagesController = new LanguagesController();
        languagesController.initialize(languageComboBox);

        fetchData();

        filteredData = new FilteredList<>(data, n -> true);
        sortedData = new SortedList<>(filteredData);

        sortedData.setComparator((x, y) -> {
            int number1 = extractNumber(x.getTitle());
            int number2 = extractNumber(y.getTitle());
            return Integer.compare(number1, number2);
        });
        notesListTable.setItems(sortedData);

        updateNotesCount(filteredData.size());

        titleTc.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTc.setCellValueFactory(new PropertyValueFactory<>("description"));

        searchNotes.setOnKeyReleased(e -> {
            filteredData.setPredicate(n -> {
                if (searchNotes.getText() == null || searchNotes.getText().isEmpty())
                    return true;
                return n.getTitle().contains(searchNotes.getText())
                        || n.getDescription().contains(searchNotes.getText());
            });

            updateNotesCount(filteredData.size());
        });

        languageComboBox.setOnAction(event -> {
            LanguageOption selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem();
            prefs.put("lang", selectedLanguage.getLanguageTag());

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("alert.changedLanguage"));
            alert.setHeaderText(bundle.getString("information.changedLanguage.header"));
            alert.setContentText(bundle.getString("information.changedLanguage.content"));
            alert.showAndWait();

            System.out.println("locale: " + localeManager.getLocale());
        });
    }

    private void updateNotesCount(int length) {
        if (length > 1)
            notesCount.setText(length + " Notes");
        else
            notesCount.setText(length + " Note");
    }
}
