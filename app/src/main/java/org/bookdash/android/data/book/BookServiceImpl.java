package org.bookdash.android.data.book;

import android.support.annotation.NonNull;
import android.util.Log;

import org.bookdash.android.data.database.firebase.BookDatabase;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.firebase.FireRole;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;


public class BookServiceImpl implements BookService {

    private static final String TAG = "BookServiceImpl";
    private final BookDatabase bookDatabase;

    public BookServiceImpl(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    @Override
    public Observable<List<FireLanguage>> getLanguages() {
        return bookDatabase.getLanguages();
    }

    @Override
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage fireLanguage) {
        return bookDatabase.getBooks().flatMap(filterLanguage(fireLanguage));
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails fireBookDetails) {
        return Observable.just(fireBookDetails.getContributorsIndexList()).flatMap(getContributorsFromIds());
    }

    @Override
    public Observable<BookPages> downloadBook(final FireBookDetails fireBookDetails) {
        return Observable.defer(new Func0<Observable<BookPages>>() {
            @Override
            public Observable<BookPages> call() {

                return null;
            }
        });
    }

    private Func1<List<String>, Observable<List<FireContributor>>> getContributorsFromIds() {
        return new Func1<List<String>, Observable<List<FireContributor>>>() {
            @Override
            public Observable<List<FireContributor>> call(List<String> userIds) {
                return Observable.from(userIds).flatMap(new Func1<String, Observable<FireContributor>>() {
                    @Override
                    public Observable<FireContributor> call(final String s) {
                        return bookDatabase.getContributorById(s).flatMap(getRolesForContributor());
                    }
                }).toList();
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
            public Observable<FireRole> call(final String s) {
                return bookDatabase.getRoleById(s);
            }
        };
    }

    @NonNull
    private Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>> filterLanguage(final FireLanguage fireLanguage) {
        return new Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>>() {
            @Override
            public Observable<List<FireBookDetails>> call(final List<FireBookDetails> fireBookList) {
                Log.d(TAG, "call() called with: " + "fireBookList = [" + fireBookList + "]");
                return Observable.from(fireBookList).filter(new Func1<FireBookDetails, Boolean>() {
                    @Override
                    public Boolean call(final FireBookDetails fireBookDetails) {
                        Log.d(TAG, "call() called with: " + "fireBookDetails = [" + fireBookDetails + "]");
                        return fireBookDetails.getBookLanguage().equalsIgnoreCase(fireLanguage.getId());
                    }
                }).toList();
            }
        };
    }
}
