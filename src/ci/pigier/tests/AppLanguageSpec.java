package ci.pigier.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ci.pigier.LocaleManager;

public class AppLanguageSpec {

	@Test
	public void changeAppLanguage() {
		LocaleManager locale = LocaleManager.getInstance();
		locale.setLocale(Locale.forLanguageTag("fr"));
		String old = locale.getLocale().toLanguageTag();

		locale.setLocale(Locale.forLanguageTag("en"));
		String current = locale.getLocale().toLanguageTag();
		
		assertTrue(!old.equals(current), "Les langues devraient être différentes.");
		assertTrue(current.equals("en"), "La langue actuelle devrait être l'anglais (en)");
	}
}
