package org.bookdash.android.presentation.settings;

import org.bookdash.android.presentation.base.BasePresenter;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements
    SettingsContract.Presenter {


    @Override
    public void openTutorialScreen() {
        checkViewAttached();
        getView().showTutorialScreen();
    }
}
