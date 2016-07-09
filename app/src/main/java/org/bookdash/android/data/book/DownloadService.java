package org.bookdash.android.data.book;

import org.bookdash.android.domain.model.DownloadProgressItem;

import rx.Observable;

public interface DownloadService {

    Observable<DownloadProgressItem> downloadFile(String url);
}
