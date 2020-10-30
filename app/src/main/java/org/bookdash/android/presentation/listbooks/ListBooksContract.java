package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.MvpPresenter;
import org.bookdash.android.presentation.base.MvpView;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface ListBooksContract {

    interface View extends MvpView {
        void showErrorScreen(boolean show, String errorMessage, boolean showRetryButton);

        void showLoading(boolean visible);

        void showBooks(List<FireBookDetails> bookDetailList);

        void showSnackBarError(int message);

        void showLanguagePopover(String[] languages, int selected);

        void startSearchActivity();

        void onSelectedLanguageChanged(String selectedLanguage);
    }

    interface Presenter extends MvpPresenter<View>{
        void loadLanguages();

        void saveSelectedLanguage(int indexOfLanguage);

        void loadBooksForLanguagePreference();

        void clickOpenLanguagePopover();

        void openSearchScreen();
    }

}
