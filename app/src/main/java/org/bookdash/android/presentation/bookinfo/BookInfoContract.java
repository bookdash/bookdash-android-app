package org.bookdash.android.presentation.bookinfo;

import android.graphics.Bitmap;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookInfoContract {

    interface View {

        void showBookDetailView();

        void showError(String errorMessage);

        void showSnackBarMessage(int message);

        void showDownloadProgress(int downloadProgress);

        void showDownloadFinished();

        void setToolbarTitle(String title);

        void setBookInfoBinding(FireBookDetails bookInfo);

        void openBook(FireBookDetails bookDetail, BookPages bookPages, String location);

        void showContributors(List<FireContributor> contributors);

        void onImageLoaded(Bitmap bitmap);

        void setStatusBarColor(int color);

        void setAccentColor(int accentColor);

        void setToolbarColor(int color);

        void sendShareEvent(String bookTitle);
    }

    interface UserActionsListener {

        void loadContributorInformation(FireBookDetails bookDetailId);

        void downloadBook(FireBookDetails bookDetail);

        void loadImage(String url);

        void shareBookClicked(FireBookDetails bookInfo);
    }
}
