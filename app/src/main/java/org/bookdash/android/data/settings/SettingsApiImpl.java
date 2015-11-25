package org.bookdash.android.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsApiImpl implements SettingsApi {
    private final Context context;
    private static final String LANGUAGE_SETTING = "language_option";
    private static final String PREF_IS_FIRST_TIME = "is_first_time";

    public SettingsApiImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isFirstTime() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_IS_FIRST_TIME, true);

    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(PREF_IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    @Override
    public String getLanguagePreference() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANGUAGE_SETTING, "English");
    }

    @Override
    public void saveLanguagePreference(String langPref) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(LANGUAGE_SETTING, langPref);
        editor.apply();
    }

}
