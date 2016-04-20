package org.bookdash.android.presentation.downloads;

import android.test.suitebuilder.annotation.SmallTest;

import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.domain.pojo.BookDetail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;


public class DownloadsPresenterTest {
    @Mock
    private BookDetailRepository bookRepository;

    private DownloadsPresenter downloadsPresenter;

    @Mock
    private DownloadsContract.View downloadsView;
    @Captor
    private ArgumentCaptor<BookDetailRepository.GetBooksForLanguageCallback> bookloadedCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        downloadsPresenter = new DownloadsPresenter(bookRepository, downloadsView);
    }

    @After
    public void tearDown() throws Exception {

    }

    private List<BookDetail> BOOKS = new ArrayList<>();

    @Test
    public void testGetListDownloads_ReturnsDownloads() {

        downloadsPresenter.loadListDownloads(anyString());
        verify(downloadsView).showLoading(true);
        verify(bookRepository).getBooksForLanguage(anyString(), anyBoolean(), bookloadedCaptor.capture());
        bookloadedCaptor.getValue().onBooksLoaded(BOOKS);

        verify(downloadsView).showLoading(false);
        verify(downloadsView).showDownloadedBooks(BOOKS);
    }

    @Test
    public void testGetListDownloads_Error_ReturnsErrorMessage(){
        downloadsPresenter.loadListDownloads(anyString());
        verify(downloadsView).showLoading(true);
        verify(bookRepository).getBooksForLanguage(anyString(), anyBoolean(), bookloadedCaptor.capture());
        bookloadedCaptor.getValue().onBooksLoadError(new Exception("Blah books didn't load") );

        verify(downloadsView).showLoading(false);
        verify(downloadsView).showError("Blah books didn't load");
    }
}