package org.bookdash.android.presentation.listbooks;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.bookdash.android.R;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.utils.NavigationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        NavigationUtils.selectNavDrawItem(mActivityTestRule.getActivity(), R.id.action_about);
        //Then
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
    }


    @Test
    public void contributorsClicked_ShowThanksPopover() {

        //When
        NavigationUtils.selectNavDrawItem(mActivityTestRule.getActivity(), R.id.action_thanks);
        //Then
        onView(withText("Contributors")).inRoot(isDialog()).check(matches(isDisplayed()));

    }

    @Test
    public void contributorsOkClick_HideThanksPopover() {
        //When
        NavigationUtils.selectNavDrawItem(mActivityTestRule.getActivity(), R.id.action_thanks);

        //Then
        onView(withText(android.R.string.ok)).perform(click());

    }


}
