package org.bookdash.android.presentation.listbooks;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.bookdash.android.R;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.Collections;
import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
class ListBooksPresenter extends BasePresenter<ListBooksContract.View> implements ListBooksContract.Presenter {


    private final Analytics analytics;
    private SettingsRepository settingsRepository;
    private BookService bookService;

    private List<FireLanguage> languages;
    private Scheduler ioScheduler, mainScheduler;

    ListBooksPresenter(SettingsRepository settingsRepository, BookService bookService, Analytics analytics,
                       Scheduler ioScheduler, Scheduler mainScheduler) {
        this.settingsRepository = settingsRepository;
        this.bookService = bookService;
        this.analytics = analytics;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override
    public void loadLanguages() {
        addSubscription(bookService.getLanguages().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<List<FireLanguage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {
                        getView().showSnackBarError(R.string.error_loading_languages);
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                    @Override
                    public void onNext(final List<FireLanguage> fireLanguages) {
                        ListBooksPresenter.this.languages = fireLanguages;

                    }
                }));
    }

    @Override
    public void saveSelectedLanguage(final int indexOfLanguage) {
        addSubscription(settingsRepository.saveLanguagePreference(languages.get(indexOfLanguage)).observeOn(ioScheduler)
                .subscribeOn(mainScheduler).subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(final Throwable e) {
                        getView().showSnackBarError(R.string.error_saving_selected_language);
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                    @Override
                    public void onNext(final Boolean aBoolean) {
                        FireLanguage selectedLanguage = languages.get(indexOfLanguage);
                        analytics.trackLanguageChange(selectedLanguage.getLanguageName());
                        getView().onSelectedLanguageChanged(selectedLanguage.getLanguageName());
                        loadBooksForLanguage(selectedLanguage);
                    }
                }));
    }

    @Override
    public void loadBooksForLanguagePreference() {
        getView().showLoading(true);
        addSubscription(settingsRepository.getLanguagePreference().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<FireLanguage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {
                        getView().showLoading(false);
                        getView().showErrorScreen(true, e.getMessage(), true);
                    }

                    @Override
                    public void onNext(final FireLanguage fireLanguage) {
                        analytics.setUserLanguage(fireLanguage.getLanguageName());
                        getView().onSelectedLanguageChanged(fireLanguage.getLanguageName());
                        loadBooksForLanguage(fireLanguage);
                    }
                }));

    }

    @Override
    public void clickOpenLanguagePopover() {
        if (languages == null) {
            getView().showSnackBarError(R.string.error_loading_languages_try_again);
            return;
        }
        addSubscription(settingsRepository.getLanguagePreference().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<FireLanguage>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(final Throwable e) {
                        getView().showSnackBarError(R.string.error_opening_languagepopover);
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                    @Override
                    public void onNext(final FireLanguage fireLanguage) {

                        String[] langArray = new String[languages.size()];
                        int languageToSelect = 0;

                        for (int i = 0; i < languages.size(); i++) {
                            if (languages.get(i).getLanguageName().equals(fireLanguage.getLanguageName())) {
                                languageToSelect = i;

                            }
                            langArray[i] = languages.get(i).getLanguageName();
                        }
                        getView().showLanguagePopover(langArray, languageToSelect);
                    }
                }));

    }

    @Override
    public void openSearchScreen() {
        getView().startSearchActivity();
    }

    private void loadBooksForLanguage(FireLanguage language) {
        addSubscription(bookService.getBooksForLanguage(language).observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<List<FireBookDetails>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {

                        ListBooksContract.View view = getView();
                        if (view == null) {
                            return;
                        }

                        view.showLoading(false);
                        view.showErrorScreen(true, e.getMessage(), true);
                    }

                    @Override
                    public void onNext(final List<FireBookDetails> bookList) {
                        getView().showLoading(false);
                        getView().showErrorScreen(false, "", false);
                        Collections.sort(bookList, FireBookDetails.COMPARATOR);
                        getView().showBooks(bookList);
                    }
                }));

    }


}
