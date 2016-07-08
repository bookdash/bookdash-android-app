package org.bookdash.android.data.book;

import rx.Observable;

public interface DownloadService {

    Observable<DownloadServiceImpl.DownloadProgressItem> downloadFile(String url);
}
