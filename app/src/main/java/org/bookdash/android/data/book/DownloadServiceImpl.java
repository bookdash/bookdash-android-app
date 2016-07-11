package org.bookdash.android.data.book;


import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.data.utils.ZipManager;
import org.bookdash.android.domain.model.DownloadProgressItem;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.gson.BookPages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;


public class DownloadServiceImpl implements DownloadService {
    public static final String TAG = "DownloadService";
    private final FirebaseStorage storageRef;

    public DownloadServiceImpl(FirebaseStorage firebaseStorageRef) {
        this.storageRef = firebaseStorageRef;
    }

    @Override
    public Observable<DownloadProgressItem> downloadFile(final String url) {
        final StorageReference fileDownloadRef = storageRef.getReferenceFromUrl(url);
        final File localFile;
        try {
            Uri uri = Uri.parse(url);
            localFile = File.createTempFile(uri.getLastPathSegment(),"");

            return RxFirebaseStorage.getFile(fileDownloadRef, localFile).flatMap(new Func1<FileDownloadTask.TaskSnapshot, Observable<DownloadProgressItem>>() {
                @Override
                public Observable<DownloadProgressItem> call(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DownloadProgressItem downloadProgressItem = new DownloadProgressItem(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                    if (downloadProgressItem.isComplete()) {
                        return Observable.defer(new TransformFileIntoBookPages(downloadProgressItem, localFile));
                    }
                    return Observable.just(downloadProgressItem);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "IOException downloading file", e);
            return Observable.error(e);
        }
    }

    class TransformFileIntoBookPages implements Func0<Observable<DownloadProgressItem>> {
        private final File file;
        DownloadProgressItem downloadProgressItem;

        TransformFileIntoBookPages(DownloadProgressItem downloadProgressItem, File localFile) {
            this.downloadProgressItem = downloadProgressItem;
            this.file = localFile;
        }

        @Override
        public Observable<DownloadProgressItem> call() {
            String targetLocation = BookDashApplication.FILES_DIR + File.separator + file.getName().replace(".zip", "");
            Log.d(TAG, "Target Location:" + targetLocation);
            ZipManager zipManager = new ZipManager();
            Log.d(TAG, " Absolute Path:" + file.getAbsolutePath());
            zipManager.unzip(file.getAbsolutePath(), targetLocation);

            BookPages bookPages = getBookPages(targetLocation + File.separator + FireBookDetails.BOOK_FORMAT_JSON_FILE);
            downloadProgressItem.setBookPages(bookPages);
            Log.d(TAG, "TransformFileIntoBookPages call() called - bookPages:" + bookPages);
            return Observable.just(downloadProgressItem);

        }
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
