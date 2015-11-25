package org.bookdash.android.presentation.about;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.NestedScrollView;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Html;
import android.view.View;

import org.bookdash.android.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;


import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * @author rebeccafranks
 * @since 15/11/21.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class AboutActivityTest {
    @Rule
    public ActivityTestRule<AboutActivity> testRule = new ActivityTestRule<>(AboutActivity.class);

    @Before
    public void setUp(){
        Intents.init();
    }

    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void loadAboutBookDash_SeeInformation() throws Throwable {
        CharSequence about = Html.fromHtml(InstrumentationRegistry.getTargetContext().getString(R.string.why_bookdash));
        String headingAbout = InstrumentationRegistry.getTargetContext().getString(R.string.heading_about);

        onView(withText(headingAbout)).check(matches(isDisplayed()));

        onView(withText(about.toString())).perform(scrollTo()).check(matches(isDisplayed()));

    }

    @Test
    public void clickLearnMore_OpenBrowser() throws Throwable {

        onView(withText("LEARN MORE")).perform(scrollTo(),click());

        intended(allOf(hasAction(Intent.ACTION_VIEW),
                        hasData(Uri.parse("http://bookdash.org"))
                )
        );
    }
}
