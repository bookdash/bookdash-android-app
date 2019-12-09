package org.bookdash.android.presentation.splash;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.splash.util.ElapsedTimeIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
@RunWith(AndroidJUnit4.class)
public class SplashScreenTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class, true,
            false);
    private int splashScreenWaitingTime = 1100;

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

}
