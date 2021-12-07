package com.example.group_project;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    public static final String TAG = "ParseApplication";

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // register Parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(TodoList.class);
        ParseObject.registerSubclass(TodoListItem.class);


        // initialize Parse model
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("BegUyORZatrVQX7vG9snQBnwVrcis0QCG3rqO8BO")
                .clientKey("Rubc5ZuumSjFlSfnv0OKcwJ28BnHz5r6si4VqaOz")
                .server("https://parseapi.back4app.com")
                .build()
        );

        Log.i(TAG, "ParseApplication successfully initialized");
    }
}

