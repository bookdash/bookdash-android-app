package org.bookdash.android.presentation.listbooks;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.bookdash.android.R;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.utils.NavigationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


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
        onView(withId(R.id.container_language)).perform(click());
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
