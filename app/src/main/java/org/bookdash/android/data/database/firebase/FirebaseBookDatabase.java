package org.bookdash.android.data.database.firebase;

import android.support.annotation.NonNull;
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
import rx.functions.Func2;


public class FirebaseBookDatabase implements BookDatabase {

    private static final String TAG = "FirebaseBookDatabase";
    private final DatabaseReference booksTable;
    private final DatabaseReference languagesTable;
    private final FirebaseObservableListeners firebaseObservableListeners;
    private final DatabaseReference contributorsTable;
    private final DatabaseReference roleTable;

    public FirebaseBookDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
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
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage fireLanguage) {
        return firebaseObservableListeners.listenToValueEvents(booksTable.orderByChild(FireBookDetails.BOOK_TITLE), asBooks()).flatMap(filterByLanguageSelected(fireLanguage));
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails fireBookDetails) {
        return Observable.just(fireBookDetails.getContributorsIndexList()).flatMap(getContributorsFromIds());
    }

    private Func1<List<String>, Observable<List<FireContributor>>> getContributorsFromIds() {
        return new Func1<List<String>, Observable<List<FireContributor>>>() {
            @Override
            public Observable<List<FireContributor>> call(List<String> userIds) {
                return Observable.from(userIds).flatMap(getUserFromId()).toList();
            }
        };
    }

    private Func1<String, Observable<FireContributor>> getUserFromId() {
        return new Func1<String, Observable<FireContributor>>() {
            @Override
            public Observable<FireContributor> call(final String userId) {
                return firebaseObservableListeners.listenToSingleValueEvents(contributorsTable.child(userId), asContributor()).flatMap(getRolesForContributor());
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

    private Func1<FireContributor, Observable<FireContributor>> getRolesForContributor() {
        return new Func1<FireContributor, Observable<FireContributor>>() {
            @Override
            public Observable<FireContributor> call(final FireContributor fireContributor) {
                return Observable.zip(Observable.just(fireContributor), Observable.from(fireContributor.getRoleIds()).flatMap(getRole()).toList(), new Func2<FireContributor, List<FireRole>, FireContributor>() {
                    @Override
                    public FireContributor call(final FireContributor fireContributor, final List<FireRole> fireRoles) {
                        fireContributor.setActualRoles(fireRoles);
                        return fireContributor;
                    }
                });
            }
        };
    }

    private Func1<String, Observable<FireRole>> getRole() {
        return new Func1<String, Observable<FireRole>>() {
            @Override
            public Observable<FireRole> call(final String roleId) {
                return firebaseObservableListeners.listenToSingleValueEvents(roleTable.child(roleId), asRole());
            }
        };
    }

    private Func1<DataSnapshot, FireRole> asRole() {
        return new Func1<DataSnapshot, FireRole>() {
            @Override
            public FireRole call(final DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(FireRole.class);
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
                    Log.d(TAG, "Book Details:" + bookDetails.bookTitle + ". Book URL:" + bookDetails.bookCoverPageUrl);
                    bookDetails.bookId = snap.getKey();
                    List<String> keys = new ArrayList<>();
                    if (snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME).hasChildren()) {
                        Iterable<DataSnapshot> children = snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME).getChildren();
                        for (DataSnapshot child : children) {
                            keys.add(child.getKey());
                        }
                    }
                    bookDetails.setContributors(keys);
                    fireBookDetails.add(bookDetails);

                }
                return fireBookDetails;
            }
        };
    }

    @NonNull
    private Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>> filterByLanguageSelected(final FireLanguage fireLanguage) {
        return new Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>>() {
            @Override
            public Observable<List<FireBookDetails>> call(final List<FireBookDetails> fireBookList) {
                Log.d(TAG, "call() called with: " + "fireBookList = [" + fireBookList + "]");
                return Observable.from(fireBookList).filter(new Func1<FireBookDetails, Boolean>() {
                    @Override
                    public Boolean call(final FireBookDetails fireBookDetails) {
                        Log.d(TAG, "call() called with: " + "fireBookDetails = [" + fireBookDetails + "]");
                        return fireBookDetails.bookLanguage.equalsIgnoreCase(fireLanguage.getId());
                    }
                }).toList();
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
