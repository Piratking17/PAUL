package ci.pigier.model;

import javafx.scene.image.Image;

public class LanguageOption {

    private String language;
    private Image icon = null;

    public LanguageOption(String language) {
        this.language = language;
    }

    public String getLanguageTag() {
        if (language.equals("Anglais") || language.equals("English"))
            return "en";
        else if (language.equals("Français")
                || language.equals("French"))
            return "fr";

        return null;
    }

    public String getLanguage() {
        return language;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Image getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return language;
    }
}
