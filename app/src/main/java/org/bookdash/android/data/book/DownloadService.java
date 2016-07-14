package org.bookdash.android.data.book;

import org.bookdash.android.domain.model.DownloadProgressItem;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

import rx.Observable;

public interface DownloadService {

    Observable<DownloadProgressItem> downloadFile(FireBookDetails bookKey);
}
