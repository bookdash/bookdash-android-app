package org.bookdash.android.data.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bookdash.android.data.utils.firebase.FirebaseObservableListeners;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FirebaseBookDatabase implements BookDatabase {

    private static final String TAG = "FirebaseBookDatabase";
    private final DatabaseReference booksTable;
    private final DatabaseReference languagesTable;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseBookDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        this.booksTable = firebaseDatabase.getReference(FireBookDetails.TABLE_NAME);
        this.languagesTable = firebaseDatabase.getReference(FireLanguage.TABLE_NAME);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<List<FireLanguage>> getLanguages() {
        return firebaseObservableListeners.listenToValueEvents(languagesTable, asLanguages());
    }

    @Override
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage fireLanguage) {
        return firebaseObservableListeners.listenToValueEvents(booksTable.orderByChild(FireBookDetails.BOOK_TITLE), asBooks());
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails fireBookDetails) {
        return null;
    }


    private Func1<DataSnapshot, List<FireBookDetails>> asBooks() {
        return new Func1<DataSnapshot, List<FireBookDetails>>() {
            @Override
            public List<FireBookDetails> call(DataSnapshot dataSnapshot) {

                List<FireBookDetails> fireBookDetails = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    FireBookDetails bookDetails = snap.getValue(FireBookDetails.class);
                    Log.d(TAG, "Book Details:" + bookDetails.bookTitle + ". Book URL:" + bookDetails.bookCoverPageUrl);
                    bookDetails.bookId = snap.getKey();
                    fireBookDetails.add(bookDetails);

                }
                return fireBookDetails;
            }
        };
    }

    private Func1<DataSnapshot, List<FireLanguage>> asLanguages() {
        return new Func1<DataSnapshot, List<FireLanguage>>() {
            @Override
            public List<FireLanguage> call(DataSnapshot dataSnapshot) {
                List<FireLanguage> fireLanguages = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    FireLanguage language = snap.getValue(FireLanguage.class);
                    fireLanguages.add(language);
                }
                return fireLanguages;
            }
        };
    }
}
