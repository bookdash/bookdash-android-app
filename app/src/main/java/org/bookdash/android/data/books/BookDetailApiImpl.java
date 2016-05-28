package org.bookdash.android.data.books;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.repacked.apache.commons.codec.language.bm.Lang;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.data.utils.FileManager;
import org.bookdash.android.data.utils.ZipManager;
import org.bookdash.android.domain.pojo.Book;
import org.bookdash.android.domain.pojo.BookContributor;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.domain.pojo.Language;
import org.bookdash.android.domain.pojo.gson.BookPages;
import org.bookdash.android.domain.pojo.firebase.FireLanguage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bolts.Task;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class BookDetailApiImpl implements BookDetailApi {

    private static final String TAG = "BookDetailApiImpl";
    private final Executor DISK_EXECUTOR = Executors.newCachedThreadPool();

    @Override
    public void getBooksForLanguages(@NonNull String language, @NonNull final BookServiceCallback<List<BookDetail>> bookServiceCallback) {
        ParseQuery<Language> queryLanguagesNew = ParseQuery.getQuery(Language.class);
        queryLanguagesNew.whereEqualTo(Language.LANG_NAME_COL, language);

        ParseQuery<BookDetail> queryBookDetail = ParseQuery.getQuery(BookDetail.class);
        queryBookDetail.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        queryBookDetail.include(BookDetail.BOOK_LANGUAGE_COL);
        queryBookDetail.include(BookDetail.BOOK_ID_COL);
        queryBookDetail.whereEqualTo(BookDetail.BOOK_ENABLED_COL, true);
        queryBookDetail.addDescendingOrder(BookDetail.CREATED_AT_COL);
        queryBookDetail.whereMatchesQuery(BookDetail.BOOK_LANGUAGE_COL, queryLanguagesNew);
        queryBookDetail.findInBackground(new FindCallback<BookDetail>() {
            @Override
            public void done(List<BookDetail> list, ParseException e) {
                if (e != null) {
                    bookServiceCallback.onError(e);
                    return;
                }
                bookServiceCallback.onLoaded(list);
            }
        });
    }

    private List<BookDetail> filterOnlyDownloadedBooks(List<BookDetail> bookDetails) {
        List<BookDetail> bookDetailsDownloaded = new ArrayList<>();

        for (BookDetail b : bookDetails) {
            if (b.isDownloadedAlready() || b.isDownloading()) {
                bookDetailsDownloaded.add(b);
            }
        }
        return bookDetailsDownloaded;
    }

    @Override
    public void getDownloadedBooks(final BookServiceCallback<List<BookDetail>> bookServiceCallback) {

        ParseQuery<BookDetail> queryBookDetail = ParseQuery.getQuery(BookDetail.class);
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
                bookServiceCallback.onLoaded(filterOnlyDownloadedBooks(list));
            }
        });
    }

    @Override
    public void getBookDetail(String bookDetailId, final BookServiceCallback<BookDetail> bookServiceCallback) {
        ParseQuery<BookDetail> queryBookDetail = ParseQuery.getQuery(BookDetail.class);
        queryBookDetail.whereEqualTo(BookDetail.OBJECT_ID, bookDetailId);
        queryBookDetail.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        queryBookDetail.include(BookDetail.BOOK_LANGUAGE_COL);
        queryBookDetail.include(BookDetail.BOOK_ID_COL);
        queryBookDetail.whereEqualTo(BookDetail.BOOK_ENABLED_COL, true);
        queryBookDetail.getFirstInBackground(new GetCallback<BookDetail>() {
            @Override
            public void done(BookDetail bookDetail, ParseException e) {
                if (e != null) {
                    bookServiceCallback.onError(e);
                    return;
                }
                bookServiceCallback.onLoaded(bookDetail);
            }
        });
    }

    @Override
    public void getContributorsForBook(Book bookId, final BookServiceCallback<List<BookContributor>> contributorsCallback) {

        ParseQuery<BookContributor> query = ParseQuery.getQuery(BookContributor.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.whereEqualTo(BookContributor.BOOK_BOOK_COL, bookId);
        query.include(BookContributor.BOOK_CONTRIBUTOR_COL);
        query.findInBackground(new FindCallback<BookContributor>() {
            @Override
            public void done(List<BookContributor> list, ParseException e) {
                if (e != null) {
                    contributorsCallback.onError(e);
                    return;
                }
                contributorsCallback.onLoaded(list);
            }
        });


    }


    @Override
    public void getLanguages(final BookServiceCallback<List<FireLanguage>> languagesCallback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference languagesRef = database.getReference("bd_languages");
        languagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FireLanguage> fireLanguages = new ArrayList<>();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    FireLanguage language = snap.getValue(FireLanguage.class);
                    Log.d(TAG, "Language:" + language.languageAbbreviation + ". Language Name:" + language.languageName);
                    fireLanguages.add(language);
                }
                languagesCallback.onLoaded(fireLanguages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                languagesCallback.onError(databaseError.toException());
            }
        });

    }


    @Override
    public void downloadBook(final BookDetail bookInfo, @NonNull final BookServiceCallback<BookPages> downloadBookCallback, @NonNull final BookServiceProgressCallback progressCallback) {
        if (bookInfo.isDownloadedAlready()) {
            progressCallback.onProgressChanged(100);
            downloadBookCallback.onLoaded(getBookPages(bookInfo.getFolderLocation() + File.separator + BookDetail.BOOK_INFO_FILE_NAME));
            return;
        }
        bookInfo.getBookFile().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(final byte[] bytes, ParseException e) {
                if (e != null) {
                    downloadBookCallback.onError(e);
                    return;
                }
                getBookPages(bookInfo, bytes, new BookServiceCallback<BookPages>() {
                    @Override
                    public void onLoaded(BookPages result) {
                        downloadBookCallback.onLoaded(result);
                    }

                    @Override
                    public void onError(Exception error) {
                        downloadBookCallback.onError(error);
                    }
                });


            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer progressInt) {
                progressCallback.onProgressChanged(progressInt);
            }
        });
    }

    @Override
    public void deleteBook(final BookDetail bookDetail, final BookServiceCallback<Boolean> deleteBook) {
        Task.call(new Callable<Void>() {
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
        }, DISK_EXECUTOR);
    }

    @WorkerThread
    private void deleteLocalBook(BookDetail bookDetail) {
        FileManager.deleteFolder(bookDetail.getFolderLocation());
        FileManager.deleteFolder(BookDashApplication.FILES_DIR + "/" + bookDetail.getObjectId());
    }

    private void getBookPages(final BookDetail bookInfo, final byte[] bytes, final BookServiceCallback<BookPages> bookServiceCallback) {
        Task.call(new Callable<BookPages>() {
            @Override
            public BookPages call() throws Exception {
                BookPages bookPages = saveBook(bytes, bookInfo);
                if (bookPages == null) {
                    bookServiceCallback.onError(new Exception("Failed to save book"));
                    return null;
                }
                bookServiceCallback.onLoaded(bookPages);
                return bookPages;
            }
        }, DISK_EXECUTOR);
    }


    @WorkerThread
    private BookPages saveBook(byte[] bytes, BookDetail bookDetail) {
        String targetLocation = BookDashApplication.FILES_DIR + File.separator + bookDetail.getObjectId();
        String fileLocation = BookDashApplication.FILES_DIR + File.separator + bookDetail.getBookFile().getName();

        File f = new File("", targetLocation);
        if (!f.exists() || f.list().length == 0) {
            FileManager.saveFile(BookDashApplication.FILES_DIR, bytes, File.separator + bookDetail.getBookFile().getName());
            ZipManager zipManager = new ZipManager();
            zipManager.unzip(fileLocation, targetLocation);

            FileManager.deleteFile(BookDashApplication.FILES_DIR, File.separator + bookDetail.getBookFile().getName());
        }

        return getBookPages(bookDetail.getFolderLocation() + File.separator + BookDetail.BOOK_INFO_FILE_NAME);
    }


    private BookPages getBookPages(String fileName) {
        Gson gson = new Gson();
        BufferedReader br = null;
        BookPages bookPages = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            bookPages = gson.fromJson(br, BookPages.class);
        } catch (FileNotFoundException e) {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e1) {
                Log.e(TAG, "EX: ", e);
            }

            Log.e(TAG, "Ex:" + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "error parsing book: " + fileName, e);
        }
        return bookPages;
    }
}
