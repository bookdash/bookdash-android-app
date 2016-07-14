package org.bookdash.android.presentation.downloads;


import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.List;

interface DownloadsContract {

    interface View extends MvpView {

        void showDownloadedBooks(List<FireBookDetails> books);

        void showLoading(boolean show);

        void showErrorScreen(boolean show, String errorMessage, boolean showRetryButton);

        void showSnackBarError(int message);

        void showSnackBarError(String message);

        void showNoBooksDownloadedMessage();
    }

    interface Presenter extends MvpPresenter<View> {
        void loadListDownloads();

        void deleteDownload(FireBookDetails bookDetail);
    }

}
