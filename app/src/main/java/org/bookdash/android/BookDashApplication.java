package org.bookdash.android;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jakewharton.threetenabp.AndroidThreeTen;
import io.fabric.sdk.android.Fabric;
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
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build();
        Fabric.with(this, crashlyticsKit);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Firebase Debug Info:" + FirebaseInstanceId.getInstance().getToken());
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
