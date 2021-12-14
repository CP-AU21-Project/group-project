package com.example.group_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_BIO = "bio";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_PROFILE_PICTURE = "profilePicture";
    public static final String KEY_USERNAME = "username";


    /* ------------------ ACCESS METHODS ------------------- */

    public String getBio() { return getString(KEY_BIO); }
    public void setBio(String bio) { put(KEY_BIO, bio); }

    public Date getCreatedAt() { return getDate(KEY_CREATED_KEY); }

    public ParseFile getProfilePicture() {
        return getParseFile(KEY_PROFILE_PICTURE);
    }
    public void setProfilePicture(ParseFile profilePicture) {
        put(KEY_PROFILE_PICTURE, profilePicture);
    }

    public String getUsername() { return getString(KEY_USERNAME); }
    public void setUsername(String username) { put(KEY_USERNAME, username); }

}
