package org.bookdash.android.presentation.splash;

import android.content.Context;

import org.bookdash.android.R;
import org.bookdash.android.data.settings.SettingsRepository;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SplashPresenter implements SplashContract.UserActionsListener {
    private final SettingsRepository settingsRepository;
    private final SplashContract.View splashView;

    public SplashPresenter(SplashContract.View view, SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
        splashView = view;
    }

    @Override
    public void loadSplash() {
        if (settingsRepository.isFirstTime()) {
            splashView.loadTutorial();
        } else {
            splashView.loadMainScreen();
        }
    }

    @Override
    public ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
                R.color.slide_1, R.drawable.tut_page_1_front,  R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(context.getString(R.string.slide_2_volunteer_professionals), context.getString(R.string.slide_2_volunteer_professionals_subtitle),
                R.color.slide_2,  R.drawable.tut_page_2_front,  R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.slide_3_download_and_go), context.getString(R.string.slide_3_download_and_go_subtitle),
                R.color.slide_3, R.drawable.tut_page_3_foreground,  R.drawable.tut_page_3_background);

        TutorialItem tutorialItem4 = new TutorialItem(context.getString(R.string.slide_4_different_languages), context.getString(R.string.slide_4_different_languages_subtitle),
                R.color.slide_4,  R.drawable.tut_page_4_foreground,  R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    public void finishedTutorial() {
        settingsRepository.setIsFirstTime(false);
        splashView.loadMainScreen();
    }
}
