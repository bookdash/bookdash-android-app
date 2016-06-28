package org.bookdash.android.domain.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/17 6:39 PM
 */
@ParseClassName("Book")
@Deprecated
public class Book extends ParseObject {

    public String getDateBookCreated(){
        Date date = getDate("date_book_created");
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return simpleDateFormat.format(date);
    }

}
