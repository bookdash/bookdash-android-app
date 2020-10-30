package org.bookdash.android.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.bookdash.android.R;

public class FirebaseConfig implements RemoteConfigSettingsApi {

    private static final long CACHE_EXPIRATION_IN_SECONDS = 3600;
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
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().
                setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG ? 0 : CACHE_EXPIRATION_IN_SECONDS).build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.firebase_remote_config_defaults);
        return new FirebaseConfig(firebaseRemoteConfig);
    }

    public FirebaseConfig init() {
        firebaseRemoteConfig.fetch(CACHE_EXPIRATION_IN_SECONDS).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activate();
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

