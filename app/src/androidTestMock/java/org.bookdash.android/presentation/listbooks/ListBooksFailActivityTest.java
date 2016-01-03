package org.bookdash.android.presentation.listbooks;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import org.bookdash.android.R;
import org.bookdash.android.data.books.FakeBookDetailApiImpl;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.MenuItem;

import org.bookdash.android.R;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.presentation.about.AboutActivity;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.bookdash.android.test.BuildConfig;


import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;


import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * @author rebeccafranks
 * @since 15/11/22.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListBooksFailActivityTest {
    @Rule
    public ActivityTestRule<ListBooksActivity> activityActivityTestRule  = new ActivityTestRule<>(ListBooksActivity.class, true, false);

    @Before
    public void setUp(){
        Intents.init();

    }

    @After
    public void tearDown(){
        Intents.release();

    }

    @Test
    public void loadLanguages_LoadError_ShowSnackBarMessage(){
        FakeBookDetailApiImpl.setShouldFailService(true);
        activityActivityTestRule.launchActivity(null);

        String text = InstrumentationRegistry.getTargetContext().getString(R.string.error_loading_languages);
        onView(withText(text)).check(matches(isDisplayed()));

        FakeBookDetailApiImpl.setShouldFailService(false);

    }

    @Test
    public void loadBooks_LoadError_ShowRetryButton(){
        FakeBookDetailApiImpl.setShouldFailService(true);
        activityActivityTestRule.launchActivity(null);

        onView(withText("BOOKS LOAD ERROR")).check(matches(isDisplayed()));
        onView(withText("Retry")).check(matches(isDisplayed()));

        FakeBookDetailApiImpl.setShouldFailService(false);
    }

    @Test
    public void clickRetryButton_ReloadsBooksDisplayed(){
        //Given
        FakeBookDetailApiImpl.setShouldFailService(true);
        activityActivityTestRule.launchActivity(null);
        onView(withText("BOOKS LOAD ERROR")).check(matches(isDisplayed()));
        onView(withText("Retry")).check(matches(isDisplayed()));

        //When
        FakeBookDetailApiImpl.setShouldFailService(false);
        onView(withText("Retry")).perform(click());
        //Then
        onView(withText("Searching for Spring")).check(matches(isDisplayed()));
        onView(withText("Why is Nita Upside Down?")).check(matches(isDisplayed()));

    }
}
