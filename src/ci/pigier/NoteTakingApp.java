package ci.pigier;


import java.util.Locale;
import java.util.ResourceBundle;

import ci.pigier.ui.FXMLPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NoteTakingApp extends Application {

	@Override
    public void start(Stage stage) throws Exception {
        // Récupération de l'instance de LocaleManager
        LocaleManager localeManager = LocaleManager.getInstance();

        // Stockage du Stage pour la réutilisation lors du rechargement de l'application
        localeManager.setStage(stage);

        // Récupération de la langue sélectionnée par l'utilisateur, "fr" (français) par
        // défaut
        String userLang = localeManager.getPref().get("lang", "fr");

        // Définition de la locale en fonction de la langue sélectionnée
        localeManager.setLocale(Locale.forLanguageTag(userLang));

        // Récupération du bundle de ressources pour la locale courante, pour charger les
        // chaînes de caractères spécifiques à la langue sélectionnée
        ResourceBundle bundle = localeManager.getBundle();

        System.out.println(userLang);
        Parent root = FXMLLoader.load(FXMLPage.LIST.getPage(), bundle);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle(bundle.getString("app.title"));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


}