package org.bookdash.android;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 8:54 AM
 */
public class BookDashApplication extends Application {
    public static boolean isTablet = false;
    public static String FILES_DIR;
    private FirebaseAnalytics firebaseAnalytics;

    public static BookDashApplication get(Context context) {
        return (BookDashApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Crashlytics crashlyticsKit = new Crashlytics.Builder().core(
                new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG)
                        .build())
                .build();
        Fabric.with(this, crashlyticsKit);

        Injection.init(this);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        FILES_DIR = getFilesDir().getPath();
        getDefaultTracker();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/minyna.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


    }

    synchronized public FirebaseAnalytics getDefaultTracker() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        }
        return firebaseAnalytics;
    }

}
