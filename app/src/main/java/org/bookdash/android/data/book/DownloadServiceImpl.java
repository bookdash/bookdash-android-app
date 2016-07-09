package org.bookdash.android.data.book;


import android.util.Log;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bookdash.android.domain.model.DownloadProgressItem;

import java.io.File;
import java.io.IOException;

import rx.Observable;
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
        File localFile;
        try {
            localFile = File.createTempFile("images", "zip");

            return RxFirebaseStorage.getFile(fileDownloadRef, localFile).flatMap(new Func1<FileDownloadTask.TaskSnapshot, Observable<DownloadProgressItem>>() {
                @Override
                public Observable<DownloadProgressItem> call(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    return Observable.just(new DownloadProgressItem(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount()));
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "IOException downloading file", e);
            return Observable.error(e);
        }
    }

}
