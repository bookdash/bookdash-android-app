package org.bookdash.android.data.book;


import android.util.Log;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        File localFile = null;
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

    public class DownloadProgressItem {
        long bytesTransferred;
        long totalByteCount;

        public DownloadProgressItem(long bytesTransferred, long totalByteCount) {
            this.bytesTransferred = bytesTransferred;
            this.totalByteCount = totalByteCount;
        }

        public int getDownloadProgress() {
            return (int) (((float) (bytesTransferred) / (float) totalByteCount) * 100);
        }
    }

    /*public class DownloadSubscriber implements Observable.OnSubscribe<DownloadProgressItem> {

        String url;

        DownloadSubscriber(String url) {
            this.url = url;
        }

        @Override
        public void call(final Subscriber<? super DownloadProgressItem> subscriber) {
            Log.d(TAG, "call() called with: subscriber = [" + "]");
            final StorageReference fileDownloadRef = storageRef.getReferenceFromUrl(url);

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "zip");
                fileDownloadRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess() called with: taskSnapshot = [" + taskSnapshot + "]");
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onNext(new DownloadProgressItem(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount()));
                        subscriber.onCompleted();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onError(exception);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(final FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        DownloadProgressItem downloadProgressItem = new DownloadProgressItem(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                        Log.d(TAG, "onProgress() called with: taskSnapshot = [" + downloadProgressItem.getDownloadProgress() + "]");
                        subscriber.onNext(downloadProgressItem);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "IOException downloading file", e);
            }
        }
    }*/
}
