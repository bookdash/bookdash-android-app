package org.bookdash.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.parse.Parse;
import com.parse.ParseObject;

import org.bookdash.android.domain.model.Book;
import org.bookdash.android.domain.model.BookContributor;
import org.bookdash.android.domain.model.Contributor;

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

    @Override
    public void onCreate() {
        super.onCreate();

        Crashlytics crashlyticsKit = new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build();
        Fabric.with(this, crashlyticsKit);
        Injection.init(this);
        ParseObject.registerSubclass(Book.class);
        ParseObject.registerSubclass(Contributor.class);
        ParseObject.registerSubclass(BookContributor.class);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        Parse.initialize(this, BuildConfig.PARSE_APPLICATION_ID, BuildConfig.PARSE_CLIENT_KEY);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        FILES_DIR = getFilesDir().getPath();
        getDefaultTracker();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/ComingSoon.ttf").setFontAttrId(R.attr.fontPath).build());
    }


    synchronized public FirebaseAnalytics getDefaultTracker() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        }
        return firebaseAnalytics;
    }
}
