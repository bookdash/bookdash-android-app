package org.bookdash.android.data.book;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.List;

import rx.Observable;


public class FakeBookServiceApiImpl implements BookService {
    @Override
    public Observable<List<FireLanguage>> getLanguages() {
        return null;
    }

    @Override
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage fireLanguage) {
        return null;
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails bookDetails) {
        return null;
    }

    @Override
    public Observable<List<FireBookDetails>> getDownloadedBooks() {
        return null;
    }
}
