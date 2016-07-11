package org.bookdash.android.data.books;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.gson.BookPages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class BookDetailApiImpl implements BookDetailApi {

    private static final String TAG = "BookDetailApiImpl";
    private final Executor DISK_EXECUTOR = Executors.newCachedThreadPool();


    /*private List<BookDetail> filterOnlyDownloadedBooks(List<BookDetail> bookDetails) {
        List<BookDetail> bookDetailsDownloaded = new ArrayList<>();

        for (BookDetail b : bookDetails) {
            if (b.isDownloadedAlready() || b.isDownloading()) {
                bookDetailsDownloaded.add(b);
            }
        }
        return bookDetailsDownloaded;
    }*/

    @Override
    public void getDownloadedBooks(final BookServiceCallback<List<FireBookDetails>> bookServiceCallback) {

        /*ParseQuery<BookDetail> queryBookDetail = ParseQuery.getQuery(BookDetail.class);
        queryBookDetail.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        queryBookDetail.include(BookDetail.BOOK_LANGUAGE_COL);
        queryBookDetail.include(BookDetail.BOOK_ID_COL);
        queryBookDetail.whereEqualTo(BookDetail.BOOK_ENABLED_COL, true);
        queryBookDetail.addDescendingOrder(BookDetail.CREATED_AT_COL);
        queryBookDetail.findInBackground(new FindCallback<BookDetail>() {
            @Override
            public void done(List<BookDetail> list, ParseException e) {
                if (e != null) {
                    bookServiceCallback.onError(e);
                    return;
                }
               // bookServiceCallback.onLoaded(filterOnlyDownloadedBooks(list));
            }
        });*/
    }



    @Override
    public void deleteBook(final FireBookDetails bookDetail, final BookServiceCallback<Boolean> deleteBook) {
   /*     Task.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    deleteLocalBook(bookDetail);
                    deleteBook.onLoaded(true);
                } catch (Exception e) {
                    deleteBook.onError(e);
                }
                return null;

            }
        }, DISK_EXECUTOR);*/
    }

    @WorkerThread
    private void deleteLocalBook(FireBookDetails bookDetail) {
        //   FileManager.deleteFolder(bookDetail.getFolderLocation());
        //   FileManager.deleteFolder(BookDashApplication.FILES_DIR + "/" + bookDetail.getObjectId());
    }




}
