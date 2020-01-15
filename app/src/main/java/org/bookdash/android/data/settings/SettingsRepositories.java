package org.bookdash.android.data.settings;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsRepositories {

    private static SettingsRepository repository = null;

    private SettingsRepositories() {
        // no instance
    }

    public synchronized static SettingsRepository getInstance(@NonNull SettingsApi settingsApi,
                                                              FirebaseMessaging firebaseMessaging) {
        if (null == repository) {
            repository = new SettingsRepositoryImpl(settingsApi, firebaseMessaging);
        }
        return repository;
    }
}
