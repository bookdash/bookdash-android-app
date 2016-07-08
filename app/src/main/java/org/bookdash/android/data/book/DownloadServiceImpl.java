package org.bookdash.android.data.book;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;


public class DownloadServiceImpl implements DownloadService {

    private final StorageReference storageRef;

    public DownloadServiceImpl(StorageReference firebaseStorageRef) {
        this.storageRef = firebaseStorageRef;
    }

    @Override
    public Observable<DownloadProgressItem> downloadFile(final String url) {
        return Observable.create(new Observable.OnSubscribe<DownloadProgressItem>() {

            @Override
            public void call(final Subscriber<? super DownloadProgressItem> subscriber) {
                final StorageReference fileDownloadRef = storageRef.child(url);

                File localFile = null;
                try {
                    localFile = File.createTempFile("images", "zip");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fileDownloadRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        subscriber.onNext(new DownloadProgressItem() {
                            @Override
                            public long getBytesTransferred() {
                                return taskSnapshot.getBytesTransferred();
                            }

                            @Override
                            public long getTotalByteCount() {
                                return taskSnapshot.getTotalByteCount();
                            }
                        });
                        subscriber.onCompleted();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        subscriber.onError(exception);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(final FileDownloadTask.TaskSnapshot taskSnapshot) {

                        subscriber.onNext(new DownloadProgressItem() {
                            @Override
                            public long getBytesTransferred() {
                                return taskSnapshot.getBytesTransferred();
                            }

                            @Override
                            public long getTotalByteCount() {
                                return taskSnapshot.getTotalByteCount();
                            }
                        });
                    }
                });
            }
        });

    }

    public interface DownloadProgressItem {
        long getBytesTransferred();

        long getTotalByteCount();
    }
}
