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
import ci.pigier.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class BaseController {
	private static String url = "jdbc:mysql://localhost:3306/notetakingdb";
	private static String user = "root";
	private static String pass = "root";

    protected static LocaleManager localeManager = LocaleManager.getInstance();
    protected static ResourceBundle bundle = localeManager.getBundle();
    protected Preferences prefs = localeManager.getPref();

	protected Alert alert;
	protected static Note editNote = null;
	protected static ObservableList<Note> data = FXCollections.<Note>observableArrayList();

	protected void navigate(Event event, URL fxmlDocName) throws IOException {
		Parent pageParent = FXMLLoader.load(fxmlDocName, bundle);
		Scene scene = new Scene(pageParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.hide();
		appStage.setScene(scene);
		appStage.show();
	}

	protected int extractNumber(String title) {
		String[] parts = title.split(" ");
		try {
			return Integer.parseInt(parts[title.length() - 1]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	public Connection getConnection() {
		Connection connexion = null;

		try {
			connexion = DriverManager.getConnection(url, user, pass);
			System.out.println("Connexion établie.");
		} catch (SQLException e) {
			System.out.println("Une erreur est survénue lors de la connexion. Contenu: " + e.getMessage());
		}

		return connexion;
	}
	
	public void handleDatabaseConnectionErr() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erreur de connexion");
		alert.setHeaderText("Impossible de se connecter à la base de données");
		alert.setContentText("Veuillez lancer le conteneur mysql sur Docker, tout en spécifiant le mot de passe root comme spécifié dans la classe BaseController");
		alert.showAndWait();
	}

	public int fetchData() {
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
                return data.size();
            } catch (SQLException e) {
                System.out.println("Error fetching data: " + e.getMessage());
            }
        } else handleDatabaseConnectionErr();
        
        return 0;
    }

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
        } else handleDatabaseConnectionErr();

        return false;
    }

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
        } else handleDatabaseConnectionErr();

        return false;
    }

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
        } else handleDatabaseConnectionErr();

        return false;
    }
}
