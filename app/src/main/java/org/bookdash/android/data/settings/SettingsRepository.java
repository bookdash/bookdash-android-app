package org.bookdash.android.data.settings;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public interface SettingsRepository {

    boolean isFirstTime();

    void setIsFirstTime(boolean isFirstTime);

    Single<FireLanguage> getLanguagePreference();

    Single<Boolean> isSubscribedToNewBookNotification();

    Single<Boolean> saveLanguagePreference(FireLanguage languagePreference);

    Single<Boolean> setNewBookNotificationStatus(boolean onOff);

    Single<Boolean> initialSubscribeToNewBookNotifications();
}
