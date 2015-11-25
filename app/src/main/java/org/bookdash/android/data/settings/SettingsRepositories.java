package org.bookdash.android.data.settings;

import android.support.annotation.NonNull;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsRepositories {

    private SettingsRepositories() {
        // no instance
    }

    private static SettingsRepository repository = null;

    public synchronized static SettingsRepository getInstance(@NonNull SettingsApi settingsApi) {
        if (null == repository) {
            repository = new SettingsRepositoryImpl(settingsApi);
        }
        return repository;
    }
}
