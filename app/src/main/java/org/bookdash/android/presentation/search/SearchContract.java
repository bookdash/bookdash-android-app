package org.bookdash.android.presentation.search;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 2016/12/11
 */

interface SearchContract {

    interface View extends MvpView {

        void showSearchResults(List<FireBookDetails> any);

        void showErrorMessage(String errorMsg);

        void showLoading();

        void hideLoading();

        void showNoInternetMessage();

        void showNoResultsMessage();

        void showRetryButton();

        void hideRetryButton();
    }

    interface Presenter extends MvpPresenter<View> {

        void search(String searchTerm);

    }
}
