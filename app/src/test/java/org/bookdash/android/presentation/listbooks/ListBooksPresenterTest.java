package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 15/11/04.
 */
public class ListBooksPresenterTest {

    @Mock
    private BookService bookRepository;
    @Mock
    private ListBooksContract.View listBookView;
    @Mock
    private SettingsRepository settingsRepository;
    /**
     * Item under test
     */
    private ListBooksPresenter listBooksPresenter;
    private List<FireLanguage> LANGUAGES = new ArrayList<>();
    private List<FireBookDetails> BOOKS = new ArrayList<>();
    private FireLanguage ENGLISH_LANGUAGE = new FireLanguage("English", "EN", true, "2");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        listBooksPresenter = new ListBooksPresenter(settingsRepository, bookRepository, Schedulers.immediate(),
                Schedulers.immediate());
        listBooksPresenter.attachView(listBookView);
    }

    @Test
    public void loadBooksForLanguagePreference_LoadsBooks_ShowsBooks() {
        BOOKS.add(new FireBookDetails("Test title", "url", "cover_url", true, "description", ENGLISH_LANGUAGE));
        when(settingsRepository.getLanguagePreference()).thenReturn(Single.just(ENGLISH_LANGUAGE));
        when(bookRepository.getBooksForLanguage(ENGLISH_LANGUAGE)).thenReturn(Observable.just(BOOKS));

        listBooksPresenter.loadBooksForLanguagePreference();

        verify(listBookView).showLoading(true);
        verify(listBookView).showLoading(false);
        verify(listBookView).showBooks(BOOKS);
    }


    @Test
    public void loadBooksForLanguage_ThrowsError_ShowsError() {
        BOOKS.add(new FireBookDetails("Test title", "url", "cover_url", true, "description", ENGLISH_LANGUAGE));
        when(settingsRepository.getLanguagePreference()).thenReturn(Single.just(ENGLISH_LANGUAGE));
        when(bookRepository.getBooksForLanguage(ENGLISH_LANGUAGE))
                .thenReturn(Observable.<List<FireBookDetails>>error(new Exception("Eek!")));

        listBooksPresenter.loadBooksForLanguagePreference();

        verify(listBookView).showLoading(true);
        verify(listBookView).showLoading(false);
        verify(listBookView, never()).showBooks(anyList());
        verify(listBookView).showErrorScreen(true, "Eek!", true);

    }

    @Test
    public void loadBooksForLanguage_LanguageError_ShowsError() {
        when(settingsRepository.getLanguagePreference()).thenReturn(Single.<FireLanguage>error(new Exception("eek!")));

        listBooksPresenter.loadBooksForLanguagePreference();

        verify(listBookView).showLoading(true);
        verify(listBookView).showLoading(false);
        verify(listBookView, never()).showBooks(anyList());
        verify(listBookView).showErrorScreen(true, "eek!", true);
    }


}
