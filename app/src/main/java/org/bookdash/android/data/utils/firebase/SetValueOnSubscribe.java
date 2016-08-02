package org.bookdash.android.data.utils.firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import rx.Observable;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

class SetValueOnSubscribe<T, U> implements Observable.OnSubscribe<U> {

    private final T value;
    private final DatabaseReference databaseReference;
    private final U returnValue;

    SetValueOnSubscribe(T value, DatabaseReference databaseReference, U returnValue) {
        this.value = value;
        this.databaseReference = databaseReference;
        this.returnValue = returnValue;
    }

    @Override
    public void call(Subscriber<? super U> subscriber) {
        databaseReference.setValue(value, new RxCompletionListener<>(subscriber, returnValue));
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

