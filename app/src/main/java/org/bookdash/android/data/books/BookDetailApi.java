package org.bookdash.android.data.books;

import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Language;
import org.bookdash.android.domain.pojo.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookDetailApi {

    void getBooksForLanguages(String language, BookServiceCallback<List<BookDetail>> bookServiceCallback);

    interface BookServiceCallback<T> {
        void onLoaded(T result);

        void onError(Exception error);
    }
    interface BookServiceProgressCallback{
        void onProgressChanged(int progress);
    }

    void getBookDetail(String bookDetailId, BookServiceCallback<BookDetail> bookServiceCallback);

    void getContributorsForBook(Book bookId, BookServiceCallback<List<BookContributor>> contributorsCallback);


    void getLanguages(BookServiceCallback<List<Language>> languagesCallback);

    void downloadBook(BookDetail bookDetail, BookServiceCallback<BookPages> downloadBookCallback, BookServiceProgressCallback bookServiceProgressCallback);

}
