package org.bookdash.android.data.database.firebase;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;
import org.bookdash.android.domain.model.firebase.FireRole;

import java.util.List;

import rx.Observable;


public interface BookDatabase {

    Observable<List<FireLanguage>> getLanguages();

    Observable<List<FireBookDetails>> getBooks();

    Observable<FireContributor> getContributorById(String contributorId);

    Observable<FireRole> getRoleById(String roleId);

    Observable<List<FireBookDetails>> getBooksByLanguage(FireLanguage fireLanguage);

}
