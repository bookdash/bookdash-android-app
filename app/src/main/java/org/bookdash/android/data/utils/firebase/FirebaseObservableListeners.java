package org.bookdash.android.data.utils.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import rx.Observable;
import rx.functions.Func1;

public class FirebaseObservableListeners {

    public <T> Observable<T> listenToValueEvents(Query query, Func1<DataSnapshot, T> marshaller) {
        return Observable.create(new ListenToValueEventsOnSubscribe<>(query, marshaller));
    }

    public <T> Observable<T> listenToSingleValueEvents(Query query, Func1<DataSnapshot, T> marshaller) {
        return Observable.create(new ListenToSingleValueOnSubscribe<>(query, marshaller));
    }

    public <T> Observable<T> removeValue(DatabaseReference databaseReference, T returnValue) {
        return Observable.create(new RemoveValueOnSubscribe<>(databaseReference, returnValue));
    }

    public <T, U> Observable<U> setValue(T value, DatabaseReference databaseReference, U returnValue) {
        return Observable.create(new SetValueOnSubscribe<>(value, databaseReference, returnValue));
    }

}
