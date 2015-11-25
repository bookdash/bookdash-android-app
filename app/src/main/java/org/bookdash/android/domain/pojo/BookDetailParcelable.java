package org.bookdash.android.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BookDetailParcelable implements Parcelable {

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public String getBookDownloadUrl() {
        return bookDownloadUrl;
    }

    public void setBookDownloadUrl(String bookDownloadUrl) {
        this.bookDownloadUrl = bookDownloadUrl;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    private String bookImageUrl;
    private String bookDownloadUrl;
    private String bookTitle;

    public String getBookDetailObjectId() {
        return bookDetailObjectId;
    }

    public void setBookDetailObjectId(String bookDetailObjectId) {
        this.bookDetailObjectId = bookDetailObjectId;
    }

    private String bookDetailObjectId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookImageUrl);
        dest.writeString(this.bookDownloadUrl);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookDetailObjectId);
    }

    public BookDetailParcelable() {
    }

    protected BookDetailParcelable(Parcel in) {
        this.bookImageUrl = in.readString();
        this.bookDownloadUrl = in.readString();
        this.bookTitle = in.readString();
        this.bookDetailObjectId = in.readString();
    }

    public static final Parcelable.Creator<BookDetailParcelable> CREATOR = new Parcelable.Creator<BookDetailParcelable>() {
        public BookDetailParcelable createFromParcel(Parcel source) {
            return new BookDetailParcelable(source);
        }

        public BookDetailParcelable[] newArray(int size) {
            return new BookDetailParcelable[size];
        }
    };
}
