package org.bookdash.android;

import android.content.Context;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.data.book.FakeBookServiceApiImpl;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.settings.SettingsRepositoryImpl;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class Injection {


    public static void init(Context context) {

    }

    public static BookService provideBookService() {
        return new FakeBookServiceApiImpl();
    }

    public static SettingsRepository provideSettingsRepo(Context context) {
        return new SettingsRepositoryImpl(new FakeSettingsApiImpl());
    }

    public static DownloadService provideDownloadService() {
        return new FakeDownloadService();
    }
}
