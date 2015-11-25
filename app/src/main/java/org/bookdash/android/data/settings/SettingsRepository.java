package org.bookdash.android.data.settings;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public interface SettingsRepository {

    boolean isFirstTime();
    void setIsFirstTime(boolean isFirstTime);

    String getLanguagePreference();
    void saveLanguagePreference(String languagePreference);
}
