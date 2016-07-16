package org.bookdash.android.presentation.bookinfo;

import org.bookdash.android.R;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;

/**
 * @author rebeccafranks
 * @since 15/11/04.
 */
public class BookInfoPresenterTest {

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
    public void loadBookDetails_BookDownloadError_ShowErrorMessage() {

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


}
