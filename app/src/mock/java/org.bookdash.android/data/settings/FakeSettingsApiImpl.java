package org.bookdash.android.data.settings;

import android.support.annotation.VisibleForTesting;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class FakeSettingsApiImpl implements SettingsApi {
    private static boolean isFirstTime = true;
    private static String languagePreference = "English";

    @Override
    public boolean isFirstTime() {
        return isFirstTime;
    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    @Override
    public String getLanguagePreference() {
        return languagePreference;
    }

    @Override
    public void saveLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    @VisibleForTesting
    public static void setFirstTime(boolean firstTime){
        isFirstTime = firstTime;
    }

    @VisibleForTesting
    public static void setLanguagePref(String lang) {
        languagePreference = lang;
    }
}
