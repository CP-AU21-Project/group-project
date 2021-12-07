package com.example.group_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TodoList")
public class TodoList extends ParseObject {
    public static final String KEY_USER = "user";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IS_COMPLETED = "isCompleted";
    public static final String KEY_TITLE = "title";


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }


    /* ---------------------- access methods for normal fields -------------- */

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public boolean getIsCompleted() { return getBoolean(KEY_IS_COMPLETED); }

    public void setIsCompleted(boolean isCompleted) { put(KEY_IS_COMPLETED, isCompleted); }

}