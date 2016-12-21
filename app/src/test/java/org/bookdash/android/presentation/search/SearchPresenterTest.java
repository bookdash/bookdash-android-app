package org.bookdash.android.presentation.search;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 2016/12/11
 */

public class SearchPresenterTest {

    @Mock
    SearchContract.View view;
    @Mock
    BookService bookRepository;
    @Mock
    Analytics analytics;

    private SearchContract.Presenter presenter;

    private FireLanguage ENGLISH_LANGUAGE = new FireLanguage("English", "EN", true, "2");


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SearchPresenter(bookRepository, analytics, Schedulers.immediate(), Schedulers.immediate());
        presenter.attachView(view);
    }

    @Test
    public void search_ValidSearchTerm_ReturnsValidResults() {
        when(bookRepository.searchBooks(anyString())).thenReturn(Observable.just(getFakeBooks()));

        String searchTerm = "hug";
        presenter.search(searchTerm);

        verify(analytics).trackSearchBooks(searchTerm);
        verify(view).showSearchResults(anyListOf(FireBookDetails.class));
        verify(bookRepository).searchBooks(searchTerm);
        verify(analytics).trackSearchBooksSuccess(searchTerm, getFakeBooks().size());
        verify(view, never()).showErrorMessage(anyString());
    }

    private List<FireBookDetails> getFakeBooks() {
        List<FireBookDetails> books = new ArrayList<>();
        books.add(new FireBookDetails("Test title", "url", "cover_url", true, "description", ENGLISH_LANGUAGE,
                System.currentTimeMillis()));
        return books;
    }


    @Test
    public void search_NoInternet_ShowsNoInternetMsg() {
        when(bookRepository.searchBooks(anyString()))
                .thenReturn(Observable.<List<FireBookDetails>>error(new IOException("No Internet")));

        String searchTerm = "hug";
        presenter.search(searchTerm);

        verify(view).showLoading();
        verify(analytics).trackSearchBooks(searchTerm);
        verify(view, never()).showSearchResults(anyListOf(FireBookDetails.class));
        verify(bookRepository).searchBooks(searchTerm);
        verify(view).showNoInternetMessage();
        verify(view).hideLoading();
    }

    @Test
    public void search_NoValidMatches_ShowsNoResultsMsg() {
        when(bookRepository.searchBooks(anyString()))
                .thenReturn(Observable.<List<FireBookDetails>>just(new ArrayList<FireBookDetails>()));

        String searchTerm = "hug";
        presenter.search(searchTerm);

        verify(view).showLoading();
        verify(view, never()).showSearchResults(anyListOf(FireBookDetails.class));
        verify(bookRepository).searchBooks(searchTerm);
        verify(analytics).trackSearchBooks(searchTerm);
        verify(analytics).trackSearchBooksNoResults(searchTerm);
        verify(view).showNoResultsMessage();
        verify(view).hideLoading();
    }

    @Test
    public void search_NoSearchTerm_ThrowsError() {
        final String errorMessage = "This is awkward.";
        when(bookRepository.searchBooks(anyString()))
                .thenReturn(Observable.<List<FireBookDetails>>error(new Exception(errorMessage)));

        String searchTerm = "hug";
        presenter.search(searchTerm);

        verify(view).showLoading();
        verify(view, never()).showSearchResults(anyListOf(FireBookDetails.class));
        verify(bookRepository).searchBooks(searchTerm);
        verify(analytics).trackSearchBooks(searchTerm);
        verify(analytics).trackSearchError(searchTerm, errorMessage);

        verify(view).showErrorMessage(errorMessage);
        verify(view).hideLoading();
    }
}
