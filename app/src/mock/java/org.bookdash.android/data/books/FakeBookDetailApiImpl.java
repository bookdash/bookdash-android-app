package org.bookdash.android.data.books;

import android.support.annotation.VisibleForTesting;

import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Contributor;
import org.bookdash.android.domain.pojo.Language;
import org.bookdash.android.domain.pojo.gson.BookPages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/14.
 */
public class FakeBookDetailApiImpl implements BookDetailApi {
    private static boolean shouldFailService = false;
    List<BookDetail> bookDetails = new ArrayList<>();
    List<Language> languages = new ArrayList<>();
    List<BookContributor> contributors = new ArrayList<>();

    @VisibleForTesting
    public static void setShouldFailService(boolean shouldFail){
        shouldFailService = shouldFail;
    }

    public FakeBookDetailApiImpl() {
        Language language = new Language("English", "EN", "1en");
        Language language1 = new Language("Zulu", "ZU", "2zu");
        languages.add(language);
        languages.add(language1);

        BookDetail bookDetail = new BookDetail("Searching for Spring",
                "http://bookdash.org/wp-content/uploads/2015/09/searching-for-the-spirit-of-spring_pdf-ebook-20150921_Page_01.jpg", "f4r2gho2h", language);
        BookDetail bookDetail2 = new BookDetail("Why is Nita Upside Down?",
                "http://bookdash.org/wp-content/uploads/2015/09/why-is-nita-upside-down_pdf-ebook_20150920_Page_01.jpg", "12r2gho2h", language);

        BookDetail bookDetailZu = new BookDetail("[ZULU]isipilingi",
                "http://bookdash.org/wp-content/uploads/2015/09/searching-for-the-spirit-of-spring_pdf-ebook-20150921_Page_01.jpg", "f4r2gho2h", language1);
        BookDetail bookDetailZu2 = new BookDetail("[ZULU]kubheke phansi",
                "http://bookdash.org/wp-content/uploads/2015/09/why-is-nita-upside-down_pdf-ebook_20150920_Page_01.jpg", "12r2gho2h", language1);

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
    public void getBooksForLanguages(String language, BookServiceCallback<List<BookDetail>> bookServiceCallback) {
        booksForLanguages(language, bookServiceCallback);
    }

    @Override
    public void searchBooksForLanguages(String searchString, String language, BookServiceCallback<List<BookDetail>> bookServiceCallback) {
        booksForLanguages(language, bookServiceCallback);
    }

    public void booksForLanguages(String language, BookServiceCallback<List<BookDetail>> bookServiceCallback) {
        if (shouldFailService) {
            bookServiceCallback.onError(new Exception("BOOKS LOAD ERROR"));
        } else {
            List<BookDetail> bookDetailsNew = new ArrayList<>();
            for (BookDetail b : bookDetails) {
                if (b.getLanguage().getLanguageName().equals(language)) {
                    bookDetailsNew.add(b);
                }
            }
            bookServiceCallback.onLoaded(bookDetailsNew);
        }
    }

    @Override
    public void getDownloadedBooks(BookServiceCallback<List<BookDetail>> bookServiceCallback) {
        //TODO
    }

    @Override
    public void getBookDetail(String bookDetailId, BookServiceCallback<BookDetail> bookServiceCallback) {
        if (shouldFailService) {
            bookServiceCallback.onError(new Exception("BOOK DETAIL ERROR"));
        } else {
            for (BookDetail b : bookDetails) {
                if (b.getObjectId().equals(bookDetailId)) {
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
    public void getLanguages(BookServiceCallback<List<Language>> languagesCallback) {
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
