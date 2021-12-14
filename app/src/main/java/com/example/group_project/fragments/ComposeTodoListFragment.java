package com.example.group_project.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.group_project.R;
import com.example.group_project.TodoList;
import com.example.group_project.TodoListItem;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ComposeTodoListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ComposeTodoListFragment";

    // TodoList
    private Button btnTodoListSubmit;
    private EditText etTodoListCategory;
    private EditText etTodoListDescription;
    private EditText etTodoListTitle;

    // TodoListItems
    private ArrayAdapter<String> adapter;
    private Button btnAddTodoListItem;
    private EditText etTodoListItemDescription;
    private ListView lvTodoListItems;
    private List<String> allTodoListItems;

    public ComposeTodoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose_todo_list, container, false);
    }

    // here's where we set up all the views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TodoList
        etTodoListTitle = view.findViewById(R.id.etTodoListTitle);
        etTodoListCategory = view.findViewById(R.id.etTodoListCategory);
        etTodoListDescription = view.findViewById(R.id.etTodoListDescription);
        btnTodoListSubmit = view.findViewById(R.id.btnTodoListSubmit);

        // TodoListItems
        btnAddTodoListItem = view.findViewById(R.id.btnAddTodoListItem);
        etTodoListItemDescription = view.findViewById(R.id.etTodoListItemDescription);
        lvTodoListItems = view.findViewById(R.id.lvTodoListItems);
        allTodoListItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allTodoListItems);
        lvTodoListItems.setAdapter(adapter);
        lvTodoListItems.setOnItemClickListener(this);

        btnTodoListSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirm that a title was written before posting
                String title = etTodoListTitle.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String category = etTodoListCategory.getText().toString();
                if (category.isEmpty()) {
                    category = "none";
                }

                // confirm that a description was written before posting
                String description = etTodoListDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                // make the post with description and photo, for the current user
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveTodoList(title, category, description, currentUser); // actual uploading
            }
        });

        btnAddTodoListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = etTodoListItemDescription.getText().toString();
                if (task.isEmpty()) {
                    Toast.makeText(getContext(), "TodoList item cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // valid TodoList item, add to list
                allTodoListItems.add(task);
                adapter.notifyDataSetChanged();
                etTodoListItemDescription.setText("");
//                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allTodoListItems);
//                lvTodoListItems.setAdapter(adapter);
                //adapter.add(task);
            }
        });



    }

    private void saveTodoList(String title, String category, String description, ParseUser currentUser) {
        TodoList todoList = new TodoList();
        todoList.setCategory(category);
        todoList.setDescription(description);
        todoList.setTitle(title);
        todoList.setUser(currentUser);

        todoList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i(TAG, "TodoList saved successfully!");
                etTodoListCategory.setText("");
                etTodoListDescription.setText("");
                etTodoListTitle.setText("");
            }
        });

        /* convert items to TodoListItem and save all to Parse server -- */
        for (String task : allTodoListItems) {
            saveTodoListItems(task, currentUser, todoList);
        }

        // return to previous fragment
        getActivity().onBackPressed();
        //getActivity().getFragmentManager().popBackStackImmediate();
    }

    private void saveTodoListItems(String task, ParseUser currentUser, TodoList todoList) {
        TodoListItem todoListItem = new TodoListItem();
        todoListItem.setCategory(todoList.getCategory());
        todoListItem.setTitle(task);
        todoListItem.setTodoList(todoList);
        todoListItem.setUser(currentUser);

        todoListItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i(TAG, "TodoListItem: " + task + " saved successfully!");
                etTodoListItemDescription.setText("");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String task = adapterView.getItemAtPosition(i).toString();
        Log.i(TAG, "onItemClick: " + task);
    }
}
