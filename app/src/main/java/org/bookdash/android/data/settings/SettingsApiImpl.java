package org.bookdash.android.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.concurrent.Callable;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsApiImpl implements SettingsApi {
    public static final String FIRE_LANGUAGE_PREF = "fire_language_pref";
    private static final String LANGUAGE_SETTING = "language_option";
    private static final String PREF_IS_FIRST_TIME = "is_first_time";
    private final Context context;

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
    public Single<Boolean> saveSelectedLanguage(final FireLanguage fireLanguage) {
        return Single.defer(new Callable<Single<Boolean>>() {
            @Override
            public Single<Boolean> call() throws Exception {

                Gson gson = new Gson();
                String json = gson.toJson(fireLanguage);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                editor.putString(FIRE_LANGUAGE_PREF, json);
                editor.apply();
                return Single.just(true);
            }
        });
    }

    @Override
    public Single<FireLanguage> getSavedLanguage() {
        return Single.defer(new Callable<Single<FireLanguage>>() {
            @Override
            public Single<FireLanguage> call() throws Exception {
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String json = sharedPreferences.getString(FIRE_LANGUAGE_PREF, "");
                if (json.isEmpty()) {
                    return Single.just(new FireLanguage("English", "EN", true, "-KFQlsLuj6nKinrrPdZy"));
                }
                return Single.just(gson.fromJson(json, FireLanguage.class));
            }
        });
    }
}
