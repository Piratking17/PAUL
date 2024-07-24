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
        LocaleManager localeManager = LocaleManager.getInstance();
        String userLang = localeManager.getPref().get("lang", "fr");
        localeManager.setLocale(Locale.forLanguageTag(userLang));
        ResourceBundle bundle = localeManager.getBundle();

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