package org.bookdash.android.data.settings;

import androidx.annotation.VisibleForTesting;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class FakeSettingsApiImpl implements SettingsApi {
    public static FireLanguage fireLanguage = new FireLanguage("English", "EN", true, "1");
    private static boolean isFirstTime = true;

    @Override
    public boolean isFirstTime() {
        return isFirstTime;
    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    @Override
    public Single<Boolean> saveSelectedLanguage(final FireLanguage fireLanguage) {
        this.fireLanguage = fireLanguage;
        return Single.just(true);
    }

    @Override
    public Single<FireLanguage> getSavedLanguage() {
        return Single.just(fireLanguage);
    }

    @Override
    public Single<Boolean> isSubscribedToNewBookNotification() {
        return Single.just(true);
    }

    @Override
    public Single<Boolean> saveNewBookNotificationPreference(final boolean onOff) {
        return Single.just(true);
    }


    @VisibleForTesting
    public static void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }


}
