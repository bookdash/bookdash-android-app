package org.bookdash.android.config;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/**
 * @author rebeccafranks
 * @since 2016/12/11
 */
public class CrashlyticsTree extends Timber.Tree {
    @Override
    protected void log(final int priority, final String tag, final String message, final Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        Crashlytics.log(priority, tag, message);

        if (t != null) {
            Crashlytics.logException(t);
        }
    }
}
