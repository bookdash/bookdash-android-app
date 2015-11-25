package org.bookdash.android.data.books;

import org.bookdash.android.domain.pojo.BookDetail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 15/11/14.
 */
public class BookDetailRepositoryImplTest {

    @Mock
    private BookDetailApi bookDetailApi;

    private BookDetailRepositoryImpl bookDetailRepository;

    @Mock
    private BookDetailRepository.GetBooksForLanguageCallback booksForLanguageCallback;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        bookDetailRepository = new BookDetailRepositoryImpl(bookDetailApi);
    }
    private List<BookDetail> BOOKS;
    @Test
    public void getBooksForLanguageApiSuccess(){
        bookDetailRepository.getBooksForLanguage("EN", booksForLanguageCallback);

    //    verify(bookDetailApi).getBooksForLanguages("EN", any(BookDetailApi.BookServiceCallback.class));
    }

    @Test
    public void getBooksForLanguageApiFailure(){

    }
}
