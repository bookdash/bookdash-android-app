package org.bookdash.android;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.bookdash.android.data.book.BookService;
import org.bookdash.android.data.book.BookServiceImpl;
import org.bookdash.android.data.books.BookDetailApiImpl;
import org.bookdash.android.data.books.BookDetailRepositories;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.data.database.firebase.FirebaseBookDatabase;
import org.bookdash.android.data.settings.SettingsApiImpl;
import org.bookdash.android.data.settings.SettingsRepositories;
import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.utils.firebase.FirebaseObservableListeners;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class Injection {

    private static BookService bookService = null;

    private Injection() {

    }


    public static void init(Context context){
        if (!isInitialized()) {
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(context, FirebaseOptions.fromResource(context), "Book Dash");
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
            firebaseDatabase.setPersistenceEnabled(true);
            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();
            FirebaseBookDatabase bookDatabase = new FirebaseBookDatabase(firebaseDatabase, firebaseObservableListeners);
            bookService = new BookServiceImpl(bookDatabase);
        }
    }

    private static boolean isInitialized() {
        return bookService != null;
    }

    public static BookDetailRepository provideBookRepo(){
        return BookDetailRepositories.getInstance(new BookDetailApiImpl());
    }

    public static SettingsRepository provideSettingsRepo(Context context) {
        return SettingsRepositories.getInstance(new SettingsApiImpl(context));
    }

    public static BookService provideBookService() {
        return bookService;
    }
}
