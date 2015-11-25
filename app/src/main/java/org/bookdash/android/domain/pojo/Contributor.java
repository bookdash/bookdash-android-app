package org.bookdash.android.domain.pojo;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Contributor")
public class Contributor extends ParseObject {

    public static final String CONTRIBUTOR_TABLE = "Contributor";
    public static final String COL_NAME = "name";
    public static final String COL_ROLE = "role";

    public Contributor() {
    }

    public Contributor(String name, String role) {
        super(CONTRIBUTOR_TABLE);
        put(COL_NAME, name);
        put(COL_ROLE, role);
    }

    public String getName(){
        return getString(COL_NAME);
    }
    public String getRole(){
        return getString(COL_ROLE);
    }

    public ParseFile getAvatar(){
        return getParseFile("avatar");
    }

}
