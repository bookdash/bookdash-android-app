package org.bookdash.android.domain.model.gson;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/21 8:36 PM
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

public class Page implements Parcelable {

    @Expose
    private Integer pageNumber;
    @Expose
    private String image;
    @Expose
    private String audio;
    @Expose
    private String text;

    /**
     * @return The pageNumber
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber The pageNumber
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The audio
     */
    public String getAudio() {
        return audio;
    }

    /**
     * @param audio The audio
     */
    public void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.pageNumber);
        dest.writeString(this.image);
        dest.writeString(this.audio);
        dest.writeString(this.text);
    }

    public Page() {
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", image='" + image + '\'' +
                ", audio='" + audio + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    protected Page(Parcel in) {
        this.pageNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
        this.audio = in.readString();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    public Spanned getHtmlText() {
        if (TextUtils.isEmpty(getText())) {
            return SpannedString.valueOf("");
        }
        return Html.fromHtml(getText());
    }
}