package ci.pigier;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

public class LocaleManager {

    private static LocaleManager instance = new LocaleManager();
    private ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.getDefault());
    private Preferences prefs = Preferences.userRoot().node("ci.pigier.notetakingapp");
    private Stage stage;

    private LocaleManager() {}

    public static LocaleManager getInstance() {
        return instance;
    }

    public Locale getLocale() {
        return locale.get();
    }

    public Preferences getPref() {
        return prefs;
    }

    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("ci.pigier.i18n.translation", locale.get());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
