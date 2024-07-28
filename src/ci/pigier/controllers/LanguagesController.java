package ci.pigier.controllers;

import java.util.ResourceBundle;

import ci.pigier.LocaleManager;
import ci.pigier.model.LanguageOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * Contrôleur pour la gestion des langues dans l'application.
 */
public class LanguagesController {

    static LocaleManager localeManager = LocaleManager.getInstance();
    private static ResourceBundle bundle = localeManager.getBundle();
    
    public LanguagesController() {}

    /**
     * Initialise le combobox avec les options de langue disponibles.
     * 
     * @param languageComboBox Le combobox à initialiser
     */
    public void initialize(ComboBox<LanguageOption> languageComboBox) {
        ObservableList<LanguageOption> languageOptions = FXCollections.observableArrayList(
            new LanguageOption(bundle.getString("language.french")),
            new LanguageOption(bundle.getString("language.english"))
        );

        languageComboBox.setItems(languageOptions);

        languageComboBox.setCellFactory(comboBox -> new ListCell<LanguageOption>() {
            // Pour les mises à jour futures : Ajouter une icône de langue
            // private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getLanguage());
                }
            }
        });

        // Sélection par défaut
        String userLang = localeManager.getPref().get("lang", "fr");
        if (userLang.equals("fr")) {
            languageComboBox.getSelectionModel().selectFirst();
        } else {
            languageComboBox.getSelectionModel().select(1);
        }
    }
}
