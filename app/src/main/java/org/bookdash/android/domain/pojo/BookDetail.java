package org.bookdash.android.domain.pojo;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.bookdash.android.BookDashApplication;

import java.io.File;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 2:10 PM
 */
@ParseClassName(BookDetail.BOOK_TABLE_NAME)
public class BookDetail extends ParseObject {

    public static final String BOOK_TABLE_NAME = "BookDetails";
    public static final String BOOK_TITLE_COL = "book_title";
    public static final String BOOK_COVER_PAGE_URL_COL = "book_cover_page_url";
    public static final String BOOK_DOWNLOAD_FILE_COL = "book_download_file";
    public static final String BOOK_LANGUAGE_COL = "book_language";
    public static final String BOOK_ID_COL = "book_id";
    public static final String ABOUT_BOOK_COL = "aboutBook";
    public static final String OBJECT_ID = "objectId";
    public static final String BOOK_ENABLED_COL = "book_enabled";
    public static final String CREATED_AT_COL = "createdAt";
    public static final String BOOK_INFO_FILE_NAME = "bookdetails.json";


    private boolean isDownloading = false;

    public BookDetail() {
    }

    public BookDetail(String title, String bookCoverUrl, String objectId, Language languageId) {
        super(BOOK_TABLE_NAME);
        put(BOOK_TITLE_COL, title);
        put(BOOK_COVER_PAGE_URL_COL, bookCoverUrl);
        put(BOOK_LANGUAGE_COL, languageId);
        put(OBJECT_ID, objectId);
        //put();
    }

    public String getBookTitle() {
        return getString(BOOK_TITLE_COL);
    }

    public String getBookCoverUrl() {
        return getString(BOOK_COVER_PAGE_URL_COL);
    }

    public ParseFile getBookFile() {
        return getParseFile(BOOK_DOWNLOAD_FILE_COL);
    }

    public Language getLanguage() {
        return (Language) get(BOOK_LANGUAGE_COL);
    }

    public Book getBook() {
        return (Book) get(BOOK_ID_COL);
    }

    public String getAboutBook() {
        return getString(ABOUT_BOOK_COL);
    }

    public String getBookDetailId() {
        return getString(OBJECT_ID);
    }

    public String getFolderLocation(String filesDir) {
        return getFolderLocation(new File(filesDir, getObjectId() + File.separator));
    }

    private String getFolderLocation(File file) {
        if (file.isDirectory() && (file.canRead())) {
            File[] files = file.listFiles();
            return files[0].getAbsoluteFile().toString();
        }
        return null;
    }

    public boolean isDownloadedAlready() {
        String targetLocation = BookDashApplication.FILES_DIR + File.separator + getObjectId();
        File f = new File("", targetLocation);
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
        bookDetailParcelable.setBookDetailObjectId(getObjectId());
        return bookDetailParcelable;
    }
}