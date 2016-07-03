package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
class ListBooksPresenter implements ListBooksContract.Presenter {

    private ListBooksContract.View listBooksView;
    private SettingsRepository settingsRepository;

    private List<FireLanguage> languages;
    private BookService bookService;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    ListBooksPresenter(ListBooksContract.View listBooksView, SettingsRepository settingsRepository, BookService bookService) {
        this.listBooksView = listBooksView;
        this.settingsRepository = settingsRepository;
        this.bookService = bookService;
    }

    public void startPresenting() {

    }

    public void stopPresenting() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void loadLanguages() {
        compositeSubscription.add(bookService.getLanguages().subscribe(new Action1<List<FireLanguage>>() {
            @Override
            public void call(final List<FireLanguage> fireLanguages) {
                ListBooksPresenter.this.languages = fireLanguages;

            }
        }));

    }

    @Override
    public void saveSelectedLanguage(int indexOfLanguage) {
        settingsRepository.saveLanguagePreference(languages.get(indexOfLanguage).getLanguageName());

        loadBooksForLanguage(languages.get(indexOfLanguage).getLanguageName());
    }

    @Override
    public void loadBooksForLanguagePreference() {
        String languagePreference = settingsRepository.getLanguagePreference();
        loadBooksForLanguage(languagePreference);
    }

    private void loadBooksForLanguage(String language) {
        listBooksView.showLoading(true);
        compositeSubscription.add(bookService.getBooksForLanguage(null).subscribe(new Action1<List<FireBookDetails>>() {
            @Override
            public void call(final List<FireBookDetails> fireBookDetailses) { //todo
                listBooksView.showLoading(false);
                listBooksView.showErrorScreen(false, "", false);
                listBooksView.showBooks(fireBookDetailses);
            }
        }));

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


}
