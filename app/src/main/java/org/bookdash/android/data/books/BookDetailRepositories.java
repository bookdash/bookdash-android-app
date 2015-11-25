package org.bookdash.android.data.books;

import android.support.annotation.NonNull;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class BookDetailRepositories {

    private BookDetailRepositories() {
        // no instance
    }

    private static BookDetailRepository repository = null;

    public synchronized static BookDetailRepository getInstance(@NonNull BookDetailApi bookDetailApi) {
        if (null == repository) {
            repository = new BookDetailRepositoryImpl(bookDetailApi);
        }
        return repository;
    }
}
