package org.bookdash.android.presentation.settings;

import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public interface SettingsContract {

    interface View extends MvpView{
        void showTutorialScreen();
    }

    interface Presenter extends MvpPresenter<View>{
        void openTutorialScreen();
    }
}
