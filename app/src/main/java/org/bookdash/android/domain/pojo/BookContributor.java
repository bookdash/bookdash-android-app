package org.bookdash.android.domain.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName(BookContributor.BOOK_CONTRIBUTOR_TABLE_NAME)
public class BookContributor extends ParseObject {
    public static final String BOOK_CONTRIBUTOR_TABLE_NAME = "BookContributor";
    public static final String BOOK_CONTRIBUTOR_COL = "contributor";

    public BookContributor() {
    }

    public BookContributor(Contributor contributor) {
        super(BOOK_CONTRIBUTOR_TABLE_NAME);
        put(BOOK_CONTRIBUTOR_COL, contributor);
    }

    public Contributor getContributor() {
        return (Contributor) get(BOOK_CONTRIBUTOR_COL);
    }
}
