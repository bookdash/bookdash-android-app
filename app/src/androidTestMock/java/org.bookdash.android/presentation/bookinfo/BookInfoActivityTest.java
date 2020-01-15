package org.bookdash.android.presentation.bookinfo;

import android.content.Intent;
import android.test.suitebuilder.annotation.LargeTest;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author rebeccafranks
 * @since 15/11/21.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BookInfoActivityTest {

    public static final String BOOK_OBJ_ID = "f4r2gho2h";
    @Rule
    public ActivityTestRule<BookInfoActivity> activityTestRule = new ActivityTestRule<>(BookInfoActivity.class, true,
            false);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void loadBookInfo_DisplayBookInformation() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FireBookDetails bookDetailParcelable = new FireBookDetails();
        bookDetailParcelable.setBookId(BOOK_OBJ_ID);
        bookDetailParcelable.setBookDescription("Book description is great");
        bookDetailParcelable.setBookCoverPageUrl("http://riggaroo.co.za/bookdash/3-fishgift/xhosa/1-cover.jpg");
        intent.putExtra(BookInfoActivity.BOOK_PARCEL, bookDetailParcelable);
        activityTestRule.launchActivity(intent);

        onView(withText("Book description is great"))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withText("Rebecca Franks")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
