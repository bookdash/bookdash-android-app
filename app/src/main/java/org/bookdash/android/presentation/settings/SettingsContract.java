package org.bookdash.android.presentation.settings;

import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;


public interface SettingsContract {

    interface View extends MvpView {
        void showTutorialScreen(ArrayList<TutorialItem> tutorialItems);

    }

    interface Presenter extends MvpPresenter<View> {
        void openTutorialScreen();

        void setNewBookNotificationSubscriptionStatus(boolean onOff);
    }
}
