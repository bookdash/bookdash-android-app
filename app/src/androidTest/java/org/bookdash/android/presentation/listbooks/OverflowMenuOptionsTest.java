package org.bookdash.android.presentation.listbooks;

import android.support.design.widget.NavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.bookdash.android.R;
import org.bookdash.android.presentation.about.AboutActivity;
import org.bookdash.android.presentation.main.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
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
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void languageItemClick_ShowLanguageChooser() {
        onView(withId(R.id.action_language_choice)).perform(click());
        //Then
        onView(withText(R.string.language_selection_heading)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void aboutMenuClick_ShowAboutBookDashScreen() {
        //When
        selectNavDrawItem(R.id.action_about);
        //Then
        intended(hasComponent(AboutActivity.class.getCanonicalName()));
    }


    @Test
    public void contributorsClicked_ShowThanksPopover() {

        //When
        selectNavDrawItem(R.id.action_thanks);
        //Then
        onView(withText("Contributors")).inRoot(isDialog()).check(matches(isDisplayed()));

    }

    @Test
    public void contributorsOkClick_HideThanksPopover() {
        //When
        selectNavDrawItem(R.id.action_thanks);

        //Then
        onView(withText(android.R.string.ok)).perform(click());

    }

    // Due to the NavigationItem not being exposed, we have to do this work around to test NavigationView
    // https://code.google.com/p/android/issues/detail?id=187701
    public void selectNavDrawItem(final int navItemId){
        onView(allOf(withContentDescription(containsString("Navigate up")), isClickable())).perform(click());
        final NavigationView navigation =(NavigationView) mActivityTestRule.getActivity().findViewById(R.id.navigation_view);
        mActivityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigation.getMenu().performIdentifierAction(navItemId, 0);
                navigation.setCheckedItem(navItemId);
            }
        });
    }
}
