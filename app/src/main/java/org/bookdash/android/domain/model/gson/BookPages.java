package org.bookdash.android.domain.model.gson;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/21 8:36 PM
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class BookPages implements Parcelable {

    @Expose
    private List<Page> pages = new ArrayList<>();

    /**
     * @return The pages
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * @param pages The pages
     */
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.pages);
    }

    public BookPages() {
    }

    protected BookPages(Parcel in) {
        this.pages = new ArrayList<>();
        in.readList(this.pages, Page.class.getClassLoader());
    }

    public static final Parcelable.Creator<BookPages> CREATOR = new Parcelable.Creator<BookPages>() {
        public BookPages createFromParcel(Parcel source) {
            return new BookPages(source);
        }

        public BookPages[] newArray(int size) {
            return new BookPages[size];
        }
    };
}

