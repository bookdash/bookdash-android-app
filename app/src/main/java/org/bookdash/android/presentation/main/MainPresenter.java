package org.bookdash.android.presentation.main;


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
