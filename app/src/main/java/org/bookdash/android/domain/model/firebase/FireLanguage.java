package org.bookdash.android.domain.model.firebase;


public class FireLanguage {
    public static final String TABLE_NAME = "bd_languages";
    private String languageName;
    private String languageAbbreviation;
    private boolean enabled;
    private String id;

    public FireLanguage(String languageName, String languageAbbreviation, boolean enabled) {
        this.languageName = languageName;
        this.languageAbbreviation = languageAbbreviation;
        this.enabled = enabled;
    }

    public FireLanguage() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public String toString() {
        return languageName;
    }

    public String getLanguageAbbreviation() {
        return languageAbbreviation;
    }

    public void setLanguageAbbreviation(String languageAbbreviation) {
        this.languageAbbreviation = languageAbbreviation;
    }

    public String getId() {
        return id;
    }
}
