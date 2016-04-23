package org.bookdash.android.presentation.utils;

import android.app.Activity;
import android.support.design.widget.NavigationView;

import org.bookdash.android.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;


public class NavigationUtils {

    public static void selectNavDrawItem(Activity activity, final int navItemId){
        onView(allOf(withContentDescription(containsString("Navigate up")), isClickable())).perform(click());
        final NavigationView navigation =(NavigationView) activity.findViewById(R.id.navigation_view);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigation.getMenu().performIdentifierAction(navItemId, 0);
                navigation.setCheckedItem(navItemId);
            }
        });
    }
}
