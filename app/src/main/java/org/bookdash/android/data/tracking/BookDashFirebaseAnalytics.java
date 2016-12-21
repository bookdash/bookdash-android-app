package org.bookdash.android.data.tracking;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.bookdash.android.domain.model.firebase.FireBookDetails;

/**
 * @author rebeccafranks
 * @since 2016/12/05
 */
public class BookDashFirebaseAnalytics implements Analytics {

    private static final String EVENT_VIEW_DOWNLOADS = "view_downloads";
    private static final String EVENT_VIEW_BOOK = "view_single_book";
    private static final String PARAM_BOOK_ID = "book_id";
    private static final String PARAM_BOOK_NAME = "book_name";
    private static final String EVENT_DELETE_BOOK = "delete_book";
    private static final String EVENT_RATE_APP = "rate_app";
    private static final String EVENT_VIEW_CONTRIBUTORS = "view_contributors";
    private static final String EVENT_CHANGE_LANGUAGE = "change_language";
    private static final String PARAM_NEW_LANGUAGE = "new_language";
    private static final String PARAM_CURRENT_LANGUAGE = "current_language";
    private static final String EVENT_VIEW_ALL_BOOKS = "view_all_books";
    private static final String PARAM_ERROR_MESSAGE = "error_msg";
    private static final String EVENT_DOWNLOAD_BOOK_FAILED = "download_book_failed";
    private static final String EVENT_DOWNLOAD_BOOK_STARTED = "download_book_started";
    private static final String EVENT_SHARE_BOOK = "share_book";
    private static final String EVENT_INVITE_PEOPLE = "invite_friends";
    private static final String PARAM_SEARCH_TERM = "search_term";
    private static final String EVENT_SEARCH_BOOKS = "search_books";
    private static final String EVENT_SEARCH_BOOKS_ERROR = "search_books_error";
    private static final String EVENT_SEARCH_BOOKS_SUCCESS = "search_books_success";
    private static final String PARAM_SEARCH_RESULTS_COUNT = "search_results_count";
    private static final String EVENT_SEARCH_BOOKS_NO_RESULTS = "search_books_no_results";

    private FirebaseAnalytics firebaseAnalytics;

    public BookDashFirebaseAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void trackLanguageChange(final String newLanguage) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NEW_LANGUAGE, newLanguage);
        firebaseAnalytics.logEvent(EVENT_CHANGE_LANGUAGE, bundle);
        setUserLanguage(newLanguage);
    }

    @Override
    public void trackViewBooksDownloaded() {
        firebaseAnalytics.logEvent(EVENT_VIEW_DOWNLOADS, null);
    }

    @Override
    public void trackViewBook(final FireBookDetails book) {
        Bundle bundle = getBookInfoBundle(book);
        firebaseAnalytics.logEvent(EVENT_VIEW_BOOK, bundle);
    }

    @Override
    public void trackDeleteBook(final FireBookDetails book) {
        Bundle bundle = getBookInfoBundle(book);
        firebaseAnalytics.logEvent(EVENT_DELETE_BOOK, bundle);
    }

    @Override
    public void trackRateAppClick() {
        firebaseAnalytics.logEvent(EVENT_RATE_APP, null);
    }

    @Override
    public void trackViewContributors() {
        firebaseAnalytics.logEvent(EVENT_VIEW_CONTRIBUTORS, null);

    }

    @Override
    public void trackViewAllBooks() {
        firebaseAnalytics.logEvent(EVENT_VIEW_ALL_BOOKS, null);

    }

    @Override
    public void trackDeleteBookFailed(final FireBookDetails book, final String message) {
        Bundle bundle = getBookInfoBundle(book);
        bundle.putString(PARAM_ERROR_MESSAGE, message);
        firebaseAnalytics.logEvent(EVENT_DELETE_BOOK, bundle);
    }

    @Override
    public void trackDownloadBookStarted(final FireBookDetails book) {
        Bundle bundle = getBookInfoBundle(book);
        firebaseAnalytics.logEvent(EVENT_DOWNLOAD_BOOK_STARTED, bundle);
    }

    @Override
    public void trackDownloadBookFailed(final FireBookDetails book, String errorMsg) {
        Bundle bundle = getBookInfoBundle(book);
        bundle.putString(PARAM_ERROR_MESSAGE, errorMsg);
        firebaseAnalytics.logEvent(EVENT_DOWNLOAD_BOOK_FAILED, bundle);
    }

    @Override
    public void trackShareBook(final FireBookDetails book) {
        Bundle bundle = getBookInfoBundle(book);
        firebaseAnalytics.logEvent(EVENT_SHARE_BOOK, bundle);
    }

    @Override
    public void trackInvitePeople() {
        firebaseAnalytics.logEvent(EVENT_INVITE_PEOPLE, null);
    }

    public void setUserLanguage(String language) {
        firebaseAnalytics.setUserProperty(PARAM_CURRENT_LANGUAGE, language);

    }

    @Override
    public void trackSearchBooks(final String searchTerm) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SEARCH_TERM, searchTerm);
        firebaseAnalytics.logEvent(EVENT_SEARCH_BOOKS, bundle);
    }

    @Override
    public void trackSearchError(final String searchTerm, final String message) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SEARCH_TERM, searchTerm);
        firebaseAnalytics.logEvent(EVENT_SEARCH_BOOKS_ERROR, bundle);
    }

    @Override
    public void trackSearchBooksSuccess(final String searchTerm, final int numberSearchResults) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SEARCH_TERM, searchTerm);
        bundle.putInt(PARAM_SEARCH_RESULTS_COUNT, numberSearchResults);
        firebaseAnalytics.logEvent(EVENT_SEARCH_BOOKS_SUCCESS, bundle);
    }

    @Override
    public void trackSearchBooksNoResults(final String searchTerm) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SEARCH_TERM, searchTerm);
        firebaseAnalytics.logEvent(EVENT_SEARCH_BOOKS_NO_RESULTS, bundle);
    }

    @NonNull
    private Bundle getBookInfoBundle(final FireBookDetails book) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BOOK_NAME, book.getBookTitle());
        bundle.putString(PARAM_BOOK_ID, book.getId());
        return bundle;
    }
}
