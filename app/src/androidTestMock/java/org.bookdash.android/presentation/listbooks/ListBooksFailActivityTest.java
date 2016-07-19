package org.bookdash.android.presentation.listbooks;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.bookdash.android.presentation.main.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author rebeccafranks
 * @since 15/11/22.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListBooksFailActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true,
            false);

    @Before
    public void setUp() {
        Intents.init();

    }

    @After
    public void tearDown() {
        Intents.release();

    }

  /*  @Test
    public void loadLanguages_LoadError_ShowSnackBarMessage() {
        FakeBookDetailApiImpl.setShouldFailService(true);
        activityActivityTestRule.launchActivity(null);

        String text = InstrumentationRegistry.getTargetContext().getString(R.string.error_loading_languages);
        onView(withText(text)).check(matches(isDisplayed()));

        FakeBookDetailApiImpl.setShouldFailService(false);

    }

    @Test
    public void loadBooks_LoadError_ShowRetryButton() {
        FakeBookDetailApiImpl.setShouldFailService(true);
        activityActivityTestRule.launchActivity(null);

        onView(withText("BOOKS LOAD ERROR")).check(matches(isDisplayed()));
        onView(withText("Retry")).check(matches(isDisplayed()));

        FakeBookDetailApiImpl.setShouldFailService(false);
    }

    @Test
    public void clickRetryButton_ReloadsBooksDisplayed() {
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

    }*/
}
