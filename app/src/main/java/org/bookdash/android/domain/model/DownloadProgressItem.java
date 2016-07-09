package org.bookdash.android.domain.model;

public class DownloadProgressItem {
    long bytesTransferred;
    long totalByteCount;

    public DownloadProgressItem(long bytesTransferred, long totalByteCount) {
        this.bytesTransferred = bytesTransferred;
        this.totalByteCount = totalByteCount;
    }

    public int getDownloadProgress() {
        return (int) (((float) (bytesTransferred) / (float) totalByteCount) * 100);
    }
}
