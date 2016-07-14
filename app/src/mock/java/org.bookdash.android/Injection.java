package org.bookdash.android;

import android.content.Context;

import org.bookdash.android.data.books.FakeBookDetailApiImpl;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.settings.SettingsRepositoryImpl;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class Injection {


    public static BookDetailRepository provideBookRepo(){
        return BookDetailRepositories.getInstance(new FakeBookDetailApiImpl());
    }

    public static SettingsRepository provideSettingsRepo(Context context) {
        return new SettingsRepositoryImpl(new FakeSettingsApiImpl());
    }
}
