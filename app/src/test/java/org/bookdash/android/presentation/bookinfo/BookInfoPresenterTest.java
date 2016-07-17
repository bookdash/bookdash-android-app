package org.bookdash.android.presentation.bookinfo;

import org.bookdash.android.R;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.domain.model.DownloadProgressItem;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.gson.BookPages;
import org.bookdash.android.domain.model.gson.Page;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 15/11/04.
 */
public class BookInfoPresenterTest {

    private ArrayList<FireContributor> CONTRIBUTORS = new ArrayList<>();
    @Mock
    private BookService bookService;
    @Mock
    private BookInfoContract.View bookInfoView;
    @Mock
    private DownloadService downloadService;

    private BookInfoPresenter bookInfoPresenter;
    @Mock
    private FireBookDetails BOOK_DETAIL;

    @Before
    public void setupListBooksPresenter() {
        MockitoAnnotations.initMocks(this);
        bookInfoPresenter = new BookInfoPresenter(bookService, downloadService, Schedulers.immediate(),
                Schedulers.immediate());
        bookInfoPresenter.attachView(bookInfoView);
        BOOK_DETAIL = new FireBookDetails();

    }

    @Test
    public void loadContributorInformation_ShowsContributors() {
        CONTRIBUTORS = new ArrayList<>();
        FireContributor fireContributor = new FireContributor("Rebecca Franks", "avatar_url", "123");
        CONTRIBUTORS.add(fireContributor);
        when(bookService.getContributorsForBook(BOOK_DETAIL))
                .thenReturn(Observable.<List<FireContributor>>just(CONTRIBUTORS));

        bookInfoPresenter.loadContributorInformation(BOOK_DETAIL);

        bookInfoView.showContributors(CONTRIBUTORS);
    }

    @Test
    public void loadContributorInformation_ErrorThrown_ShowsSnackbarError() {
        when(bookService.getContributorsForBook(BOOK_DETAIL))
                .thenReturn(Observable.<List<FireContributor>>error(new Exception("Error Downloading Books")));

        bookInfoPresenter.loadContributorInformation(BOOK_DETAIL);

        bookInfoView.showSnackBarMessage(R.string.error_getting_contributors);
    }

    @Test
    public void downloadBook_BookNotAvailable_ShowSnackBarMessage() {
        bookInfoPresenter.downloadBook(null);

        verify(bookInfoView).showSnackBarMessage(R.string.book_not_available);
    }

    @Test
    public void downloadBook_BookUrlNull_ShowSnackBarMessage() {
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showSnackBarMessage(R.string.book_not_available);
    }

    @Test
    public void downloadbook_BookAlreadyDownloading_ShowSnackbarErrorMessage() {
        BOOK_DETAIL.setBookUrl("http://dummy.com");
        BOOK_DETAIL.setIsDownloading(true);
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showSnackBarMessage(R.string.book_is_downloading);
    }

    @Test
    public void downloadBook_SendsProgressUpdates_UpdatesView() {
        BOOK_DETAIL.setBookUrl("http://dummy.com/book.zip");
        BOOK_DETAIL.setIsDownloading(false);

        when(downloadService.downloadFile(BOOK_DETAIL)).thenReturn(observableWithProgress(10));
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showDownloadProgress(10);

    }

    Observable<DownloadProgressItem> observableWithProgress(int progress) {
        return Observable.just(new DownloadProgressItem(progress, 100));
    }

    @Test
    public void downloadBook_Completed_UpdatesView() {
        BOOK_DETAIL.setBookUrl("http://dummy.com/book.zip");
        BOOK_DETAIL.setIsDownloading(false);

        //noinspection unchecked
        when(downloadService.downloadFile(BOOK_DETAIL)).thenReturn(completeDownload());
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showDownloadProgress(100);
        verify(bookInfoView).showDownloadFinished();
        verify(bookInfoView).openBook(any(FireBookDetails.class), any(BookPages.class), anyString());
    }
    @Test
    public void downloadBook_CompletedBookPagesNull_ShowsErrorMsg() {
        BOOK_DETAIL.setBookUrl("http://dummy.com/book.zip");
        BOOK_DETAIL.setIsDownloading(false);

        //noinspection unchecked
        when(downloadService.downloadFile(BOOK_DETAIL)).thenReturn(Observable.just(new DownloadProgressItem(100,100)));
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showDownloadProgress(100);
        verify(bookInfoView).showSnackBarMessage(R.string.failed_to_open_book);
        verify(bookInfoView, never()).showDownloadFinished();
        verify(bookInfoView, never()).openBook(any(FireBookDetails.class), any(BookPages.class), anyString());
    }

    Observable<DownloadProgressItem> completeDownload() {
        DownloadProgressItem downloadProgressItem = new DownloadProgressItem(100, 100);
        BookPages bookPages = new BookPages();
        List<Page> listPages = new ArrayList<>();
        listPages.add(new Page());
        bookPages.setPages(listPages);
        downloadProgressItem.setBookPages(bookPages);
        return Observable.just(downloadProgressItem);
    }

    @Test
    public void downloadBook_OnError_ShowsErrorMessage() {
        BOOK_DETAIL.setBookUrl("http://dummy.com/book.zip");
        BOOK_DETAIL.setIsDownloading(false);

        //noinspection unchecked
        when(downloadService.downloadFile(BOOK_DETAIL))
                .thenReturn(Observable.<DownloadProgressItem>error(new Exception("Failed to download")));
        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView, never()).showDownloadProgress(100);
        verify(bookInfoView, never()).showDownloadFinished();
        verify(bookInfoView, never()).openBook(any(FireBookDetails.class), any(BookPages.class), anyString());
        verify(bookInfoView).showSnackBarMessage(R.string.failed_to_download_book, "Failed to download");
    }

    @Test
    public void shareBook_SendsShareEvent() {
        BOOK_DETAIL.setBookTitle("Book Title");
        bookInfoPresenter.shareBookClicked(BOOK_DETAIL);

        verify(bookInfoView).sendShareEvent("Book Title");
    }

    @Test
    public void shareBook_BookNull_ShowsErrorMessage(){
        bookInfoPresenter.shareBookClicked(null);


        verify(bookInfoView).showSnackBarMessage(R.string.book_info_still_loading);
        verify(bookInfoView, never()).sendShareEvent(anyString());
    }
}
