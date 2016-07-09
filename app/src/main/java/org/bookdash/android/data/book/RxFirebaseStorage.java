package org.bookdash.android.data.book;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import rx.Observable;
import rx.Subscriber;

public class RxFirebaseStorage {


    @NonNull
    public static Observable<FileDownloadTask.TaskSnapshot> getFile(@NonNull final StorageReference storageRef,
                                                                    @NonNull final File destinationFile) {
        return Observable.create(new Observable.OnSubscribe<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void call(final Subscriber<? super FileDownloadTask.TaskSnapshot> subscriber) {
                storageRef.getFile(destinationFile).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onNext(taskSnapshot);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onCompleted();
                    }
                })
                ;

            }
        });
    }


}
