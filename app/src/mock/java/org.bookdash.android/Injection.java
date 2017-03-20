package org.bookdash.android;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.bookdash.android.data.analytics.MockAnalytics;
import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.data.book.DownloadServiceImpl;
import org.bookdash.android.data.book.FakeBookServiceApiImpl;
import org.bookdash.android.data.settings.FakeSettingsApiImpl;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.settings.SettingsRepositoryImpl;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.data.tutorial.TutorialsRepository;
import org.bookdash.android.data.tutorial.TutorialsRepositories;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class Injection {

    public static final String STORAGE_PREFIX = "gs://book-dash-qa.appspot.com/";

    private static DownloadServiceImpl downloadService;

    public static void init(Context context) {
        if (!isInitialized()) {
            FirebaseApp firebaseApp = FirebaseApp
                    .initializeApp(context, FirebaseOptions.fromResource(context), "Book Dash");
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
            firebaseDatabase.setPersistenceEnabled(true);
            downloadService = new DownloadServiceImpl(FirebaseStorage.getInstance(firebaseApp));
        }
    }

    public static TutorialsRepository provideTutorialRepo(Context context) {
        return TutorialsRepositories.getInstance(context);
    }

    private static boolean isInitialized() {
        return downloadService != null;
    }

    public static BookService provideBookService() {
        return new FakeBookServiceApiImpl();
    }

    public static SettingsRepository provideSettingsRepo(Context context) {
        return new SettingsRepositoryImpl(new FakeSettingsApiImpl());
    }

    public static DownloadService provideDownloadService() {
        return downloadService;
    }

    public static Analytics provideAnalytics() {
        return new MockAnalytics();
    }
}
