package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface ListBooksContract {

    interface View {
        void showThanksPopover();
        void showAboutPage();
        void showRatingPlayStore();
        void showErrorScreen(boolean show, String errorMessage, boolean showRetryButton);
        void showLoading(boolean visible);
        void showBooks(List<BookDetail> bookDetailList);
        void showSnackBarError(int message);
        void showLanguagePopover(String[] languages, int selected);
    }

    interface UserActionsListener {
        void loadLanguages();
        void saveSelectedLanguage(int indexOfLanguage, boolean downloadOnly);
        void loadBooksForLanguagePreference(boolean downloadedOnly);
        void clickOpenLanguagePopover();
    }

}
