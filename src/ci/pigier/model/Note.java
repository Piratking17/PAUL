package ci.pigier.model;

import javafx.beans.property.SimpleStringProperty;

public class Note {
	private int id;
	private final SimpleStringProperty title;
	private final SimpleStringProperty description;

	public Note(int id, String title, String description) {
		this.id = id;
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}
	
    @Override
    public String toString() {
        return "Note{" + "title=" + title + ", description=" + description + '}';
    }
}
