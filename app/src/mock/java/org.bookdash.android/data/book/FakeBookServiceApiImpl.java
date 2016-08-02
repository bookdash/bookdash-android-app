package org.bookdash.android.data.book;

import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FakeBookServiceApiImpl implements BookService {

    FireLanguage fireLanguageEnglish = new FireLanguage("English", "EN", true, "1");
    FireLanguage fireLanguageZulu = new FireLanguage("Zulu", "ZU", true, "2");
    List<FireLanguage> fireLanguages = new ArrayList<>();

    List<FireBookDetails> fireBookDetailses = new ArrayList<>();
    FireBookDetails fireBookDetails1 = new FireBookDetails("Searching for the Spirit of Spring",
            "https://storage.googleapis.com/bookdash_books/books/50-itwasntme/itwasntme-en.zip",
            "https://storage.googleapis.com/bookdash_books/books/50-itwasntme/Page_01.jpg", true, "description_dummy",
            fireLanguageEnglish);
    FireBookDetails fireBookDetails2 = new FireBookDetails("Searching for the Spirit of Spring Zulu",
            "https://storage.googleapis.com/bookdash_books/books/50-itwasntme/itwasntme-en.zip",
            "https://storage.googleapis.com/bookdash_books/books/50-itwasntme/Page_01.jpg", true, "description_dummy",
            fireLanguageZulu);


    public FakeBookServiceApiImpl() {
        fireLanguages.add(fireLanguageEnglish);
        fireLanguages.add(fireLanguageZulu);

        fireBookDetailses.add(fireBookDetails1);
        fireBookDetailses.add(fireBookDetails2);
    }

    @Override
    public Observable<List<FireLanguage>> getLanguages() {

        return Observable.just(fireLanguages);
    }

    @Override
    public Observable<List<FireBookDetails>> getBooksForLanguage(final FireLanguage fireLanguage) {
        return Observable.from(fireBookDetailses).filter(new Func1<FireBookDetails, Boolean>() {
            @Override
            public Boolean call(final FireBookDetails fireBookDetails) {
                return fireBookDetails.getBookLanguage().equals(fireLanguage.getId());
            }
        }).toList();
    }

    @Override
    public Observable<List<FireContributor>> getContributorsForBook(final FireBookDetails bookDetails) {
        List<FireContributor> fireContributors = new ArrayList<>();
        FireContributor fireContributor = new FireContributor("Rebecca Franks", "https://riggaroo.co.za/wp-content/uploads/2016/03/rebeccafranks_circle.png", "1");
        fireContributors.add(fireContributor);
        return Observable.just(fireContributors);
    }

    @Override
    public Observable<List<FireBookDetails>> getDownloadedBooks() {
        return Observable.from(fireBookDetailses).filter(new Func1<FireBookDetails, Boolean>() {
            @Override
            public Boolean call(final FireBookDetails fireBookDetails) {
                return fireBookDetails.isDownloadedAlready();
            }
        }).toList();
    }
}
