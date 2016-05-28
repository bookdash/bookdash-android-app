package org.bookdash.android.domain.pojo.firebase;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.domain.pojo.BookDetailParcelable;

import java.io.File;

public class FireBookDetails {
    public String bookTitle;
    public String bookUrl;
    public String bookCoverPageUrl;
    public boolean bookEnabled;
    public String bookLanguage;
    public String bookId;
    public String aboutBook;

    private boolean isDownloading;

    public FireBookDetails(String bookTitle, String bookUrl, String bookCoverPageUrl, boolean bookEnabled, String bookLanguage, String aboutBook) {
        this.bookTitle = bookTitle;
        this.bookUrl = bookUrl;
        this.bookCoverPageUrl = bookCoverPageUrl;
        this.bookEnabled = bookEnabled;
        this.bookLanguage = bookLanguage;
        this.aboutBook = aboutBook;
    }

    public FireBookDetails() {

    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookCoverUrl() {
        return bookCoverPageUrl;
    }


    public String getId() {
        return bookId;
    }

    public String getWebUrl() {
        return null;//todo
    }


    private String getFolderLocation(File file) {
        if (file != null && file.isDirectory() && (file.canRead())) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0 || files[0] == null) {
                return null;
            }
            return files[0].getAbsoluteFile().toString();
        }
        return null;
    }

    public String getFolderLocation() {
        return getFolderLocation(new File(BookDashApplication.FILES_DIR, getId() + File.separator));
    }

    public boolean isDownloadedAlready() {
        String folderLocation = getFolderLocation();
        if (folderLocation == null || folderLocation.isEmpty()) {
            return false;
        }
        File f = new File("", folderLocation);
        return f.exists();
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public BookDetailParcelable toBookParcelable() {
        BookDetailParcelable bookDetailParcelable = new BookDetailParcelable();
        bookDetailParcelable.setBookTitle(getBookTitle());
        bookDetailParcelable.setBookImageUrl(getBookCoverUrl());
        bookDetailParcelable.setBookDetailObjectId(getId());
        bookDetailParcelable.setWebUrl(getWebUrl());
        return bookDetailParcelable;
    }
}
