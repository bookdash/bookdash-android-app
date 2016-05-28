package org.bookdash.android.domain.pojo.firebase;


public class FireContributor {
    private String name;
    private String profilePicUrl;
    private String role;

    public FireContributor(String name, String profilePicUrl, String role) {
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.role = role;
    }

    public FireContributor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
