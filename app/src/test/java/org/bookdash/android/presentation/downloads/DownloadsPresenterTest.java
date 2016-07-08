package org.bookdash.android.presentation.downloads;

import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


public class DownloadsPresenterTest {
    @Mock
    private BookDetailRepository bookRepository;

    private DownloadsPresenter downloadsPresenter;

    @Mock
    private DownloadsContract.View downloadsView;
    @Captor
    private ArgumentCaptor<BookDetailRepository.GetBooksForLanguageCallback> bookloadedCaptor;
    @Captor
    private ArgumentCaptor<BookDetailRepository.DeleteBookCallBack> deleteBookCallBackArgumentCaptor;
    private FireLanguage language;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        downloadsPresenter = new DownloadsPresenter(bookRepository, downloadsView);
        language = new FireLanguage("English", "EN", true, "123");
    }

    @After
    public void tearDown() throws Exception {

    }

    private List<FireBookDetails> BOOKS = new ArrayList<>();

    @Test
    public void testGetListDownloads_ReturnsDownloads() {

        downloadsPresenter.loadListDownloads();
        verify(downloadsView).showLoading(true);
        verify(bookRepository).getDownloadedBooks(bookloadedCaptor.capture());
        BOOKS.add(new FireBookDetails("test title", "http://test.com","urlcover", true,  language.getId(), "description"));
        bookloadedCaptor.getValue().onBooksLoaded(BOOKS);

        verify(downloadsView).showLoading(false);
        verify(downloadsView).showDownloadedBooks(BOOKS);
    }

    @Test
    public void testGetListDownloads_Error_ReturnsErrorMessage() {
        downloadsPresenter.loadListDownloads();
        verify(downloadsView).showLoading(true);
        verify(bookRepository).getDownloadedBooks(bookloadedCaptor.capture());
        bookloadedCaptor.getValue().onBooksLoadError(new Exception("Blah books didn't load"));

        verify(downloadsView).showLoading(false);
        verify(downloadsView).showErrorScreen(true, "Blah books didn't load", true);
    }

    @Test
    public void testDeleteDownload_RemovesDownload() {
        FireBookDetails bookDetail = new FireBookDetails("FAKE BOOK test title", "http://test.com","urlcover", true,  language.getId(), "description");
        downloadsPresenter.deleteDownload(bookDetail);

        verify(bookRepository).deleteBook(any(FireBookDetails.class), deleteBookCallBackArgumentCaptor.capture());
        deleteBookCallBackArgumentCaptor.getValue().onBookDeleted(bookDetail);

        verify(bookRepository).getDownloadedBooks(bookloadedCaptor.capture());
        bookloadedCaptor.getValue().onBooksLoaded(BOOKS);

        verify(downloadsView).showNoBooksDownloadedMessage();
    }

    @Test
    public void testDeleteDownload_RemovesDownloadKeepsOthers() {
        FireBookDetails bookDetail = new FireBookDetails("FAKE BOOK test title", "http://test.com","urlcover", true,  language.getId(), "description");
        downloadsPresenter.deleteDownload(bookDetail);

        verify(bookRepository).deleteBook(any(FireBookDetails.class), deleteBookCallBackArgumentCaptor.capture());
        deleteBookCallBackArgumentCaptor.getValue().onBookDeleted(bookDetail);
        BOOKS.add(new FireBookDetails("FAKE BOOK 2 test title", "http://test.com","urlcover", true,  language.getId(), "description"));
        verify(bookRepository).getDownloadedBooks(bookloadedCaptor.capture());
        bookloadedCaptor.getValue().onBooksLoaded(BOOKS);

        verify(downloadsView).showDownloadedBooks(BOOKS);
    }
}