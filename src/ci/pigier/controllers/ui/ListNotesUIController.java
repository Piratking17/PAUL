package ci.pigier.controllers.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

import ci.pigier.controllers.BaseController;
import ci.pigier.controllers.LanguagesController;
import ci.pigier.model.LanguageOption;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ListNotesUIController extends BaseController implements Initializable {

    // Liste des notes filtrées
    private FilteredList<Note> filteredData;

    // Nom de la colonne triée
    private String sortedColumn = null;

    // Etiquette labellisé (ASC || DESC)
    private Label currentSortIndicator = null;

    @FXML
    private TableColumn<Note, String> descriptionTc;

    @FXML
    private TableColumn<Note, String> titleTc;

    @FXML
    private Label notesCount;

    @FXML
    private TableView<Note> notesListTable;

    @FXML
    private ComboBox<LanguageOption> languageComboBox;

    @FXML
    private TextField searchNotes;

    /**
     * Actualise les données et effectue une recherche.
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void doRefresh(ActionEvent event) throws IOException {
        fetchData();
        search();
        updateSortIndicator(null, true);
    }

    /**
     * Supprime la note sélectionnée.
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void doDelete(ActionEvent event) throws IOException {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedNote)) {
            removeNote(selectedNote.getId());
            fetchData();
            updateNotesCount(data.size());
        } else {
            alertController.handleWarning(bundle.getString("alert.title.noSelection"),
                    bundle.getString("warning.noNoteSelected"), bundle.getString("warning.noNoteSelected.message"));
        }
    }

    /**
     * Modifie la note sélectionnée.
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void doEdit(ActionEvent event) throws IOException {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            editNote = selectedNote;
            navigate(event, FXMLPage.ADD.getPage());
        } else {
            alertController.handleWarning(bundle.getString("alert.title.noSelection"),
                    bundle.getString("warning.noNoteSelected"), bundle.getString("warning.noNoteSelected.message"));
        }
    }

    /**
     * Crée une nouvelle note.
     * 
     * @param event L'événement déclencheur
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    @FXML
    void newNote(ActionEvent event) throws IOException {
        editNote = null;
        navigate(event, FXMLPage.ADD.getPage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation du contrôleur de langues
        LanguagesController languagesController = new LanguagesController();
        languagesController.initialize(languageComboBox);
        fetchData(); // Chargement des données

        // Configuration des colonnes avec PropertyValueFactory
        titleTc.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTc.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Désactiver le tri des colonnes
        titleTc.setSortable(false);
        descriptionTc.setSortable(false);

        addHeaderClickEvent(titleTc, bundle.getString("tableview.header.title"), "title");
        addHeaderClickEvent(descriptionTc, bundle.getString("tableview.header.desc"), "description");

        // Initialisation des données filtrées
        filteredData = new FilteredList<>(data, n -> true);
        notesListTable.setItems(filteredData); // Ajout des données au tableau
        updateNotesCount(filteredData.size()); // Mise à jour du nombre de notes

        // Recherche lorsque la valeur du champ de recherche change
        searchNotes.setOnKeyReleased(e -> search());

        // Changement de langue lorsque la valeur sélectionnée du combobox change
        languageComboBox.setOnAction(event -> {
            LanguageOption selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem();
            prefs.put("lang", selectedLanguage.getLanguageTag()); // Ajout de la langue aux préférences

            alertController.handleInfo(bundle.getString("alert.changedLanguage"),
                    bundle.getString("information.changedLanguage.header"),
                    bundle.getString("information.changedLanguage.content"));

            System.out.println("locale: " + localeManager.getLocale());

            // close the app
            closeApp();

            // reload the app
            // try {
            //     reloadApp();
            // } catch (Exception e1) {
            //     e1.printStackTrace();
            // }
        });
    }

    /**
     * Met à jour l'étiquette du nombre de notes.
     * 
     * @param length Le nombre de notes
     */
    private void updateNotesCount(int length) {
        if (length > 1)
            notesCount.setText(length + " Notes");
        else
            notesCount.setText(length + " Note");
    }

    /**
     * Filtre les notes en fonction de la valeur du champ de recherche.
     */
    private void search() {
        filteredData.setPredicate(n -> {
            if (searchNotes.getText() == null || searchNotes.getText().isEmpty())
                return true;
            return n.getTitle().contains(searchNotes.getText())
                    || n.getDescription().contains(searchNotes.getText());
        });

        updateNotesCount(filteredData.size());
    }

    /**
     * Ajoute un événement de clic à l'en-tête de colonne pour le tri.
     * 
     * @param column     La colonne du tableau
     * @param headerText Le texte de l'en-tête
     * @param columnName Le nom de la colonne
     */
    private void addHeaderClickEvent(TableColumn<Note, String> column, String headerText, String columnName) {
        Label label = new Label(headerText);
        Label sortIndicator = new Label(""); // Indicateur
        HBox header = new HBox(label, sortIndicator);
        header.setAlignment(Pos.CENTER);
        column.setGraphic(header);

        label.setOnMouseClicked(event -> {
            boolean ascending = sortedColumn == null || !sortedColumn.equals(columnName);
            sortColumn(columnName, ascending);
            sortedColumn = ascending ? columnName : null;
            updateSortIndicator(sortIndicator, ascending);

            if (currentSortIndicator != null && currentSortIndicator != sortIndicator) {
                currentSortIndicator.setText("");
            }
            currentSortIndicator = sortIndicator;
        });
    }

    /**
     * Met à jour l'indicateur de tri d'une étiquette donnée en fonction de l'ordre
     * de tri.
     * 
     * @param sortIndicator L'étiquette à mettre à jour avec l'indicateur de tri
     * @param ascending     true si l'ordre de tri est ascendant, false sinon
     */
    private void updateSortIndicator(Label sortIndicator, boolean ascending) {
        if (Objects.isNull(sortIndicator)) {
            if (!Objects.isNull(currentSortIndicator))
                currentSortIndicator.setText("");
            sortedColumn = null;
        } else
            sortIndicator.setText(ascending ? " (ASC)" : " (DESC)");
    }

    /**
     * Trie la colonne dans l'ordre spécifié.
     * 
     * @param column    Le nom de la colonne à trier
     * @param ascending Vrai si l'ordre de tri est ascendant, faux sinon
     */
    private void sortColumn(String column, boolean ascending) {
        if (ascending) {
            if (column.equals("title"))
                Collections.sort(data, Comparator.comparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER));
            else
                Collections.sort(data, Comparator.comparing(Note::getDescription, String.CASE_INSENSITIVE_ORDER));
        } else {
            // Tri par ordre décroissant
            if (column.equals("title"))
                Collections.sort(data, Comparator.comparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
            else
                Collections.sort(data,
                        Comparator.comparing(Note::getDescription, String.CASE_INSENSITIVE_ORDER).reversed());
        }

        notesListTable.refresh();
    }

}
