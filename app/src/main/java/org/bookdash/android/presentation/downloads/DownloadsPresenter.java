package org.bookdash.android.presentation.downloads;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

class DownloadsPresenter extends BasePresenter<DownloadsContract.View> implements DownloadsContract.Presenter {
    private final BookService bookService;
    private final Scheduler ioScheduler, mainScheduler, computationScheduler;
    private final DownloadService downloadService;

    DownloadsPresenter(BookService bookService, DownloadService downloadService, Scheduler io, Scheduler main, Scheduler compScheduler) {
        this.bookService = bookService;
        this.ioScheduler = io;
        this.mainScheduler = main;
        this.computationScheduler = compScheduler;
        this.downloadService = downloadService;
    }

    public void loadListDownloads() {
        getView().showLoading(true);
        addSubscription(bookService.getDownloadedBooks().subscribeOn(ioScheduler).observeOn(mainScheduler).subscribe(new Subscriber<List<FireBookDetails>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorScreen(true, e.getMessage(), true);
                getView().showLoading(false);
            }

            @Override
            public void onNext(List<FireBookDetails> books) {
                getView().showLoading(false);
                if (books.isEmpty() || books.size() == 0) {
                    getView().showNoBooksDownloadedMessage();
                    return;
                }
                getView().showDownloadedBooks(books);
            }
        }));

    }

    @Override
    public void deleteDownload(FireBookDetails bookDetail) {
        getView().showLoading(true);
        addSubscription(downloadService.deleteDownload(bookDetail).subscribeOn(computationScheduler).observeOn(mainScheduler).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showLoading(false);
                getView().showSnackBarError(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                loadListDownloads();
            }
        }));

    }
}
