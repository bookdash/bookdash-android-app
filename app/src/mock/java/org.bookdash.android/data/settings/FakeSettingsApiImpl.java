package org.bookdash.android.data.settings;

import android.support.annotation.VisibleForTesting;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class FakeSettingsApiImpl implements SettingsApi {
    private static boolean isFirstTime = true;
    private static FireLanguage fireLanguage = new FireLanguage("English","EN", true, "1");
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


    @VisibleForTesting
    public static void setFirstTime(boolean firstTime){
        isFirstTime = firstTime;
    }


}
