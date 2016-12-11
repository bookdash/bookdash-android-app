package org.bookdash.android.data.tracking;


import org.bookdash.android.domain.model.firebase.FireBookDetails;

public interface Analytics {

    void trackLanguageChange(String newLanguage);

    void trackViewBooksDownloaded();

    void trackViewBook(FireBookDetails book);

    void trackDeleteBook(FireBookDetails book);

    void trackRateAppClick();

    void trackViewContributors();

    void trackViewAllBooks();

    void trackDeleteBookFailed(FireBookDetails bookDetail, String message);

    void trackDownloadBookStarted(FireBookDetails bookInfo);

    void trackDownloadBookFailed(FireBookDetails bookInfo, String errorMsg);

    void trackShareBook(FireBookDetails bookInfo);

    void trackInvitePeople();

    void setUserLanguage(String language);

    void trackSearchBooks(String searchTerm);

    void trackSearchError(String searchTerm, String message);

    void trackSearchBooksSuccess(String searchTerm, int numberSearchResults);

    void trackSearchBooksNoResults(String searchTerm);
}
