package org.bookdash.android.presentation.about;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import junit.framework.Assert;

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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * @author rebeccafranks
 * @since 15/11/21.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class AboutFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        NavigationUtils.selectNavDrawItem(testRule.getActivity(), R.id.action_about);

    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void loadAboutBookDash_SeeInformation() throws Throwable {
        CharSequence about = Html.fromHtml(InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.why_bookdash));
        String headingAbout = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.heading_about);

        onView(withText(headingAbout)).check(matches(isDisplayed()));

        onView(withText(about.toString())).perform(scrollTo()).check(matches(isDisplayed()));

    }

    @Test
    public void clickLearnMore_OpenBrowser() throws Throwable {

        onView(withText(R.string.learn_more)).perform(scrollTo(), click());

        intended(allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("http://bookdash.org"))
                ));
    }

    @Test
    public void testGetScreenName() {
        Assert.assertEquals("MainActivity", testRule.getActivity().getScreenName());
    }
}
