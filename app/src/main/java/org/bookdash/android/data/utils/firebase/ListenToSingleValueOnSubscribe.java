package org.bookdash.android.data.utils.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observables.SyncOnSubscribe;

class ListenToSingleValueOnSubscribe<T> implements Observable.OnSubscribe<T> {

    private final Query query;
    private final Func1<DataSnapshot, T> marshaller;

    ListenToSingleValueOnSubscribe(Query query, Func1<DataSnapshot, T> marshaller) {
        this.query = query;
        this.marshaller = marshaller;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        query.addListenerForSingleValueEvent(new RxSingleValueListener<>(subscriber, marshaller));
    }

    private static class RxSingleValueListener<T> implements ValueEventListener {

        private final Subscriber<? super T> subscriber;
        private final Func1<DataSnapshot, T> marshaller;

        public RxSingleValueListener(Subscriber<? super T> subscriber, Func1<DataSnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren() && !subscriber.isUnsubscribed()) {
                subscriber.onNext(marshaller.call(dataSnapshot));
            }
            subscriber.onCompleted();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriber.onError(databaseError.toException()); //TODO handle errors in pipeline
        }

    }
}

