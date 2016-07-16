package org.bookdash.android.presentation.listbooks;

import android.util.Log;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
class ListBooksPresenter extends BasePresenter<ListBooksContract.View> implements ListBooksContract.Presenter {

    private static final String TAG = "ListBooksPresenter";


    private SettingsRepository settingsRepository;
    private BookService bookService;

    private ListBooksContract.View listBooksView;
    private List<FireLanguage> languages;
    private Scheduler ioScheduler, mainScheduler;

    ListBooksPresenter(ListBooksContract.View listBooksView, SettingsRepository settingsRepository,
                       BookService bookService, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.listBooksView = listBooksView;
        this.settingsRepository = settingsRepository;
        this.bookService = bookService;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override
    public void loadLanguages() {
        addSubscription(bookService.getLanguages().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Action1<List<FireLanguage>>() {
                    @Override
                    public void call(final List<FireLanguage> fireLanguages) {
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
                        Log.d(TAG, "onCompleted() called with: " + "");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(final Boolean aBoolean) {
                        loadBooksForLanguage(languages.get(indexOfLanguage));
                    }
                }));

    }

    @Override
    public void loadBooksForLanguagePreference() {
        addSubscription(settingsRepository.getLanguagePreference().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<FireLanguage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {

                    }

                    @Override
                    public void onNext(final FireLanguage fireLanguage) {
                        Log.d(TAG, "onNext() called with: " + "fireLanguage = [" + fireLanguage + "]");
                        loadBooksForLanguage(fireLanguage);
                    }
                }));

    }

    @Override
    public void clickOpenLanguagePopover() {
        if (languages == null) {
            return;
        }
        addSubscription(settingsRepository.getLanguagePreference().observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Subscriber<FireLanguage>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(final FireLanguage fireLanguage) {
                        if (!isViewAttached()) {
                            return;
                        }
                        String[] langArray = new String[languages.size()];
                        int languageToSelect = 0;

                        for (int i = 0; i < languages.size(); i++) {
                            if (languages.get(i).getLanguageName().equals(fireLanguage.getLanguageName())) {
                                languageToSelect = i;

                            }
                            langArray[i] = languages.get(i).getLanguageName();
                        }
                        listBooksView.showLanguagePopover(langArray, languageToSelect);
                    }
                }));

    }

    private void loadBooksForLanguage(FireLanguage language) {
        listBooksView.showLoading(true);
        addSubscription(bookService.getBooksForLanguage(language).observeOn(ioScheduler).subscribeOn(mainScheduler)
                .subscribe(new Action1<List<FireBookDetails>>() {
                    @Override
                    public void call(final List<FireBookDetails> fireBookList) {
                        if (!isViewAttached()) {
                            return;
                        }
                        listBooksView.showLoading(false);
                        listBooksView.showErrorScreen(false, "", false);
                        listBooksView.showBooks(fireBookList);
                    }
                }));

    }


}
