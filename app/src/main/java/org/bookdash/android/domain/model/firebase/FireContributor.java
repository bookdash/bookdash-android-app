package org.bookdash.android.domain.model.firebase;


import android.text.TextUtils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bookdash.android.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.List;

public class FireContributor {
    public static final String TABLE_NAME = "bd_contributors";
    public static final String ROLES_SECTION = "roles";
    private String name;
    private String avatar;
    private List<String> roleIds = new ArrayList<>();
    private String id;
    private List<FireRole> fireRoles = new ArrayList<>();


    @SuppressWarnings("unused") // Used By Firebase
    public FireContributor() {
    }

    public FireContributor(final String name, final String avatar_url, final String id) {
        this.name = name;
        this.avatar = avatar_url;
        this.id = id;
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

    public StorageReference getFirebaseAvatar() {
        if (avatar == null) {
            return null;
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(FirebaseConfig.STORAGE_PREFIX);
        return storageRef.child(avatar);
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> role) {
        this.roleIds = role;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setActualRoles(final List<FireRole> fireRoles) {
        this.fireRoles = fireRoles;
    }

    public String getActualRolesFormatted() {
        if (fireRoles == null || fireRoles.isEmpty()) {
            return "";
        }

        return TextUtils.join(", ", fireRoles);

    }
}
