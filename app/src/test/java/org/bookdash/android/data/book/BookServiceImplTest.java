package org.bookdash.android.data.book;

import org.bookdash.android.data.database.firebase.BookDatabase;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BookServiceImplTest {
    @Mock
    BookDatabase bookDatabase;

    BookServiceImpl bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        bookService = new BookServiceImpl(bookDatabase);
    }

    @Test
    public void testGetLanguages() throws Exception {

        FireLanguage fireLanguage = new FireLanguage("English", "EN", true, "id123");
        ArrayList<FireLanguage> fireLanguages = new ArrayList<>();
        fireLanguages.add(fireLanguage);
        when(bookDatabase.getLanguages()).thenReturn(Observable.<List<FireLanguage>>just(fireLanguages));

        TestSubscriber<List<FireLanguage>> testSubscriber = new TestSubscriber<>();
        bookService.getLanguages().subscribe(testSubscriber);

        verify(bookDatabase).getLanguages();
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();

        final List<List<FireLanguage>> result = testSubscriber.getOnNextEvents();
        assertEquals("English", result.get(0).get(0).getLanguageName());
    }

    @Test
    public void testGetBooksForLanguage() throws Exception {

        FireLanguage fireLanguage = new FireLanguage("English", "en", true, "123");

        FireBookDetails fireBookDetails = new FireBookDetails("Book Title", "url", "cover_url", true,
                "test description", fireLanguage, System.currentTimeMillis());
        ArrayList<FireBookDetails> bookDetails = new ArrayList<>();
        bookDetails.add(fireBookDetails);
        doReturn(Observable.<List<FireBookDetails>>just(bookDetails)).when(bookDatabase).getBooksByLanguage(fireLanguage);

        TestSubscriber<List<FireBookDetails>> testSubscriber = new TestSubscriber<>();
        bookService.getBooksForLanguage(fireLanguage).subscribe(testSubscriber);

        verify(bookDatabase).getBooksByLanguage(fireLanguage);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();

        final List<List<FireBookDetails>> result = testSubscriber.getOnNextEvents();
        assertEquals("Book Title", result.get(0).get(0).getBookTitle());
    }
}