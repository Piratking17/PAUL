package ci.pigier.model;

import javafx.scene.image.Image;

/**
 * Classe représentant une option de langue avec un nom de langue et une icône.
 */
public class LanguageOption {

    private String language;
    private Image icon = null;

    /**
     * Constructeur de la classe LanguageOption.
     * 
     * @param language Le nom de la langue
     */
    public LanguageOption(String language) {
        this.language = language;
    }

    /**
     * Retourne le tag de langue correspondant au nom de la langue.
     * 
     * @return Le tag de langue (ex: "en" pour Anglais, "fr" pour Français)
     */
    public String getLanguageTag() {
        if (language.equals("Anglais") || language.equals("English")) {
            return "en";
        } else if (language.equals("Français") || language.equals("French")) {
            return "fr";
        }
        return null;
    }

    /**
     * Retourne le nom de la langue.
     * 
     * @return Le nom de la langue
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Définit l'icône de la langue.
     * 
     * @param icon L'icône de la langue
     */
    public void setIcon(Image icon) {
        this.icon = icon;
    }

    /**
     * Retourne l'icône de la langue.
     * 
     * @return L'icône de la langue
     */
    public Image getIcon() {
        return icon;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet LanguageOption.
     * 
     * @return Le nom de la langue
     */
    @Override
    public String toString() {
        return language;
    }
}
