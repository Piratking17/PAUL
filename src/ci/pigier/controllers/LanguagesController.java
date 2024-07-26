package ci.pigier.controllers;

import java.util.ResourceBundle;

import ci.pigier.LocaleManager;
import ci.pigier.model.LanguageOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
// import javafx.scene.image.ImageView;

public class LanguagesController {
    static LocaleManager localeManager = LocaleManager.getInstance();
    private static ResourceBundle bundle = localeManager.getBundle();
    
    public LanguagesController() {}

    public void initialize(ComboBox<LanguageOption> languageComboBox) {
        ObservableList<LanguageOption> languageOptions = FXCollections.observableArrayList(
            new LanguageOption(bundle.getString("language.french")),
            new LanguageOption(bundle.getString("language.english"))
        );

        languageComboBox.setItems(languageOptions);

        languageComboBox.setCellFactory(comboBox -> new ListCell<LanguageOption>() {
            // For upcoming updates: Add language icon
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

        // Default selection
        String userLang = localeManager.getPref().get("lang","fr");
        if (userLang.equals("fr")) languageComboBox.getSelectionModel().selectFirst();
        else languageComboBox.getSelectionModel().select(1);
    }
}
