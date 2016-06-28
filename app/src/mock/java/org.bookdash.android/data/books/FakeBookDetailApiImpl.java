package org.bookdash.android.data.books;

import android.support.annotation.VisibleForTesting;

import org.bookdash.android.domain.model.Book;
import org.bookdash.android.domain.model.BookContributor;
import org.bookdash.android.domain.model.Contributor;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/14.
 */
public class FakeBookDetailApiImpl implements BookDetailApi {
    private static boolean shouldFailService = false;
    List<FireBookDetails> bookDetails = new ArrayList<>();
    List<FireLanguage> languages = new ArrayList<>();
    List<BookContributor> contributors = new ArrayList<>();

    @VisibleForTesting
    public static void setShouldFailService(boolean shouldFail) {
        shouldFailService = shouldFail;
    }

    public FakeBookDetailApiImpl() {
        FireLanguage language = new FireLanguage("English", "EN", true);
        FireLanguage language1 = new FireLanguage("Zulu", "ZU", true);
        languages.add(language);
        languages.add(language1);

        FireBookDetails bookDetail = new FireBookDetails("Searching for Spring", "bookurl",
                "http://bookdash.org/wp-content/uploads/2015/09/searching-for-the-spirit-of-spring_pdf-ebook-20150921_Page_01.jpg", true, "f4r2gho2h", "about a book");
        FireBookDetails bookDetail2 = new FireBookDetails("Why is Nita Upside Down?", "bookurl",
                "http://bookdash.org/wp-content/uploads/2015/09/why-is-nita-upside-down_pdf-ebook_20150920_Page_01.jpg", true, "12r2gho2h", "about");

        FireBookDetails bookDetailZu = new FireBookDetails("[ZULU]isipilingi", "url",
                "http://bookdash.org/wp-content/uploads/2015/09/searching-for-the-spirit-of-spring_pdf-ebook-20150921_Page_01.jpg", true, "f4r2gho2h", "about");
        FireBookDetails bookDetailZu2 = new FireBookDetails("[ZULU]kubheke phansi", "url",
                "http://bookdash.org/wp-content/uploads/2015/09/why-is-nita-upside-down_pdf-ebook_20150920_Page_01.jpg", true, "12r2gho2h", "about");

        bookDetails.add(bookDetail);
        bookDetails.add(bookDetail2);
        bookDetails.add(bookDetailZu);
        bookDetails.add(bookDetailZu2);

        Contributor contributor = new Contributor("Rebecca Franks", "Developer");
        Contributor contributor2 = new Contributor("Johan Smith", "Artist");

        BookContributor bookContributor = new BookContributor(contributor);
        BookContributor bookContributor2 = new BookContributor(contributor2);
        contributors.add(bookContributor);
        contributors.add(bookContributor2);

    }

    @Override
    public void getBooksForLanguages(String language, BookServiceCallback<List<FireBookDetails>> bookServiceCallback) {
        if (shouldFailService) {
            bookServiceCallback.onError(new Exception("BOOKS LOAD ERROR"));
        } else {
            List<FireBookDetails> bookDetailsNew = new ArrayList<>();
            for (FireBookDetails b : bookDetails) {
                //   if (b.getLanguage().getLanguageName().equals(language)) {
                bookDetailsNew.add(b);
                //   } //TODO
            }
            bookServiceCallback.onLoaded(bookDetailsNew);
        }
    }

    @Override
    public void getDownloadedBooks(BookServiceCallback<List<FireBookDetails>> bookServiceCallback) {
        //TODO
    }

    @Override
    public void getBookDetail(String bookDetailId, BookServiceCallback<FireBookDetails> bookServiceCallback) {
        if (shouldFailService) {
            bookServiceCallback.onError(new Exception("BOOK DETAIL ERROR"));
        } else {
            for (FireBookDetails b : bookDetails) {
                if (b.getId().equals(bookDetailId)) {
                    bookServiceCallback.onLoaded(b);
                    return;
                }
            }
        }

    }

    @Override
    public void getContributorsForBook(Book bookId, BookServiceCallback<List<BookContributor>> contributorsCallback) {
        if (shouldFailService) {
            contributorsCallback.onError(new Exception("CONTRIBUTORS ERROR"));
        } else {
            contributorsCallback.onLoaded(contributors);
        }
    }

    @Override
    public void getLanguages(BookServiceCallback<List<FireLanguage>> languagesCallback) {
        if (shouldFailService) {
            languagesCallback.onError(new Exception("LANGUAGES ERROR"));
        } else {
            languagesCallback.onLoaded(languages);
        }
    }

    @Override
    public void downloadBook(BookDetail bookDetail, BookServiceCallback<BookPages> downloadBookCallback, BookServiceProgressCallback bookServiceProgressCallback) {

    }

    @Override
    public void deleteBook(BookDetail bookDetail, BookServiceCallback<Boolean> deleteBook) {

    }
}
