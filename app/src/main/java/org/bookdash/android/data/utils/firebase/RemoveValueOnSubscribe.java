package org.bookdash.android.data.utils.firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import rx.Observable;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

class RemoveValueOnSubscribe<T> implements Observable.OnSubscribe<T> {

    private final DatabaseReference databaseReference;
    private final T returnValue;

    RemoveValueOnSubscribe(DatabaseReference databaseReference, T returnValue) {
        this.databaseReference = databaseReference;
        this.returnValue = returnValue;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        databaseReference.removeValue(new RxCompletionListener<>(subscriber, returnValue));
    }

    private static class RxCompletionListener<T> implements DatabaseReference.CompletionListener {

        private final Subscriber<? super T> subscriber;
        private final T successValue;

        RxCompletionListener(Subscriber<? super T> subscriber, T successValue) {
            this.subscriber = subscriber;
            this.successValue = successValue;
        }

        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if (databaseError == null) {
                subscriber.onNext(successValue);
                subscriber.onCompleted();
            } else {
                subscriber.onError(databaseError.toException());
            }
        }

    }
}
