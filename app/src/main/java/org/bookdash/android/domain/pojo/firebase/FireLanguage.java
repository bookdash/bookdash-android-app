package org.bookdash.android.domain.pojo.firebase;


public class FireLanguage {
    public String languageName;
    public String languageAbbreviation;
    public boolean enabled;

    public FireLanguage(String languageName, String languageAbbreviation, boolean enabled) {
        this.languageName = languageName;
        this.languageAbbreviation = languageAbbreviation;
        this.enabled = enabled;
    }

    public FireLanguage() {
    }

    public String getLanguageName() {
        return languageName;
    }

    @Override
    public String toString() {
        return languageName;
    }
}
