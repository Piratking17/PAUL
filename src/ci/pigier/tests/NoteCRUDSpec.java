package ci.pigier.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;

public class NoteCRUDSpec {

    private BaseController controller;
    private Note testNote;

    @BeforeEach
    void init() throws Exception {
        // Initialisation du contrôleur et création d'une note de test
        controller = new BaseController();
        controller.getConnection();
        testNote = new Note(0, "Test title", "Test description");
    }

    @AfterEach
    void cleanUp() {
        // Nettoyage en supprimant la note de test si elle existe
        Note fetchedNote = controller.getOne(testNote, "title");
        if (fetchedNote != null) {
            controller.removeNote(fetchedNote.getId());
        }
    }

    @Test
    public void addNewNote() throws Exception {
        // Ajout d'une nouvelle note et vérification de l'augmentation du nombre total de notes
        int oldLen = controller.fetchData().size();
        controller.addNote(testNote);
        int currentLen = controller.fetchData().size();

        assertTrue(oldLen < currentLen, "La note devrait être ajoutée.");
    }

    @Test
    public void getOneNote() throws Exception {
        // Ajout d'une nouvelle note, récupération et vérification que la note récupérée correspond à l'originale
        controller.addNote(testNote);
        Note fetchedNote = controller.getOne(testNote, "title");

        assertNotNull(fetchedNote, "La note devrait être récupérée.");
        assertEquals(testNote.getTitle(), fetchedNote.getTitle(), "Le titre de la note récupérée devrait correspondre.");
        assertEquals(testNote.getDescription(), fetchedNote.getDescription(), "La description de la note récupérée devrait correspondre.");
    }

    @Test
    public void updateExistingNote() throws Exception {
        // Ajout d'une nouvelle note, mise à jour de son titre et de sa description, et vérification que les valeurs mises à jour correspondent
        controller.addNote(testNote);
        Note fetchedNote = controller.getOne(testNote, "title");
        assertNotNull(fetchedNote, "La note devrait être récupérée.");

        fetchedNote.setTitle("Updated title");
        fetchedNote.setDescription("Updated description");
        boolean updated = controller.updateNote(fetchedNote);
        Note updatedNote = controller.getOne(fetchedNote, "title");

        assertTrue(updated, "La note devrait être mise à jour.");
        assertNotNull(updatedNote, "La note mise à jour devrait être récupérée.");
        assertEquals("Updated title", updatedNote.getTitle(), "Le titre de la note mise à jour devrait correspondre.");
        assertEquals("Updated description", updatedNote.getDescription(), "La description de la note mise à jour devrait correspondre.");
    }

    @Test
    public void removeNote() throws Exception {
        // Ajout d'une nouvelle note, suppression et vérification que la note ne peut plus être récupérée
        controller.addNote(testNote);
        Note fetchedNote = controller.getOne(testNote, "title");
        assertNotNull(fetchedNote, "La note devrait être récupérée.");

        boolean removed = controller.removeNote(fetchedNote.getId());
        Note removedNote = controller.getOne(fetchedNote, "title");

        assertTrue(removed, "La note devrait être supprimée.");
        assertEquals(null, removedNote, "La note supprimée ne devrait pas être trouvée.");
    }
}
