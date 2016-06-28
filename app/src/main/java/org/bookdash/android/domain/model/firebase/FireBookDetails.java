package org.bookdash.android.domain.model.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import org.bookdash.android.BookDashApplication;

import java.io.File;

public class FireBookDetails implements Parcelable {
    public static final String TABLE_NAME = "bd_books";
    public static final String CREATED_AT_COL = "createdAt";
    public static final String BOOK_TITLE = "bookTitle";
    public static final String CONTRIBUTORS_NAME = "contributors";
    public String bookTitle;
    public String bookUrl;
    public String bookCoverPageUrl;
    public boolean bookEnabled;
    public String bookLanguage;
    public String bookId;
    public String bookDescription;

    private boolean isDownloading;

    public FireBookDetails(String bookTitle, String bookUrl, String bookCoverPageUrl, boolean bookEnabled, String bookLanguage, String bookDescription) {
        this.bookTitle = bookTitle;
        this.bookUrl = bookUrl;
        this.bookCoverPageUrl = bookCoverPageUrl;
        this.bookEnabled = bookEnabled;
        this.bookLanguage = bookLanguage;
        this.bookDescription = bookDescription;
    }

    public FireBookDetails() {

    }

    public boolean isDownloadedAlready() {
        String folderLocation = getFolderLocation();
        if (folderLocation == null || folderLocation.isEmpty()) {
            return false;
        }
        File f = new File("", folderLocation);
        return f.exists();
    }

    public String getFolderLocation() {
        return getFolderLocation(new File(BookDashApplication.FILES_DIR, getId() + File.separator));
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

    public String getId() {
        return bookId;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }


    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookCoverUrl() {
        return bookCoverPageUrl;
    }

    public String getWebUrl() {
        return null;//todo
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookUrl);
        dest.writeString(this.bookCoverPageUrl);
        dest.writeByte(this.bookEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.bookLanguage);
        dest.writeString(this.bookId);
        dest.writeString(this.bookDescription);
        dest.writeByte(this.isDownloading ? (byte) 1 : (byte) 0);
    }

    protected FireBookDetails(Parcel in) {
        this.bookTitle = in.readString();
        this.bookUrl = in.readString();
        this.bookCoverPageUrl = in.readString();
        this.bookEnabled = in.readByte() != 0;
        this.bookLanguage = in.readString();
        this.bookId = in.readString();
        this.bookDescription = in.readString();
        this.isDownloading = in.readByte() != 0;
    }

    public static final Creator<FireBookDetails> CREATOR = new Creator<FireBookDetails>() {
        @Override
        public FireBookDetails createFromParcel(Parcel source) {
            return new FireBookDetails(source);
        }

        @Override
        public FireBookDetails[] newArray(int size) {
            return new FireBookDetails[size];
        }
    };
}
