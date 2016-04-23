package org.bookdash.android.presentation.main;

import org.bookdash.android.data.settings.SettingsApi;
import org.bookdash.android.domain.pojo.Language;

import java.util.List;

/**
 * Created by rebeccafranks on 16/04/19.
 */
public class MainPresenter implements MainContract.MainUserActions {

    private MainContract.MainView mainView;

    public MainPresenter(MainContract.MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void clickViewDownloadBooks() {
        mainView.showDownloadedBooksPage();
    }

    @Override
    public void clickViewAllBooks() {
        mainView.showAllBooksPage();
    }


}
