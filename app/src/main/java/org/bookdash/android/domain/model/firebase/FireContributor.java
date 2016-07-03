package org.bookdash.android.domain.model.firebase;


import java.util.List;

public class FireContributor {
    public static final String TABLE_NAME = "bd_contributors";
    private String name;
    private String avatar;
    private List<String> roles;
    private String id;

    public FireContributor(String name, String profilePicUrl, List<String> role) {
        this.name = name;
        this.avatar = profilePicUrl;
        this.roles = role;
    }

    public FireContributor(final String name, final String profilePicUrl, final List<String> role, final String id) {
        this.name = name;
        this.avatar = profilePicUrl;
        this.roles = role;
        this.id = id;
    }

    @SuppressWarnings("unused") // Used By Firebase
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> role) {
        this.roles = role;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
