package org.bookdash.android.config;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.bookdash.android.BuildConfig;
import org.bookdash.android.R;

public class FirebaseConfig implements RemoteConfigSettingsApi {
    private static final int CACHE_EXPIRATION_IN_SECONDS = 3600;
    private static final String DEFAULT_LANGUAGE_ID = "default_language_id";
    private static final String DEFAULT_LANGUAGE_NAME = "default_language_name";
    private static final String DEFAULT_LANGUAGE_ABBREVIATION = "default_language_abbreviation";
    private static final String TAG = "FirebaseConfig";


    private final FirebaseRemoteConfig firebaseRemoteConfig;

    private FirebaseConfig(FirebaseRemoteConfig firebaseRemoteConfig) {
        this.firebaseRemoteConfig = firebaseRemoteConfig;
    }

    public static FirebaseConfig newInstance() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.firebase_remote_config_defaults);
        return new FirebaseConfig(firebaseRemoteConfig);
    }

    public FirebaseConfig init() {
        firebaseRemoteConfig.fetch(CACHE_EXPIRATION_IN_SECONDS).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure() called with: " + "e = [" + e + "]", e);
            }
        });
        return this;
    }


    @Override
    public String getDefaultLanguageId() {
        return firebaseRemoteConfig.getString(DEFAULT_LANGUAGE_ID);
    }

    @Override
    public String getDefaultLanguageName() {
        return firebaseRemoteConfig.getString(DEFAULT_LANGUAGE_NAME);
    }

    @Override
    public String getDefaultLanguageAbbreviation() {
        return firebaseRemoteConfig.getString(DEFAULT_LANGUAGE_ABBREVIATION);
    }
}

