package org.bookdash.android.presentation.search;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.BasePresenter;

import java.io.IOException;
import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * @author rebeccafranks
 * @since 2016/12/11
 */
class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    private final Scheduler ioScheduler, mainScheduler;
    private final Analytics analytics;
    private BookService bookService;

    SearchPresenter(final BookService bookService, final Analytics analytics, Scheduler mainScheduler,
                    Scheduler ioScheduler) {
        this.bookService = bookService;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.analytics = analytics;
    }

    @Override
    public void search(final String searchTerm) {
        getView().showLoading();
        analytics.trackSearchBooks(searchTerm);
        addSubscription(bookService.searchBooks(searchTerm).subscribeOn(ioScheduler).observeOn(mainScheduler)
                .subscribe(new Subscriber<List<FireBookDetails>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(final Throwable e) {
                        getView().hideLoading();
                        if (e instanceof IOException) {
                            getView().showNoInternetMessage();
                            return;
                        }
                        analytics.trackSearchError(searchTerm, e.getMessage());
                        getView().showErrorMessage(e.getMessage());
                        getView().showRetryButton();
                    }

                    @Override
                    public void onNext(final List<FireBookDetails> fireBookDetails) {
                        getView().hideLoading();
                        getView().hideRetryButton();
                        if (fireBookDetails == null || fireBookDetails.size() == 0) {
                            analytics.trackSearchBooksNoResults(searchTerm);
                            getView().showNoResultsMessage();

                            return;
                        }
                        analytics.trackSearchBooksSuccess(searchTerm, fireBookDetails.size());
                        getView().showSearchResults(fireBookDetails);
                    }
                }));
    }
}
