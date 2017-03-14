package org.bookdash.android.data.tutorial;

import android.content.Context;

import org.bookdash.android.R;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Created by michel.onwordi on 14/03/2017.
 */

public class TutorialsRepositoryImpl implements TutorialsRepository {
    private final Context mContext;

    public TutorialsRepositoryImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public ArrayList<TutorialItem> getTutorialItems() {
        TutorialItem tutorialItem1 = new TutorialItem(mContext.getString(R.string.slide_1_african_story_books), mContext.getString(R.string.slide_1_african_story_books_subtitle),
            R.color.slide_1, R.drawable.tut_page_1_front, R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(mContext.getString(R.string.slide_2_volunteer_professionals), mContext.getString(R.string.slide_2_volunteer_professionals_subtitle),
            R.color.slide_2, R.drawable.tut_page_2_front, R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(mContext.getString(R.string.slide_3_download_and_go), mContext.getString(R.string.slide_3_download_and_go_subtitle),
            R.color.slide_3, R.drawable.tut_page_3_foreground, R.drawable.tut_page_3_background);

        TutorialItem tutorialItem4 = new TutorialItem(mContext.getString(R.string.slide_4_different_languages), mContext.getString(R.string.slide_4_different_languages_subtitle),
            R.color.slide_4, R.drawable.tut_page_4_foreground, R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }
}
