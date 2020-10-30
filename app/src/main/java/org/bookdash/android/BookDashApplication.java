package org.bookdash.android;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.installations.FirebaseInstallations;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.bookdash.android.config.CrashlyticsTree;

import rx.Subscriber;
import timber.log.Timber;


/**
 * @author Rebecca Franks
 * @since 2015/07/16 8:54 AM
 */
public class BookDashApplication extends MultiDexApplication {
    public static boolean isTablet = false;
    public static String FILES_DIR;
    private FirebaseAnalytics firebaseAnalytics;

    public static BookDashApplication get(Context context) {
        return (BookDashApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Firebase Debug Info:%s", FirebaseInstallations.getInstance().getId());
        } else {
            Timber.plant(new CrashlyticsTree());
        }

        Injection.init(this);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        FILES_DIR = getFilesDir().getPath();
        getDefaultTracker();
        Injection.provideSettingsRepo(getApplicationContext()).initialSubscribeToNewBookNotifications()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {

                    }

                    @Override
                    public void onNext(final Boolean aBoolean) {

                    }
                });

    }

    synchronized public FirebaseAnalytics getDefaultTracker() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        }
        return firebaseAnalytics;
    }

}
