package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookDetailRepository {


    void getDownloadedBooks(GetBooksForLanguageCallback getBooksForLanguageCallback);


    void deleteBook(FireBookDetails bookDetail, @NonNull DeleteBookCallBack deleteBookCallBack);

    interface GetBooksForLanguageCallback {
        void onBooksLoaded(List<FireBookDetails> books);

        void onBooksLoadError(Exception e);
    }

    interface GetBookDetailCallback {
        void onBookDetailLoaded(FireBookDetails bookDetail);

        void onBookDetailLoadError(Exception e);
    }


    interface DeleteBookCallBack {
        void onBookDeleted(FireBookDetails bookDetail);

        void onBookDeleteFailed(Exception e);

    }
}
