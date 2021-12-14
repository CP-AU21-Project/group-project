package com.example.group_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TodoListItem")
public class TodoListItem extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_TODO_LIST = "todoList";
    public static final String KEY_OBJECT_ID = "objectId";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_IS_COMPLETED = "isCompleted";
    public static final String KEY_TITLE = "title";


    /* ---------------------- access methods ----------------------- */

    public ParseUser getUser() { return getParseUser(KEY_USER); }
    public void setUser(ParseUser user) { put(KEY_USER, user); }

    public ParseObject getTodoList() { return getParseObject(KEY_TODO_LIST); }
    public void setTodoList(ParseObject todoList) { put(KEY_TODO_LIST, todoList); }

    public String getObjectId() { return getString(KEY_OBJECT_ID); }

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

}
