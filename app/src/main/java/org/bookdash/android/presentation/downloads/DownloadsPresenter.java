package org.bookdash.android.presentation.downloads;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

class DownloadsPresenter extends BasePresenter<DownloadsContract.View> implements DownloadsContract.Presenter {
    private final BookService bookService;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    DownloadsPresenter(BookService bookService, Scheduler io, Scheduler main) {
        this.bookService = bookService;
        this.ioScheduler = io;
        this.mainScheduler = main;
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
        /*bookRepository.deleteBook(bookDetail, new BookDetailRepository.DeleteBookCallBack() {

            @Override
            public void onBookDeleted(FireBookDetails bookDetail) {
                loadListDownloads();
            }

            @Override
            public void onBookDeleteFailed(Exception e) {
                view.showSnackBarError(e.getMessage());
            }
        });*/
    }
}
