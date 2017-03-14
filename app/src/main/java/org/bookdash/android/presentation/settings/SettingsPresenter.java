package org.bookdash.android.presentation.settings;

import org.bookdash.android.data.tutorial.TutorialsRepository;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements
    SettingsContract.Presenter {

    private TutorialsRepository mRepository;
    private ArrayList<TutorialItem> mTutorialItems;

    public SettingsPresenter(TutorialsRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public void openTutorialScreen() {
        checkViewAttached();
        mTutorialItems = mRepository.getTutorialItems();
        getView().showTutorialScreen(mTutorialItems);
    }
}
