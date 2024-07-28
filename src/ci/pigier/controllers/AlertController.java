package ci.pigier.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Contrôleur pour gérer les alertes dans l'application.
 * Cette classe utilise le pattern Singleton pour assurer qu'une seule instance
 * existe.
 */
public class AlertController extends BaseController {

    // Instance unique du contrôleur d'alerte
    private static AlertController instance = new AlertController();

    // Instance d'alerte
    static Alert alert;

    // Constructeur privé pour éviter l'instantiation multiple
    private AlertController() {
    }

    /**
     * Obtient l'instance unique du contrôleur d'alerte.
     * 
     * @return L'instance unique du contrôleur d'alerte
     */
    public static AlertController getInstance() {
        return instance;
    }

    /**
     * Affiche une alerte de type avertissement.
     * 
     * @param title   Le titre de l'alerte
     * @param header  Le texte d'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     */
    public void handleWarning(String title, String header, String content) {
        alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de type erreur.
     * 
     * @param title   Le titre de l'alerte
     * @param header  Le texte d'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     */
    public void handleError(String title, String header, String content) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de type information.
     * 
     * @param title   Le titre de l'alerte
     * @param header  Le texte d'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     */
    public void handleInfo(String title, String header, String content) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de type validation.
     * 
     * @param title   Le titre de l'alerte
     * @param header  Le texte d'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     * @return true si le bouton Ok est cliqué, false sinon
     */
    public boolean handleValidation(String title, String header, String content) {
        alert = new Alert(AlertType.CONFIRMATION);
        SimpleBooleanProperty res = new SimpleBooleanProperty(false);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.CANCEL) {
                res.set(false);
            } else {
                res.set(true);
            }
        });

        return res.get();
    }

}
