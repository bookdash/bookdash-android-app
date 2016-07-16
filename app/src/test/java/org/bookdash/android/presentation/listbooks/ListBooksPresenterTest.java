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

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 15/11/04.
 */
public class ListBooksPresenterTest {

    FireLanguage fireLanguage = new FireLanguage("English", "EN", true, "123");
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

    @Before
    public void setupListBooksPresenter() {
        MockitoAnnotations.initMocks(this);
        listBooksPresenter = new ListBooksPresenter(listBookView, settingsRepository, bookRepository,
                Schedulers.immediate(), Schedulers.immediate());
        listBooksPresenter.attachView(listBookView);
    }

   @Test
    public void loadLanguages_setsListLanuages(){

   }

}
