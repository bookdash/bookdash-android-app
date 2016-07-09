package org.bookdash.android.presentation.bookinfo;

import android.graphics.Bitmap;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.gson.BookPages;
import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookInfoContract {

    interface View  extends MvpView{

        void showBookDetailView();

        void showError(String errorMessage);

        void showSnackBarMessage(int message);

        void showDownloadProgress(int downloadProgress);

        void showDownloadFinished();

        void setToolbarTitle(String title);

        void setBookInfoBinding(FireBookDetails bookInfo);

        void openBook(FireBookDetails bookDetail, BookPages bookPages, String location);

        void showContributors(List<FireContributor> contributors);

        void sendShareEvent(String bookTitle);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadContributorInformation(FireBookDetails bookDetailId);

        void downloadBook(FireBookDetails bookDetail);

        void shareBookClicked(FireBookDetails bookInfo);
    }
}
