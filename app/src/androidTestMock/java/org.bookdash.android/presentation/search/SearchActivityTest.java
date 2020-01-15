package org.bookdash.android.presentation.search;


import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.bookdash.android.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

    @Test
    public void searchActivity_enterTypeSearchTerm_Displays2Results() {

        ViewInteraction searchAutoComplete = onView(allOf(withId(R.id.search_src_text),
                withParent(allOf(withId(R.id.search_plate), withParent(withId(R.id.search_edit_frame)))),
                isDisplayed()));
        searchAutoComplete.perform(replaceText("spirit"), closeSoftKeyboard());

        onView(withText("spirit")).check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewBookName), withText("Searching for the Spirit of Spring"),
                        childAtPosition(childAtPosition(withId(R.id.card_view), 0), 1), isDisplayed()));
        textView.check(matches(withText("Searching for the Spirit of Spring")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewBookName), withText("Searching for the Spirit of Spring Zulu"),
                        childAtPosition(childAtPosition(withId(R.id.card_view), 0), 1), isDisplayed()));
        textView2.check(matches(withText("Searching for the Spirit of Spring Zulu")));

    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view
                        .equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
