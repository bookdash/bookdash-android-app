package org.bookdash.android.presentation.listbooks;

import org.bookdash.android.R;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
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
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BookDetailRepository.GetLanguagesCallback> languagesCallbackArgumentCaptor;
    @Captor
    private ArgumentCaptor<BookDetailRepository.GetBooksForLanguageCallback> booksForLanguageCallbackArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> languagePreferenceCaptor;
    /**
     * Item under test
     */
    private ListBooksPresenter listBooksPresenter;

    @Before
    public void setupListBooksPresenter() {
        MockitoAnnotations.initMocks(this);
        listBooksPresenter = new ListBooksPresenter(listBookView, settingsRepository, bookRepository, Schedulers.immediate(), Schedulers.immediate());
    }
    FireLanguage fireLanguage = new FireLanguage("English", "EN", true, "123");
    @Test
    public void loadBooksSuccessfulLoadIntoView() {
        when(settingsRepository.getLanguagePreference()).thenReturn(Single.just(fireLanguage));

        listBooksPresenter.loadBooksForLanguagePreference();
      /*  verify(bookRepository).getBooksForLanguage(fireLanguage, booksForLanguageCallbackArgumentCaptor.capture());
        booksForLanguageCallbackArgumentCaptor.getValue().onBooksLoaded(BOOKS);

        verify(listBookView).showBooks(BOOKS);
        verify(listBookView).showLoading(false);*/
    }

    @Test
    public void loadBooksLoadErrorShowErrorRetryScreen() {
        when(settingsRepository.getLanguagePreference()).thenReturn(Single.just(fireLanguage));

        listBooksPresenter.loadBooksForLanguagePreference();

       /* verify(bookRepository).getBooksForLanguage(eq("EN"), booksForLanguageCallbackArgumentCaptor.capture());
        booksForLanguageCallbackArgumentCaptor.getValue().onBooksLoadError(new Exception("WHOOPS"));

        verify(listBookView).showErrorScreen(true, "WHOOPS", true);
        verify(listBookView).showLoading(false);*/

    }

    private List<FireLanguage> LANGUAGES= new ArrayList<>();
    private List<FireBookDetails> BOOKS = new ArrayList<>();

    @Test
    public void loadLanguagesCorrectlyNotifyView() {
        listBooksPresenter.loadLanguages();

      /*  verify(bookRepository).getLanguages(languagesCallbackArgumentCaptor.capture());
        languagesCallbackArgumentCaptor.getValue().onLanguagesLoaded(LANGUAGES);
*/
    }

    @Test
    public void loadLanguagesIncorrectlyErrorShownToUser() {
        listBooksPresenter.loadLanguages();

    /*    verify(bookRepository).getLanguages(languagesCallbackArgumentCaptor.capture());
        languagesCallbackArgumentCaptor.getValue().onLanguagesLoadError(new Exception("Oops"));

        verify(listBookView).showSnackBarError(R.string.error_loading_languages);
*/    }


}
