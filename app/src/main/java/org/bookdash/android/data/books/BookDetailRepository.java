package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

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
public interface BookDetailRepository {


    void getBooksForLanguage(@NonNull String language, @NonNull GetBooksForLanguageCallback booksForLanguageCallback);

    void getDownloadedBooks(GetBooksForLanguageCallback getBooksForLanguageCallback);

    interface GetBooksForLanguageCallback {
        void onBooksLoaded(List<BookDetail> books);

        void onBooksLoadError(Exception e);
    }

    interface GetBookDetailCallback {
        void onBookDetailLoaded(BookDetail bookDetail);

        void onBookDetailLoadError(Exception e);
    }

    interface GetContributorsCallback {
        void onContributorsLoaded(List<BookContributor> contributors);

        void onContributorsLoadError(Exception e);
    }


    interface GetLanguagesCallback {
        void onLanguagesLoaded(List<Language> languages);

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

    void downloadBook(BookDetail bookDetail, @NonNull GetBookPagesCallback bookPagesCallback);

    void deleteBook(BookDetail bookDetail, @NonNull DeleteBookCallBack deleteBookCallBack);

    interface DeleteBookCallBack {
        void onBookDeleted(BookDetail bookDetail);

        void onBookDeleteFailed(Exception e);

    }
}
