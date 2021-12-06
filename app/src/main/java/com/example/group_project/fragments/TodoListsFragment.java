package com.example.group_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_project.MainActivity;
import com.example.group_project.Post;
import com.example.group_project.PostsAdapter;
import com.example.group_project.R;
import com.example.group_project.TodoList;
import com.example.group_project.TodoListsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TodoListsFragment extends Fragment implements TodoListsAdapter.OnTodoListListener {
    public static final String TAG = "TodoListsFragment";

    private RecyclerView rvTodoLists;
    protected TodoListsAdapter adapter;
    protected List<TodoList> allTodoLists;

    public TodoListsFragment() {
        // required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_lists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTodoLists = view.findViewById(R.id.rvTodoLists);
        allTodoLists = new ArrayList<>();

        // steps to use the recycler view:
        // 0: create layout for one row in the list

        // 1: create the adapter
        adapter = new TodoListsAdapter(getContext(), allTodoLists, this);

        // 2: create the data source (Post)
        // 3: set the adapter on the recycler view
        rvTodoLists.setAdapter(adapter);

        // 4: set the layout manager on the recycler view
        rvTodoLists.setLayoutManager(new LinearLayoutManager(getContext()));

        // display posts, update data source, notify adapter of new data
        queryTodoLists();
    }

    protected void queryTodoLists() {
        // Specify which class to query
        ParseQuery<TodoList> query = ParseQuery.getQuery(TodoList.class);
        query.include(TodoList.KEY_USER);
        //query.setLimit(MIN_POSTS);
        query.addDescendingOrder(TodoList.KEY_CREATED_KEY);

        // query all TodoList objects for user from Parse db
        query.findInBackground(new FindCallback<TodoList>() {
            @Override
            public void done(List<TodoList> todoLists, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting todo lists", e);
                    return;
                }

                // acquire all posts for a given user to later display
                for (TodoList todoList : todoLists) {
                    Log.i(TAG, "todoList: " + todoList.getDescription() + ", username: " + todoList.getUser().getUsername());
                }

                // display users posts
                allTodoLists.addAll(todoLists);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Finished loading todo lists!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to set up onClickListener for a given TodoList, so that we can
     * navigate to that list and view the associated list items.
     * @param position
     */
    @Override
    public void onTodoListClick(int position) {
        TodoList todoList = allTodoLists.get(position); // gets the current
        Log.i(TAG, "clicked on position: " + position + " TodoList: " + todoList.getTitle());
        // Intent intent = new Intent(this, );
        // startActivity(intent);
        // TODO: navigate to target activity/fragment here
    }
}
