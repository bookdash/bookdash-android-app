package org.bookdash.android.data.books;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

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
    @Test
    public void getBooksForLanguageApiSuccess(){
       // bookDetailRepository.("EN", booksForLanguageCallback);

    //    verify(bookDetailApi).getBooksForLanguages("EN", any(BookDetailApi.BookServiceCallback.class));
    }

    @Test
    public void getBooksForLanguageApiFailure(){

    }
}
