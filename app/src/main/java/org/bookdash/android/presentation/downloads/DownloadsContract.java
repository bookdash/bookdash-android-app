package org.bookdash.android.presentation.downloads;


import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

public interface DownloadsContract {

    interface View {

        void showDownloadedBooks(List<BookDetail> books);

        void showLoading(boolean show);

        void showErrorScreen(boolean show, String errorMessage, boolean showRetryButton);

        void showSnackBarError(int message);

        void showSnackBarError(String message);

        void showNoBooksDownloadedMessage();
    }

    interface UserActions {
        void loadListDownloads();

        void deleteDownload(BookDetail bookDetail);
    }

}
