package org.bookdash.android;

import android.content.Context;

import org.bookdash.android.data.books.BookDetailApiImpl;
import org.bookdash.android.data.books.BookDetailRepositories;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.data.settings.SettingsApiImpl;
import org.bookdash.android.data.settings.SettingsRepositories;
import org.bookdash.android.data.settings.SettingsRepository;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class Injection {

    public static BookDetailRepository provideBookRepo(){
        return BookDetailRepositories.getInstance(new BookDetailApiImpl());
    }

    public static SettingsRepository provideSettingsRepo(Context context) {
        return SettingsRepositories.getInstance(new SettingsApiImpl(context));
    }
}
