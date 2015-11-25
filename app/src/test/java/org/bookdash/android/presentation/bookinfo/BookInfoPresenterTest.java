package org.bookdash.android.presentation.bookinfo;

import com.parse.ParseObject;

import org.bookdash.android.R;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Language;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

/**
 * @author rebeccafranks
 * @since 15/11/04.
 */
public class BookInfoPresenterTest {

    @Mock
    private BookDetailRepository bookRepository;

    @Mock
    private BookInfoContract.View bookInfoView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BookDetailRepository.GetBookDetailCallback> bookDetailCallbackArgumentCaptor;

    private BookInfoPresenter bookInfoPresenter;


    @Before
    public void setupListBooksPresenter() {
        MockitoAnnotations.initMocks(this);
        ParseObject.registerSubclass(BookDetail.class);
        ParseObject.registerSubclass(Language.class);
        bookInfoPresenter = new BookInfoPresenter(null, bookInfoView, bookRepository);
        BOOK_DETAIL = new BookDetail("Test book", "http://test.com/image.png", "1234", new Language("English", "EN", "123"));

    }

    @Mock
    private BookDetail BOOK_DETAIL;

    @Test
    public void loadBookDetails_BookLoads_SetBinding() {
        bookInfoPresenter.loadBookInformation("1234");

        verify(bookRepository).getBookDetail(eq("1234"), bookDetailCallbackArgumentCaptor.capture());
        bookDetailCallbackArgumentCaptor.getValue().onBookDetailLoaded(BOOK_DETAIL);

        verify(bookInfoView).setBookInfoBinding(BOOK_DETAIL);
        verify(bookInfoView).showBookDetailView();
        verify(bookInfoView).setToolbarTitle("Test book");

    }

    @Test
    public void loadBookDetails_BookDownloadError_ShowErrorMessage() {
        bookInfoPresenter.loadBookInformation("1234");

        verify(bookRepository).getBookDetail(eq("1234"), bookDetailCallbackArgumentCaptor.capture());
        bookDetailCallbackArgumentCaptor.getValue().onBookDetailLoadError(new Exception("Error loading book"));

        verify(bookInfoView).showError("Error loading book");

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

   /* @Test
    public void downloadBook_BookAlreadyDownloading_ShowSnackBarMessage() {
        when(BOOK_DETAIL.isDownloadedAlready()).thenReturn(true);
        BOOK_DETAIL.put(BookDetail.BOOK_DOWNLOAD_FILE_COL, new ParseFile("hello".getBytes()));

        bookInfoPresenter.downloadBook(BOOK_DETAIL);

        verify(bookInfoView).showSnackBarMessage(R.string.book_is_downloading);
    }*/
}
