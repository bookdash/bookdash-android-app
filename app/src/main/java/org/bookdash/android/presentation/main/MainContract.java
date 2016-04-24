package org.bookdash.android.presentation.main;


public interface MainContract {
    interface MainView {

        void showThanksPopover();

        void showAboutPage();

        void showRatingPlayStore();

        void showAllBooksPage();

        void showDownloadedBooksPage();
    }

    interface MainUserActions {

        void clickViewDownloadBooks();

        void clickViewAllBooks();
    }
}

