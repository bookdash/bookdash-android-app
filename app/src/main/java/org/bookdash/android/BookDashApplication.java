package org.bookdash.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.Parse;
import com.parse.ParseObject;

import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Contributor;
import org.bookdash.android.domain.pojo.Language;

import io.fabric.sdk.android.Fabric;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 8:54 AM
 */
public class BookDashApplication extends Application {
    public static boolean isTablet = false;
    public static String FILES_DIR;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ParseObject.registerSubclass(BookDetail.class);
        ParseObject.registerSubclass(Language.class);
        ParseObject.registerSubclass(Book.class);
        ParseObject.registerSubclass(Contributor.class);
        ParseObject.registerSubclass(BookContributor.class);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        Parse.initialize(this, BuildConfig.PARSE_APPLICATION_ID, BuildConfig.PARSE_CLIENT_KEY);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        FILES_DIR = getFilesDir().getPath();
        getDefaultTracker().enableAutoActivityTracking(true);

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
