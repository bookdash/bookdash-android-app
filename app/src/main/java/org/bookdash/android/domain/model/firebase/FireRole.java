package org.bookdash.android.domain.model.firebase;


public class FireRole {

    public static final String TABLE_NAME = "bd_roles";

    private String name;

    public FireRole(final String name) {

        this.name = name;
    }

    @SuppressWarnings("unused") //Used by Firebase
    public FireRole() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
