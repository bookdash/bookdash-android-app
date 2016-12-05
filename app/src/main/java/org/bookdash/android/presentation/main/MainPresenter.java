package org.bookdash.android.presentation.main;


import org.bookdash.android.data.tracking.Analytics;

public class MainPresenter implements MainContract.MainUserActions {

    private MainContract.MainView mainView;
    private Analytics analytics;

    public MainPresenter(MainContract.MainView mainView, Analytics analytics) {
        this.mainView = mainView;
        this.analytics = analytics;
    }

    @Override
    public void clickViewDownloadBooks() {
        analytics.trackViewBooksDownloaded();
        mainView.showDownloadedBooksPage();
    }

    @Override
    public void clickViewAllBooks() {
        analytics.trackViewAllBooks();
        mainView.showAllBooksPage();
    }

    @Override
    public void clickRateApp() {
        analytics.trackRateAppClick();
        mainView.showRatingPlayStore();
    }

    @Override
    public void clickShowContributors() {
        analytics.trackViewContributors();
        mainView.showThanksPopover();
    }

    @Override
    public void clickInvitePage() {
        analytics.trackInvitePeople();
        mainView.inviteFriends();
    }


}
