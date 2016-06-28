package org.bookdash.android.data.books;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookDetailApi {

    void getBooksForLanguages(String language, BookServiceCallback<List<FireBookDetails>> bookServiceCallback);

    void getDownloadedBooks(BookServiceCallback<List<FireBookDetails>> bookServiceCallback);

    interface BookServiceCallback<T> {
        void onLoaded(T result);

        void onError(Exception error);
    }

    interface BookServiceProgressCallback {
        void onProgressChanged(int progress);
    }

   // void getBookDetail(String bookDetailId, BookServiceCallback<FireBookDetails> bookServiceCallback);

    void getContributorsForBook(FireBookDetails bookId, BookServiceCallback<List<FireContributor>> contributorsCallback);


    void getLanguages(BookServiceCallback<List<FireLanguage>> languagesCallback);

    void downloadBook(FireBookDetails bookDetail, BookServiceCallback<BookPages> downloadBookCallback, BookServiceProgressCallback bookServiceProgressCallback);

    void deleteBook(FireBookDetails bookDetail, BookServiceCallback<Boolean> deleteBook);
}
