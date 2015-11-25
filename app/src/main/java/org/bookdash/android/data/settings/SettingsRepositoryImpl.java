package org.bookdash.android.data.settings;

import android.support.annotation.NonNull;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsRepositoryImpl implements SettingsRepository {

    private final SettingsApi settingsApi;

    public SettingsRepositoryImpl(@NonNull SettingsApi settingsApi) {
        this.settingsApi = settingsApi;
    }


    @Override
    public boolean isFirstTime() {
        return settingsApi.isFirstTime();
    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        settingsApi.setIsFirstTime(isFirstTime);
    }

    @Override
    public String getLanguagePreference() {
        return settingsApi.getLanguagePreference();
    }

    @Override
    public void saveLanguagePreference(String languagePreference) {
        settingsApi.saveLanguagePreference(languagePreference);
    }


}
