package org.bookdash.android.data.database.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bookdash.android.data.utils.firebase.FirebaseObservableListeners;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.firebase.FireRole;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FirebaseBookDatabase implements BookDatabase {

    private static final String TAG = "FirebaseBookDatabase";
    private static final String COLUMN_ENABLED = "enabled";
    private static final String BOOK_COLUMN_ENABLED = "bookEnabled";
    private final DatabaseReference booksTable;
    private final DatabaseReference languagesTable;
    private final FirebaseObservableListeners firebaseObservableListeners;
    private final DatabaseReference contributorsTable;
    private final DatabaseReference roleTable;

    public FirebaseBookDatabase(FirebaseDatabase firebaseDatabase,
                                FirebaseObservableListeners firebaseObservableListeners) {
        this.booksTable = firebaseDatabase.getReference(FireBookDetails.TABLE_NAME);
        this.languagesTable = firebaseDatabase.getReference(FireLanguage.TABLE_NAME);
        this.contributorsTable = firebaseDatabase.getReference(FireContributor.TABLE_NAME);
        this.roleTable = firebaseDatabase.getReference(FireRole.TABLE_NAME);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<List<FireLanguage>> getLanguages() {
        return firebaseObservableListeners.listenToValueEvents(languagesTable, asLanguages());
    }

    @Override
    public Observable<List<FireBookDetails>> getBooks() {
        return firebaseObservableListeners
                .listenToValueEvents(booksTable.orderByChild(FireBookDetails.BOOK_COLUMN_CREATED_DATE), asBooks());
    }

    @Override
    public Observable<FireContributor> getContributorById(final String contributorId) {
        return firebaseObservableListeners
                .listenToSingleValueEvents(contributorsTable.child(contributorId), asContributor());
    }

    @Override
    public Observable<FireRole> getRoleById(final String roleId) {
        return firebaseObservableListeners.listenToSingleValueEvents(roleTable.child(roleId), asRole());
    }

    private Func1<DataSnapshot, FireRole> asRole() {
        return new Func1<DataSnapshot, FireRole>() {
            @Override
            public FireRole call(final DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(FireRole.class);
            }
        };
    }

    private Func1<DataSnapshot, FireContributor> asContributor() {
        return new Func1<DataSnapshot, FireContributor>() {
            @Override
            public FireContributor call(DataSnapshot dataSnapshot) {
                FireContributor contributor = dataSnapshot.getValue(FireContributor.class);
                contributor.setId(dataSnapshot.getKey());
                Log.d(TAG, "Contributor:" + contributor.getName() + ", " + contributor.getAvatar());
                List<String> keys = new ArrayList<>();
                if (dataSnapshot.child(FireContributor.ROLES_SECTION).hasChildren()) {
                    Iterable<DataSnapshot> children = dataSnapshot.child(FireContributor.ROLES_SECTION).getChildren();
                    for (DataSnapshot child : children) {
                        keys.add(child.getKey());
                    }
                }
                contributor.setRoleIds(keys);
                return contributor;
            }
        };
    }

    private Func1<DataSnapshot, List<FireBookDetails>> asBooks() {
        return new Func1<DataSnapshot, List<FireBookDetails>>() {
            @Override
            public List<FireBookDetails> call(DataSnapshot dataSnapshot) {

                List<FireBookDetails> fireBookDetails = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    FireBookDetails bookDetails = snap.getValue(FireBookDetails.class);
                    Log.d(TAG, "Book Details:" + bookDetails.getBookTitle() + ". Book URL:" + bookDetails
                            .getBookCoverPageUrl());
                    bookDetails.setBookId(snap.getKey());
              //      bookDetails.setBookLanguageAbbreviation(languageAbbreviation);
                    List<String> keys = new ArrayList<>();
                    if (snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME).hasChildren()) {
                        Iterable<DataSnapshot> children = snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME)
                                .getChildren();
                        for (DataSnapshot child : children) {
                            keys.add(child.getKey());
                        }
                    }
                    bookDetails.setContributorsIndexedKeys(keys);
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
                    language.setId(snap.getKey());
                    fireLanguages.add(language);
                }
                return fireLanguages;
            }
        };
    }

}
