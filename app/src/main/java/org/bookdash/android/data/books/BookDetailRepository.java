package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.firebase.FireBookDetails;
import org.bookdash.android.domain.pojo.firebase.FireLanguage;
import org.bookdash.android.domain.pojo.gson.BookPages;

import java.util.List;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public interface BookDetailRepository {


    void getBooksForLanguage(@NonNull String language, @NonNull GetBooksForLanguageCallback booksForLanguageCallback);

    void getDownloadedBooks(GetBooksForLanguageCallback getBooksForLanguageCallback);

    interface GetBooksForLanguageCallback {
        void onBooksLoaded(List<FireBookDetails> books);

        void onBooksLoadError(Exception e);
    }

    interface GetBookDetailCallback {
        void onBookDetailLoaded(FireBookDetails bookDetail);

        void onBookDetailLoadError(Exception e);
    }

    interface GetContributorsCallback {
        void onContributorsLoaded(List<BookContributor> contributors);

        void onContributorsLoadError(Exception e);
    }


    interface GetLanguagesCallback {
        void onLanguagesLoaded(List<FireLanguage> languages);

        void onLanguagesLoadError(Exception e);
    }

    interface GetBookPagesCallback {
        void onBookPagesLoaded(BookPages bookPages);

        void onBookPagesLoadError(Exception e);

        void onBookPagesDownloadProgressUpdate(int progress);
    }

    void getBookDetail(String bookDetailId, @NonNull GetBookDetailCallback bookDetailCallback);

    void getContributorsForBook(Book bookDetailId, @NonNull GetContributorsCallback contributorsCallback);

    void getLanguages(@NonNull GetLanguagesCallback languagesCallback);

    void downloadBook(FireBookDetails bookDetail, @NonNull GetBookPagesCallback bookPagesCallback);

    void deleteBook(FireBookDetails bookDetail, @NonNull DeleteBookCallBack deleteBookCallBack);

    interface DeleteBookCallBack {
        void onBookDeleted(FireBookDetails bookDetail);

        void onBookDeleteFailed(Exception e);

    }
}
