package org.bookdash.android.data.book;


import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.gson.BookPages;

import java.util.List;

import rx.Observable;

public interface BookService {

    Observable<List<FireLanguage>> getLanguages();

    Observable<List<FireBookDetails>> getBooksForLanguage(FireLanguage fireLanguage);

    Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails bookDetails);

    Observable<BookPages> downloadBook(FireBookDetails fireBookDetails);
}
