package ci.pigier.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import ci.pigier.LocaleManager;
import ci.pigier.NoteTakingApp;
import ci.pigier.model.Note;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Contrôleur de base fournissant des fonctionnalités communes pour les autres contrôleurs.
 */
public class BaseController {

    private static String url = "jdbc:mysql://localhost:3306/notetakingdb";
    private static String user = "root";
    private static String pass = "root";

    protected static LocaleManager localeManager = LocaleManager.getInstance();
    protected static ResourceBundle bundle = localeManager.getBundle();
    protected Preferences prefs = localeManager.getPref();

    protected AlertController alertController = AlertController.getInstance();
    protected static Note editNote = null;
    protected static ObservableList<Note> data = FXCollections.<Note>observableArrayList();

    /**
     * Navigue vers une autre page.
     * 
     * @param event L'événement déclencheur
     * @param fxmlDocName Le fichier FXML de la page cible
     * @throws IOException Si une exception d'entrée ou sortie se produit
     */
    protected void navigate(Event event, URL fxmlDocName) throws IOException {
        Parent pageParent = FXMLLoader.load(fxmlDocName, bundle);
        Scene scene = new Scene(pageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     * Recharge l'application.
     * 
     * @throws Exception Si une exception se produit lors du rechargement
     */
    public void reloadApp() throws Exception {
        System.out.println( "Restarting app!" );
        localeManager.getStage().close();
        Platform.runLater( () -> {
            try {
                new NoteTakingApp().start( new Stage() );
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        } );
    }

    /**
     * Ferme l'application.
     */
    public void closeApp() {
        Platform.exit();
    }

    /**
     * Obtient une connexion à la base de données.
     * 
     * @return La connexion à la base de données
     */
    public Connection getConnection() {
        Connection connexion = null;

        try {
            connexion = DriverManager.getConnection(url, user, pass);
            System.out.println("Connexion établie.");
        } catch (SQLException e) {
            System.out.println("Une erreur est survenue lors de la connexion. Contenu: " + e.getMessage());
        }

        return connexion;
    }

    /**
     * Récupère les données des notes depuis la base de données.
     */
    public ObservableList<Note> fetchData() {
        data.clear();
        Connection connection = getConnection();
        if (connection != null) {
            String query = "SELECT * FROM notes";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    Note note = new Note(id, title, description);
                    data.add(note);
                }
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error fetching data: " + e.getMessage());
            }
        }
        
        return data;
    }

    /**
     * Récupère une note spécifique de la base de données.
     * 
     * @param note La note à récupérer
     * @param by La colonne par laquelle effectuer la recherche (par défaut "id")
     * @return La note trouvée, ou null si aucune note n'est trouvée
     */
    public Note getOne(Note note, String by) {
        Connection connection = null;
        String columnString = "id";

        if (by != null) columnString = by;

        try {
            connection = getConnection();
            if (connection != null) {
                String query = "SELECT * FROM notes WHERE " + columnString + " = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    switch (columnString) {
                        case "title":
                            pstmt.setString(1, note.getTitle());
                            break;
                        case "description":
                            pstmt.setString(1, note.getDescription());
                            break;
                        default:
                            pstmt.setInt(1, note.getId());
                            break;
                    }

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            return new Note(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("description")
                            );
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Erreur: " + e.getMessage());
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Erreur: " + e.getMessage());
                }
            }
        }

        return null;
    }


    /**
     * Ajoute une nouvelle note à la base de données.
     * 
     * @param note La note à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean addNote(Note note) {
        Connection connection = getConnection();
        if (connection != null) {
            String query = "INSERT INTO notes (title, description) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, note.getTitle());
                pstmt.setString(2, note.getDescription());
                int affectedRows = pstmt.executeUpdate();

                connection.close();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Error creating data: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * Met à jour une note existante dans la base de données.
     * 
     * @param note La note à mettre à jour
     * @return true si la mise à jour est réussie, false sinon
     */
    public boolean updateNote(Note note) {
        Connection connection = getConnection();
        if (connection != null) {
            String query = "UPDATE notes SET title=?, description=? WHERE id=?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, note.getTitle());
                pstmt.setString(2, note.getDescription());
                pstmt.setInt(3, note.getId());
                int affectedRows = pstmt.executeUpdate();

                connection.close();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Error updating data: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * Supprime une note de la base de données.
     * 
     * @param id L'identifiant de la note à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    public boolean removeNote(int id) {
        Connection connection = getConnection();
        if (connection != null) {
            String query = "DELETE FROM notes WHERE id=?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();

                connection.close();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Error removing data: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
