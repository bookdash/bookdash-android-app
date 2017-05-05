package org.bookdash.android.data.settings;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public interface SettingsApi {
    boolean isFirstTime();

    void setIsFirstTime(boolean isFirstTime);

    Single<Boolean> saveSelectedLanguage(final FireLanguage fireLanguage);

    Single<FireLanguage> getSavedLanguage();

    Single<Boolean> isSubscribedToNewBookNotification();

    Single<Boolean> saveNewBookNotificationPreference(boolean onOff);
}
