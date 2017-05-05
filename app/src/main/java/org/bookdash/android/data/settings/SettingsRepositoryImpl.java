package org.bookdash.android.data.settings;

import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.concurrent.Callable;

import rx.Single;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsRepositoryImpl implements SettingsRepository {

    public static final String TOPIC_NEW_BOOK_NOTIFICATIONS = "new_book_notifications";
    private final SettingsApi settingsApi;
    private final FirebaseMessaging firebaseMessaging;

    public SettingsRepositoryImpl(@NonNull SettingsApi settingsApi, FirebaseMessaging firebaseMessaging) {
        this.settingsApi = settingsApi;
        this.firebaseMessaging = firebaseMessaging;
    }


    @Override
    public boolean isFirstTime() {
        return settingsApi.isFirstTime();
    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        settingsApi.setIsFirstTime(isFirstTime);
    }

    @Override
    public Single<FireLanguage> getLanguagePreference() {
        return settingsApi.getSavedLanguage();
    }

    @Override
    public Single<Boolean> isSubscribedToNewBookNotification() {
        return settingsApi.isSubscribedToNewBookNotification();
    }

    @Override
    public Single<Boolean> saveLanguagePreference(FireLanguage languagePreference) {
        return settingsApi.saveSelectedLanguage(languagePreference);
    }

    @Override
    public Single<Boolean> setNewBookNotificationStatus(final boolean onOff) {
        return Single.defer(new Callable<Single<Boolean>>() {
            @Override
            public Single<Boolean> call() throws Exception {
                if (onOff) {
                    Timber.d("Subscribing to topic");
                    firebaseMessaging.subscribeToTopic(TOPIC_NEW_BOOK_NOTIFICATIONS);
                } else {
                    Timber.d("Unsubscribing from topic");
                    firebaseMessaging.unsubscribeFromTopic(TOPIC_NEW_BOOK_NOTIFICATIONS);
                }
                return settingsApi.saveNewBookNotificationPreference(onOff);
            }
        });
    }

    @Override
    public Single<Boolean> initialSubscribeToNewBookNotifications() {
        return isSubscribedToNewBookNotification().flatMap(new Func1<Boolean, Single<Boolean>>() {

            @Override
            public Single<Boolean> call(final Boolean aBoolean) {
                if (aBoolean) {
                    firebaseMessaging.subscribeToTopic(TOPIC_NEW_BOOK_NOTIFICATIONS);
                } else {
                    firebaseMessaging.unsubscribeFromTopic(TOPIC_NEW_BOOK_NOTIFICATIONS);
                }
                return Single.just(aBoolean);
            }
        });
    }


}
