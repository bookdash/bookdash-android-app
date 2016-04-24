package org.bookdash.android.presentation.splash;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.splash.util.ElapsedTimeIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashScreenTest {

    private int splashScreenWaitingTime = 1100;
    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule =
            new ActivityTestRule<>(SplashActivity.class, true, false);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void viewSplashFirstTime_NavigateToTutorialAfter1000ms() throws InterruptedException {
        FakeSettingsApiImpl.setFirstTime(true);

        activityTestRule.launchActivity(null);
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(splashScreenWaitingTime);
        Espresso.registerIdlingResources(idlingResource);

        intended(hasComponent(MaterialTutorialActivity.class.getName()));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void viewSplashScreenSecondTime_NavigateToListBooksAfter1000ms() throws InterruptedException {
        FakeSettingsApiImpl.setFirstTime(false);

        activityTestRule.launchActivity(null);
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(splashScreenWaitingTime);
        Espresso.registerIdlingResources(idlingResource);


        intended(hasComponent(MainActivity.class.getName()));

        Espresso.unregisterIdlingResources(idlingResource);

    }

    @Test
    public void viewSplashScreenFinish_StartListBooks(){
        FakeSettingsApiImpl.setFirstTime(true);

        activityTestRule.launchActivity(null);
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(splashScreenWaitingTime);
        Espresso.registerIdlingResources(idlingResource);

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, null);

        intending(hasComponent(MaterialTutorialActivity.class.getName())).respondWith(result);

        intended(hasComponent(MainActivity.class.getName()));

        Espresso.unregisterIdlingResources(idlingResource);
    }
}
