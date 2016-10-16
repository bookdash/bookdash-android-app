package org.bookdash.android.presentation.bookinfo;

import android.support.annotation.NonNull;

import org.bookdash.android.R;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.domain.model.DownloadProgressItem;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
class BookInfoPresenter extends BasePresenter<BookInfoContract.View> implements BookInfoContract.Presenter {

    private static final String TAG = "BookInfoPresenter";
    private final BookService bookService;
    private final DownloadService downloadService;
    private final Scheduler ioScheduler, mainScheduler;

    BookInfoPresenter(@NonNull BookService bookService, @NonNull DownloadService downloadService, Scheduler ioScheduler,
                      Scheduler mainScheduler) {
        this.bookService = bookService;
        this.downloadService = downloadService;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void loadContributorInformation(FireBookDetails bookDetailId) {
        addSubscription(
                bookService.getContributorsForBook(bookDetailId).subscribeOn(ioScheduler).observeOn(mainScheduler)
                        .subscribe(new Subscriber<List<FireContributor>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(final Throwable e) {
                                getView().showSnackBarMessage(R.string.error_getting_contributors);
                            }

                            @Override
                            public void onNext(final List<FireContributor> fireContributors) {
                                getView().showContributors(fireContributors);
                            }
                        }));
    }

    @Override
    public void downloadBook(final FireBookDetails bookInfo) {
        if (bookInfo == null || bookInfo.getBookUrl() == null) {
            getView().showSnackBarMessage(R.string.book_not_available);
            return;
        }
        if (bookInfo.isDownloading()) {
            getView().showSnackBarMessage(R.string.book_is_downloading);
            return;
        }
        bookInfo.setIsDownloading(true);
        addSubscription(downloadService.downloadFile(bookInfo).subscribeOn(ioScheduler).observeOn(mainScheduler)
                .subscribe(new Subscriber<DownloadProgressItem>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        bookInfo.setIsDownloading(false);
                        if (e != null) {
                            getView().showSnackBarMessage(R.string.failed_to_download_book, e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(DownloadProgressItem downloadProgressItem) {
                        getView().showDownloadProgress(downloadProgressItem.getDownloadProgress());
                        if (downloadProgressItem.isComplete()) {
                            if (downloadProgressItem.getBookPages() == null) {
                                getView().showSnackBarMessage(R.string.failed_to_open_book);
                                return;
                            }
                            getView().showDownloadFinished();
                            bookInfo.setIsDownloading(false);
                            getView().openBook(bookInfo, downloadProgressItem.getBookPages(),
                                    bookInfo.getFolderLocation());
                        }
                    }
                }));
    }

    @Override
    public void shareBookClicked(FireBookDetails bookInfo) {
        if (bookInfo == null) {
            getView().showSnackBarMessage(R.string.book_info_still_loading);
            return;
        }
        getView().sendShareEvent(bookInfo.getBookTitle());
    }
}


