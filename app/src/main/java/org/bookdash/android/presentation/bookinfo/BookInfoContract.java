package org.bookdash.android.presentation.bookinfo;

import android.graphics.Bitmap;

import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookInfoContract {

    interface View {

        void showProgress(boolean visible);

        void showBookDetailView();

        void showError(String errorMessage);

        void showSnackBarMessage(int message);

        void showDownloadProgress(int downloadProgress);

        void showDownloadFinished();

        void setToolbarTitle(String title);

        void setBookInfoBinding(BookDetail bookInfo);

        void openBook(BookDetail bookDetail, BookPages bookPages, String location);

        void showContributors(List<BookContributor> contributors);

        void onImageLoaded(Bitmap bitmap);

        void setStatusBarColor(int color);

        void setAccentColor(int accentColor);

        void setToolbarColor(int color);

        void sendShareEvent(String bookTitle);
    }

    interface UserActionsListener {

        void loadBookInformation(String bookDetailId);

        void downloadBook(BookDetail bookDetail);

        void loadImage(String url);

        void shareBookClicked(BookDetail bookInfo);
    }
}
