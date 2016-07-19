package org.bookdash.android;

import org.bookdash.android.data.book.DownloadService;
import org.bookdash.android.domain.model.DownloadProgressItem;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

import rx.Observable;

/**
 * Created by rebeccafranks on 16/07/19.
 */
public class FakeDownloadService implements DownloadService {
    @Override
    public Observable<DownloadProgressItem> downloadFile(final FireBookDetails bookKey) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteDownload(final FireBookDetails book) {
        return null;
    }
}
