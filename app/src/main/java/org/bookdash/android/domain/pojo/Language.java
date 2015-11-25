package org.bookdash.android.domain.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/16 7:37 PM
 */
@ParseClassName(Language.LANGUAGE_TABLE_NAME)
public class Language extends ParseObject {

    public static final String LANGUAGE_TABLE_NAME = "Language";
    public static final String LANG_CODE_COL = "language_abbreviation";
    private static final String LANG_NAME_COL = "language_name";
    public static final String LANGUAGE_ID = "language_id";

    public Language() {
    }

    public Language(String languageName, String code, String languageId) {
        super(LANGUAGE_TABLE_NAME);
        put(LANG_CODE_COL, code);
        put(LANG_NAME_COL, languageName);
        put(LANGUAGE_ID, languageId);

    }

    public String getLanguageCode() {
        return getString(LANG_CODE_COL);
    }

    public String getLanguageName() {
        return getString(LANG_NAME_COL);
    }

    public String getLanguageId() {
        return getString(LANGUAGE_ID);
    }

    @Override
    public String toString() {
        return getLanguageName();
    }
}
