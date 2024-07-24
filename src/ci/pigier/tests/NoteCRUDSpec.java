package ci.pigier.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;

public class NoteCRUDSpec {
	BaseController controller = new BaseController();
	
	@BeforeEach
	void init() throws Exception {
		controller.getConnection();
	}

	@Test
	public void addNewNote() throws Exception {
		int oldLen = controller.fetchData();
		Note newNote = new Note(0, "Test title", "Test description");
		controller.addNote(newNote);
		int currentLen = controller.fetchData();

		assertTrue(oldLen < currentLen && currentLen == oldLen + 1, () -> "Note added.");
    }
}
