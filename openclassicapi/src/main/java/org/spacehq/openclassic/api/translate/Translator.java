package org.spacehq.openclassic.api.translate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * Translates string keys into text from the current selected language.
 */
public class Translator {

	private final Map<String, Language> languages = new HashMap<String, Language>();
	private String def;
	
	/**
	 * Gets the default language for this translator.
	 * @return The translator's default language.
	 */
	public String getDefault() {
		return this.def;
	}
	
	/**
	 * Sets the default language for this translator.
	 * @param lang The translator's new default language.
	 */
	public void setDefault(String lang) {
		this.def = lang;
	}
	
	/**
	 * Registers a language, adding translations to an existing
	 * language instance if one already is registered.
	 * @param lang The language to register.
	 */
	public void register(Language lang) {
		Validate.notNull(lang, "Language cannot be null.");
		if(this.languages.containsKey(lang.getLangCode())) {
			for(String key : lang.getTranslations().getAbsoluteKeys(false)) {
				this.get(lang.getLangCode()).getTranslations().setValue(key, lang.getTranslations().getValue(key));
			}
		} else {
			this.languages.put(lang.getLangCode(), lang);
		}
	}
	
	/**
	 * Gets the registered language with the given name.
	 * @param lang Language to get.
	 * @return The language with the given name.
	 */
	public Language get(String lang) {
		return this.languages.get(lang);
	}
	
	/**
	 * Gets a collection of all languages registered to this translator.
	 * @return All languages registered to this translator.
	 */
	public Collection<Language> getLanguages() {
		return this.languages.values();
	}

	/**
	 * Gets a collection of the names of all languages registered to this translator.
	 * @return The names of all languages registered to this translator.
	 */
	public Collection<String> getLanguageNames() {
		return this.languages.keySet();
	}
	
	/**
	 * Translates the given key into the language specified by the game's language setting.
	 * @param text Text to translate
	 * @return The translated text.
	 */
	public String translate(String text) { 
		return this.translate(text, OpenClassic.getGame().getLanguage());
	}
	
	/**
	 * Translates the given key into the given language.
	 * @param text Text to translate.
	 * @param lang Language to translate to.
	 * @return The translated text.
	 */
	public String translate(String text, String lang) {
		if(this.get(lang) == null) {
			if(this.get(this.def) != null) {
				return this.get(this.def).translate(text);
			} else {
				return text;
			}
		} else {
			String result = this.get(lang).translate(text);
			if(result.equals(text)) {
				result = this.get(this.def).translate(text);
			}
			
			return result;
		}
	}
	
}
