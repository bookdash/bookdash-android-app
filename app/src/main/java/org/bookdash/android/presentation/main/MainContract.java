package org.bookdash.android.presentation.main;


public interface MainContract {
    interface MainView {

        void showThanksPopover();

        void showAboutPage();

        void showRatingPlayStore();

        void showAllBooksPage();

        void showDownloadedBooksPage();

        void inviteFriends();

        void showSettingsScreen();
    }

    interface MainUserActions {

        void clickViewDownloadBooks();

        void clickViewAllBooks();

        void clickRateApp();

        void clickShowContributors();

        void clickInvitePage();

        void clickShowSettings();
    }
}

