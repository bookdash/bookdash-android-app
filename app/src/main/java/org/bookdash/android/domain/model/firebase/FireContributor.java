package org.bookdash.android.domain.model.firebase;


public class FireContributor {
    public static final String TABLE_NAME = "bd_contributors";
    private String name;
    private String avatar;
    private String role;
    private String id;

    public FireContributor(String name, String profilePicUrl, String role) {
        this.name = name;
        this.avatar = profilePicUrl;
        this.role = role;
    }

    public FireContributor(final String name, final String profilePicUrl, final String role, final String id) {
        this.name = name;
        this.avatar = profilePicUrl;
        this.role = role;
        this.id = id;
    }

    public FireContributor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
