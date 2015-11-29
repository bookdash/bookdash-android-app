package org.bookdash.android.presentation.listbooks;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.MenuItem;

import org.bookdash.android.BuildConfig;
import org.bookdash.android.R;
import org.bookdash.android.presentation.about.AboutActivity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;


import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/22 3:26 PM
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OverflowMenuOptionsTest {
    @Rule
    public ActivityTestRule<ListBooksActivity> mActivityTestRule =
            new ActivityTestRule<>(ListBooksActivity.class);


    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void aboutMenuClick_ShowAboutBookDashScreen() {
        //Given
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        //When
        String title = InstrumentationRegistry.getTargetContext().getString(R.string.action_about);
        onView(withText(title)).perform(click());

        //Then
        intended(hasComponent(AboutActivity.class.getName()));
    }



    @Test
    public void rateThisAppClick_ShowPlayStoreDetail() {
        //Given
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        //When
        String title = InstrumentationRegistry.getTargetContext().getString(R.string.rate_this_app);
        onView(withText(title)).perform(click());

        //Then
        intended(allOf(hasAction(Intent.ACTION_VIEW),
                        hasData(Uri.parse("market://details?id=" + org.bookdash.android.BuildConfig.APPLICATION_ID))
                )
        );
    }

    @Test
    public void contributorsClicked_ShowThanksPopover() {
        //Given
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        //When
        String title = InstrumentationRegistry.getTargetContext().getString(R.string.settings_thank_yous);
        onView(withText(title)).perform(click());

        //Then
        onView(withText("Contributors")).inRoot(isDialog()).check(matches(isDisplayed()));

    }

    @Test
    public void contributorsOkClick_HideThanksPopover() {
        //Given
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        //When
        String title = InstrumentationRegistry.getTargetContext().getString(R.string.settings_thank_yous);
        onView(withText(title)).perform(click());

        //Then
        onView(withText(android.R.string.ok)).perform(click());

    }
}
