package org.bookdash.android.data.analytics;

import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

/**
 * @author rebeccafranks
 * @date 2016/12/05
 */

public class MockAnalytics implements Analytics {
    @Override
    public void trackLanguageChange(final String newLanguage) {

    }

    @Override
    public void trackViewBooksDownloaded() {

    }

    @Override
    public void trackViewBook(final FireBookDetails book) {

    }

    @Override
    public void trackDeleteBook(final FireBookDetails book) {

    }

    @Override
    public void trackRateAppClick() {

    }

    @Override
    public void trackViewContributors() {

    }

    @Override
    public void trackViewAllBooks() {

    }

    @Override
    public void trackDeleteBookFailed(final FireBookDetails bookDetail, final String message) {

    }

    @Override
    public void trackDownloadBookStarted(final FireBookDetails bookInfo) {

    }

    @Override
    public void trackDownloadBookFailed(final FireBookDetails bookInfo, final String errorMsg) {

    }

    @Override
    public void trackShareBook(final FireBookDetails bookInfo) {

    }

    @Override
    public void trackInvitePeople() {

    }

    @Override
    public void setUserLanguage(final String language) {

    }

    @Override
    public void trackSearchBooks(final String searchTerm) {

    }

    @Override
    public void trackSearchError(final String searchTerm, final String message) {

    }

    @Override
    public void trackSearchBooksSuccess(final String searchTerm, final int numberSearchResults) {

    }

    @Override
    public void trackSearchBooksNoResults(final String searchTerm) {

    }

    @Override
    public void trackViewHelpTutorialAgain() {
        
    }

    @Override
    public void trackUserToggleNewBookNotifications(final boolean onOff) {

    }
}
