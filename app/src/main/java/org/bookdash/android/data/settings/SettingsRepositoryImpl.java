package org.bookdash.android.data.settings;

import android.support.annotation.NonNull;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import rx.Single;

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
    public Single<FireLanguage> getLanguagePreference() {
        return settingsApi.getSavedLanguage();
    }

    @Override
    public Single<Boolean> saveLanguagePreference(FireLanguage languagePreference) {
        return settingsApi.saveSelectedLanguage(languagePreference);
    }


}
