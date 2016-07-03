package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.gson.BookPages;

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
    public void getDownloadedBooks(final GetBooksForLanguageCallback getBooksForLanguageCallback) {
        bookDetailApi.getDownloadedBooks(new BookDetailApi.BookServiceCallback<List<FireBookDetails>>() {

            @Override
            public void onLoaded(List<FireBookDetails> result) {
                getBooksForLanguageCallback.onBooksLoaded(result);
            }

            @Override
            public void onError(Exception error) {
                getBooksForLanguageCallback.onBooksLoadError(error);
            }
        });

    }


    @Override
    public void downloadBook(FireBookDetails bookDetail, @NonNull final GetBookPagesCallback bookPagesCallback) {
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

    @Override
    public void deleteBook(final FireBookDetails bookDetail, @NonNull final DeleteBookCallBack deleteBookCallBack) {
        bookDetailApi.deleteBook(bookDetail, new BookDetailApi.BookServiceCallback<Boolean>() {
            @Override
            public void onLoaded(Boolean result) {
                deleteBookCallBack.onBookDeleted(bookDetail);
            }

            @Override
            public void onError(Exception error) {
                deleteBookCallBack.onBookDeleteFailed(error);
            }
        });
    }


}
