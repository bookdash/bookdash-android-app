package org.bookdash.android.presentation.settings;

import org.bookdash.android.data.tutorial.TutorialsRepository;
import org.bookdash.android.presentation.base.BasePresenter;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements
        SettingsContract.Presenter {

    private TutorialsRepository repository;

    public SettingsPresenter(TutorialsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void openTutorialScreen() {
        checkViewAttached();
        getView().showTutorialScreen(repository.getTutorialItems());
    }
}
