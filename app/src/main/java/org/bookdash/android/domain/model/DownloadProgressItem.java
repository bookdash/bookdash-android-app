package org.bookdash.android.domain.model;

import org.bookdash.android.domain.model.gson.BookPages;

public class DownloadProgressItem {
    private long bytesTransferred;
    private long totalByteCount;
    private BookPages bookPages;

    public DownloadProgressItem(long bytesTransferred, long totalByteCount) {
        this.bytesTransferred = bytesTransferred;
        this.totalByteCount = totalByteCount;
    }

    public int getDownloadProgress() {
        return (int) (((float) (bytesTransferred) / (float) totalByteCount) * 100);
    }

    public boolean isComplete() {
        return bytesTransferred == totalByteCount;
    }

    public BookPages getBookPages() {
        return bookPages;
    }

    public void setBookPages(BookPages bookPages) {
        this.bookPages = bookPages;
    }
}
