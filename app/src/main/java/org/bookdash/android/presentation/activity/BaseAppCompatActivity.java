package org.bookdash.android.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import org.bookdash.android.BookDashApplication;


public abstract class BaseAppCompatActivity extends AppCompatActivity {
   // protected Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookDashApplication application = (BookDashApplication) getApplication();
    /*    tracker = application.getDefaultTracker();
        tracker.setScreenName(getScreenName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());*/
    }
    protected abstract String getScreenName();
}
