package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.R;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Language;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class ListBooksPresenter implements ListBooksContract.UserActionsListener {

    private ListBooksContract.View listBooksView;
    private BookDetailRepository bookDetailRepository;
    private SettingsRepository settingsRepository;

    private List<Language> languages;

    public ListBooksPresenter(ListBooksContract.View listBooksView, BookDetailRepository bookDetailRepository, SettingsRepository settingsRepository) {
        this.listBooksView = listBooksView;
        this.bookDetailRepository = bookDetailRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void loadBooksForLanguagePreference() {
        String languagePreference = settingsRepository.getLanguagePreference();
        loadBooksForLanguage(languagePreference);
    }

    @Override
    public void searchBooksForLanguage(String searchString) {
        listBooksView.showLoading(true);
        bookDetailRepository.searchBooksForLanguage(searchString, settingsRepository.getLanguagePreference(),
                new OnBookLoadCallback());
    }

    private void loadBooksForLanguage(String language) {
        listBooksView.showLoading(true);
        bookDetailRepository.getBooksForLanguage(language, new OnBookLoadCallback());

    }

    @Override
    public void loadLanguages() {
        bookDetailRepository.getLanguages(new BookDetailRepository.GetLanguagesCallback() {

            @Override
            public void onLanguagesLoaded(List<Language> languages) {
                ListBooksPresenter.this.languages = languages;
            }

            @Override
            public void onLanguagesLoadError(Exception e) {
                listBooksView.showSnackBarError(R.string.error_loading_languages);
            }
        });

    }

    @Override
    public void saveSelectedLanguage(int indexOfLanguage) {
        settingsRepository.saveLanguagePreference(languages.get(indexOfLanguage).getLanguageName());

        loadBooksForLanguage(languages.get(indexOfLanguage).getLanguageName());
    }

    @Override
    public void clickOpenLanguagePopover() {
        if (languages == null) {
            return;
        }
        int languageToSelect = 0;
        String selectedLanguage = settingsRepository.getLanguagePreference();
        String[] langArray = new String[languages.size()];

        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).getLanguageName().equals(selectedLanguage)) {
                languageToSelect = i;

            }
            langArray[i] = languages.get(i).getLanguageName();
        }
        listBooksView.showLanguagePopover(langArray, languageToSelect);
    }

    @Override
    public void clickDisplaySearch() {
        listBooksView.showSearch();
    }


    private class OnBookLoadCallback implements BookDetailRepository.GetBooksForLanguageCallback {
        @Override
        public void onBooksLoaded(List<BookDetail> books) {
            onBooksLoad(books, false, "", false);
        }

        @Override
        public void onBooksLoadError(Exception e) {
            onBooksLoad(null, true, e.getMessage().toUpperCase(), true);
        }

        public void onBooksLoad(List<BookDetail> books,
                                boolean show,
                                String errorMessage,
                                boolean showRetryButton) {
            listBooksView.showLoading(false);
            listBooksView.showErrorScreen(show, errorMessage, showRetryButton);
            if (books != null) listBooksView.showBooks(books);
        }
    }

}
