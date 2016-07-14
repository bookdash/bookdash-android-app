package org.bookdash.android.presentation.bookinfo;

import android.support.annotation.NonNull;
import android.util.Log;

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
import rx.functions.Action1;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
class BookInfoPresenter extends BasePresenter<BookInfoContract.View> implements BookInfoContract.Presenter {

    private final BookService bookService;
    private final DownloadService downloadService;
    private static final String TAG = "BookInfoPresenter";
    private final Scheduler ioScheduler, mainScheduler;

    BookInfoPresenter(
            @NonNull BookService bookService, @NonNull DownloadService downloadService, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.bookService = bookService;
        this.downloadService = downloadService;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void loadContributorInformation(FireBookDetails bookDetailId) {
        addSubscription(bookService.getContributorsForBook(bookDetailId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Action1<List<FireContributor>>() {
                    @Override
                    public void call(final List<FireContributor> fireContributors) {

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

        addSubscription(downloadService.downloadFile(bookInfo)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Subscriber<DownloadProgressItem>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() called with: e = [" + e + "]", e);
                        if (e != null) {
                            getView().showSnackBarMessage(R.string.failed_to_download_book);
                        }

                    }

                    @Override
                    public void onNext(DownloadProgressItem downloadProgressItem) {
                        Log.d(TAG, "onNext() called with: downloadProgressItem = [" + downloadProgressItem + "]");
                        getView().showDownloadProgress(downloadProgressItem.getDownloadProgress());
                        if (downloadProgressItem.isComplete()) {
                            getView().openBook(bookInfo, downloadProgressItem.getBookPages(), bookInfo.getFolderLocation());
                        }
                    }
                }));
       /*
        if (bookInfo.isDownloading()) {
            booksView.showSnackBarMessage(R.string.book_is_downloading);
            return;
        }
        bookInfo.setIsDownloading(true);


        bookDetailRepository.downloadBook(bookInfo, new BookDetailRepository.GetBookPagesCallback() {
            @Override
            public void onBookPagesLoaded(BookPages bookPages) {
                if (bookPages == null) {
                    booksView.showSnackBarMessage(R.string.failed_to_open_book);
                    return;
                }
                bookInfo.setIsDownloading(false);
                booksView.openBook(bookInfo, bookPages, bookInfo.getFolderLocation());
            }

            @Override
            public void onBookPagesLoadError(Exception e) {
                bookInfo.setIsDownloading(false);
                if (e != null) {
                    booksView.showSnackBarMessage(R.string.failed_to_download_book);
                }
            }

            @Override
            public void onBookPagesDownloadProgressUpdate(int progress) {
                booksView.showDownloadProgress(progress);
            }
        });*/

    }

    @Override
    public void shareBookClicked(FireBookDetails bookInfo) {
        if (bookInfo == null) {
            //  getView().showError(context.getString(R.string.book_info_still_loading));
            return;
        }
        getView().sendShareEvent(bookInfo.getBookTitle());
    }
}


