package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Language;
import org.bookdash.android.domain.pojo.gson.BookPages;

import java.util.ArrayList;
import java.util.List;

/**
 * Can add caching here or fetching from db instead of from service
 *
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class BookDetailRepositoryImpl implements BookDetailRepository {

    private final BookDetailApi bookDetailApi;

    public BookDetailRepositoryImpl(@NonNull BookDetailApi bookDetailApi) {
        this.bookDetailApi = bookDetailApi;
    }

    @Override
    public void getBooksForLanguage(@NonNull String language, final boolean downloadedOnly, @NonNull final GetBooksForLanguageCallback booksForLanguageCallback) {
        bookDetailApi.getBooksForLanguages(language, new BookDetailApi.BookServiceCallback<List<BookDetail>>() {

            @Override
            public void onLoaded(List<BookDetail> result) {
                if (downloadedOnly) {
                    booksForLanguageCallback.onBooksLoaded(filterOnlyDownloadedBooks(result));
                } else {
                    booksForLanguageCallback.onBooksLoaded(result);
                }
            }

            @Override
            public void onError(Exception error) {
                booksForLanguageCallback.onBooksLoadError(error);
            }
        });
    }

    private List<BookDetail> filterOnlyDownloadedBooks(List<BookDetail> bookDetails){
        List<BookDetail> bookDetailsDownloaded = new ArrayList<>();

        for (BookDetail b: bookDetails){
            if (b.isDownloadedAlready() || b.isDownloading()){
                bookDetailsDownloaded.add(b);
            }
        }
        return bookDetailsDownloaded;
    }
    @Override
    public void getBookDetail(String bookDetailId, @NonNull final GetBookDetailCallback bookDetailCallback) {
        bookDetailApi.getBookDetail(bookDetailId, new BookDetailApi.BookServiceCallback<BookDetail>() {
            @Override
            public void onLoaded(BookDetail result) {
                bookDetailCallback.onBookDetailLoaded(result);
            }

            @Override
            public void onError(Exception error) {
                bookDetailCallback.onBookDetailLoadError(error);
            }
        });
    }

    @Override
    public void getContributorsForBook(Book bookId, @NonNull final GetContributorsCallback contributorsCallback) {
        bookDetailApi.getContributorsForBook(bookId, new BookDetailApi.BookServiceCallback<List<BookContributor>>() {
            @Override
            public void onLoaded(List<BookContributor> result) {
                contributorsCallback.onContributorsLoaded(result);
            }

            @Override
            public void onError(Exception error) {
                contributorsCallback.onContributorsLoadError(error);
            }
        });
    }


    @Override
    public void getLanguages(@NonNull final GetLanguagesCallback languagesCallback) {
        bookDetailApi.getLanguages(new BookDetailApi.BookServiceCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                languagesCallback.onLanguagesLoaded(result);
            }

            @Override
            public void onError(Exception error) {
                languagesCallback.onLanguagesLoadError(error);
            }
        });
    }

    @Override
    public void downloadBook(BookDetail bookDetail, @NonNull final GetBookPagesCallback bookPagesCallback) {
        bookDetailApi.downloadBook(bookDetail, new BookDetailApi.BookServiceCallback<BookPages>() {
            @Override
            public void onLoaded(BookPages result) {
                bookPagesCallback.onBookPagesLoaded(result);
            }

            @Override
            public void onError(Exception error) {
                bookPagesCallback.onBookPagesLoadError(error);
            }
        }, new BookDetailApi.BookServiceProgressCallback() {
            @Override
            public void onProgressChanged(int progress) {
                bookPagesCallback.onBookPagesDownloadProgressUpdate(progress);
            }
        });
    }


}
