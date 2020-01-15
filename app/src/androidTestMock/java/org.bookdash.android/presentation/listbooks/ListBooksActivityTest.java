package org.bookdash.android.presentation.listbooks;

import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.search.SearchActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

/**
 * @author rebeccafranks
 * @since 15/11/14.
 */
@RunWith(AndroidJUnit4.class)
public class ListBooksActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Injection.provideSettingsRepo(InstrumentationRegistry.getTargetContext())
                .saveLanguagePreference(FakeSettingsApiImpl.fireLanguage);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void loadBooks_BookTitlesVisible() {
        //Given
        onView(withId(R.id.action_language_choice)).perform(click());

        onView(withText("English")).perform(click());

        //Then
        onView(withText("Searching for the Spirit of Spring")).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnBook_OpensBookDetails() {
        //When
        onView(withText("Searching for the Spirit of Spring")).perform(click());
        //Then
        intended(hasComponent(BookInfoActivity.class.getName()));

    }

    @Test
    public void openScreen_AppBarTitleIsCorrect() {
        matchToolbarTitle("Book Dash");
    }

    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is(title))));
    }

    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    @Test
    public void chooseDifferentLanguage_NewBooksLoaded() {
        // selectNavDrawItem(R.id.action_language_choice);
        onView(withId(R.id.action_language_choice)).perform(click());

        //When
        onView(withText("Zulu")).perform(click());

        //Then
        onView(withText("Searching for the Spirit of Spring Zulu")).check(matches(isDisplayed()));
        onView(withText("Searching for the Spirit of Spring")).check(doesNotExist());
    }

    @Test
    public void testGetScreenName_IsBookListing() {
        Assert.assertEquals("MainActivity", activityTestRule.getActivity().getScreenName());
    }

    @Test
    public void testClickSearchIcon_OpensSearchActivity() {
        onView(withId(R.id.action_search_books)).perform(click());

        intended(hasComponent(SearchActivity.class.getName()));

    }
}
