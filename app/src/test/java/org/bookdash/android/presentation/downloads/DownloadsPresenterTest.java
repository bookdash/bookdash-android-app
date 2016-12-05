package org.bookdash.android.presentation.downloads;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DownloadsPresenterTest {
    @Mock
    private BookService bookService;
    @Mock
    private DownloadService downloadService;
    @Mock
    private Analytics analytics;

    private DownloadsPresenter downloadsPresenter;

    @Mock
    private DownloadsContract.View downloadsView;
    private FireLanguage language;
    private List<FireBookDetails> BOOKS = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        downloadsPresenter = new DownloadsPresenter(bookService, downloadService, analytics, Schedulers.immediate(),
                Schedulers.immediate(), Schedulers.immediate());
        language = new FireLanguage("English", "EN", true, "123");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void loadListDownloads_NoDownloads_ShowsNoDownloadsMessage() {
        downloadsPresenter.attachView(downloadsView);

        when(bookService.getDownloadedBooks()).thenReturn(Observable.just(BOOKS));

        downloadsPresenter.loadListDownloads();

        verify(downloadsView).showLoading(true);
        verify(downloadsView).showLoading(false);
        verify(downloadsView).showNoBooksDownloadedMessage();
        verify(downloadsView, never()).showDownloadedBooks(anyList());
    }

    @Test
    public void loadListDownloads_DownloadsAvailable_ShowsListDownloads() {
        downloadsPresenter.attachView(downloadsView);

        FireBookDetails sampleBook = new FireBookDetails("Test Book", "test_url", "cover_url_test", true, "description",
                language, System.currentTimeMillis());
        BOOKS.add(sampleBook);
        when(bookService.getDownloadedBooks()).thenReturn(Observable.just(BOOKS));

        downloadsPresenter.loadListDownloads();

        verify(downloadsView).showLoading(true);
        verify(downloadsView).showLoading(false);
        verify(downloadsView).showDownloadedBooks(BOOKS);
        verify(downloadsView, never()).showNoBooksDownloadedMessage();
    }

    @Test
    public void loadListDownloads_DownloadException_ShowsErrorMessage() {
        downloadsPresenter.attachView(downloadsView);

        when(bookService.getDownloadedBooks())
                .thenReturn(Observable.<List<FireBookDetails>>error(new Exception("Book Exception")));

        downloadsPresenter.loadListDownloads();

        verify(downloadsView).showLoading(true);
        verify(downloadsView).showLoading(false);
        verify(downloadsView, never()).showDownloadedBooks(BOOKS);
        verify(downloadsView, never()).showNoBooksDownloadedMessage();
        verify(downloadsView).showErrorScreen(true, "Book Exception", true);
    }


    @Test
    public void deleteDownload_RemovesDownloadFromList() {
        downloadsPresenter.attachView(downloadsView);
        FireBookDetails sampleBook = new FireBookDetails("Test Book", "test_url", "cover_url_test", true, "description",
                language, System.currentTimeMillis());
        FireBookDetails sampleBook2 = new FireBookDetails("Test Book2", "test_url2", "cover_url_test2", true,
                "description2", language, System.currentTimeMillis());
        BOOKS.add(sampleBook2);
        when(downloadService.deleteDownload(sampleBook)).thenReturn(Observable.just(true));
        when(bookService.getDownloadedBooks()).thenReturn(Observable.just(BOOKS));

        downloadsPresenter.deleteDownload(sampleBook);

        verify(downloadsView, times(2)).showLoading(true);
        verify(downloadsView).showLoading(false);
        verify(downloadsView).showDownloadedBooks(BOOKS);
        verify(downloadService).deleteDownload(sampleBook);
        verify(analytics).trackDeleteBook(sampleBook);
    }

    @Test
    public void deleteDownload_ThrowsError_NotifiesUserOfError() {
        downloadsPresenter.attachView(downloadsView);
        FireBookDetails sampleBook = new FireBookDetails("Test Book", "test_url", "cover_url_test", true, "description",
                language, System.currentTimeMillis());
        String errorMessage = "Failed to delete";
        when(downloadService.deleteDownload(sampleBook))
                .thenReturn(Observable.<Boolean>error(new Exception(errorMessage)));

        downloadsPresenter.deleteDownload(sampleBook);

        verify(downloadsView).showLoading(true);
        verify(downloadsView).showLoading(false);
        verify(downloadsView, never()).showDownloadedBooks(BOOKS);
        verify(downloadService).deleteDownload(sampleBook);
        verify(downloadsView).showSnackBarError(errorMessage);
        verify(analytics).trackDeleteBookFailed(sampleBook, errorMessage);
    }
}