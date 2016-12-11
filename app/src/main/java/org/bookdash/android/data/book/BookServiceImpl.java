package org.bookdash.android.data.book;

import android.support.annotation.NonNull;

import org.bookdash.android.data.database.firebase.BookDatabase;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.firebase.FireRole;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;


public class BookServiceImpl implements BookService {

    private final BookDatabase bookDatabase;

    public BookServiceImpl(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    @Override
    public Observable<List<FireLanguage>> getLanguages() {
        return bookDatabase.getLanguages().flatMap(filterEnabledLanguages());
    }

    @Override
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage language) {
        return bookDatabase.getBooksByLanguage(language).flatMap(filterEnabledBooks());
    }

    private Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>> filterEnabledBooks() {
        return new Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>>() {
            @Override
            public Observable<List<FireBookDetails>> call(final List<FireBookDetails> fireBookDetailses) {
                return Observable.from(fireBookDetailses).filter(new Func1<FireBookDetails, Boolean>() {
                    @Override
                    public Boolean call(FireBookDetails bookDetails) {
                        return bookDetails.isBookEnabled();
                    }
                }).toList();
            }
        };
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails book) {
        return Observable.just(book.getContributorsIndexList()).flatMap(getContributorsFromIds());
    }

    @Override
    public Observable<List<FireBookDetails>> getDownloadedBooks() {
        return bookDatabase.getBooks().flatMap(filterDownloadedBooks());
    }

    @NonNull
    private Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>> filterDownloadedBooks() {
        return new Func1<List<FireBookDetails>, Observable<List<FireBookDetails>>>() {
            @Override
            public Observable<List<FireBookDetails>> call(List<FireBookDetails> fireBookDetailses) {
                return Observable.from(fireBookDetailses).filter(new Func1<FireBookDetails, Boolean>() {
                    @Override
                    public Boolean call(FireBookDetails bookDetails) {
                        return bookDetails.isDownloadedAlready();
                    }
                }).toList();
            }
        };
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
                return Observable.zip(Observable.just(fireContributor),
                        Observable.from(fireContributor.getRoleIds()).flatMap(getRole()).toList(),
                        new Func2<FireContributor, List<FireRole>, FireContributor>() {
                            @Override
                            public FireContributor call(final FireContributor fireContributor,
                                                        final List<FireRole> fireRoles) {
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

    private Func1<List<FireLanguage>, Observable<List<FireLanguage>>> filterEnabledLanguages() {
        return new Func1<List<FireLanguage>, Observable<List<FireLanguage>>>() {
            @Override
            public Observable<List<FireLanguage>> call(final List<FireLanguage> fireLanguages) {
                return Observable.from(fireLanguages).filter(new Func1<FireLanguage, Boolean>() {
                    @Override
                    public Boolean call(final FireLanguage fireLanguage) {
                        return fireLanguage.isEnabled();
                    }
                }).toList();
            }
        };
    }
}
