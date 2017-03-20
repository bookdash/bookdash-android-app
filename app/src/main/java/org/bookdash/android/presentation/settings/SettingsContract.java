package org.bookdash.android.presentation.settings;

import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public interface SettingsContract {

    interface View extends MvpView {
        void showTutorialScreen(ArrayList<TutorialItem> tutorialItems);
    }

    interface Presenter extends MvpPresenter<View> {
        void openTutorialScreen();
    }
}
