package com.example.group_project.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_project.PostsAdapter;
import com.example.group_project.R;
import com.example.group_project.TodoList;
import com.example.group_project.TodoListItem;
import com.example.group_project.ViewTodoListAdapter;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewTodoListFragment extends Fragment {
    public static final String TAG = "ViewTodoListFragment";

    protected List<TodoListItem> allTodoListItems;
    protected ViewTodoListAdapter adapter;
    private RecyclerView rvViewTodoListItems;
    private TextView tvViewTodoListTitle;
    private TextView tvViewTodoListCategory;
    private TextView tvViewTodoListDescription;
    private TodoList todoList;

    public ViewTodoListFragment(TodoList todoList) {
        // Required empty public constructor
        this.todoList = todoList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvViewTodoListItems = view.findViewById(R.id.rvViewTodoListItems);
        tvViewTodoListTitle = view.findViewById(R.id.tvViewTodoListTitle);
        tvViewTodoListCategory = view.findViewById(R.id.tvViewTodoListCategory);
        tvViewTodoListDescription = view.findViewById(R.id.tvViewTodoListDescription);

        // set data from parent TodoList
        tvViewTodoListTitle.setText(todoList.getTitle());
        tvViewTodoListCategory.setText("Category: " + todoList.getCategory());
        tvViewTodoListDescription.setText(todoList.getDescription());

        // set listener to delete items with swipe
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvViewTodoListItems);

        // query ParseDB for TodoListItems associated with parent TodoList
        allTodoListItems = new ArrayList<>();

        // steps to use the recycler view:
        // 0: create layout for one row in the list

        // 1: create the adapter
        adapter = new ViewTodoListAdapter(getContext(), allTodoListItems);

        // 2: create the data source (Post)
        // 3: set the adapter on the recycler view
        rvViewTodoListItems.setAdapter(adapter);

        // 4: set the layout manager on the recycler view
        rvViewTodoListItems.setLayoutManager(new LinearLayoutManager(getContext()));

        // display TodoListItems, update data source, notify adapter of new data
        queryTodoListItems();
    }

    private void queryTodoListItems() {
        ParseQuery<TodoListItem> query = ParseQuery.getQuery(TodoListItem.class);
        query.include(TodoListItem.KEY_TODO_LIST);
        query.whereEqualTo(TodoListItem.KEY_TODO_LIST, todoList);
        query.addAscendingOrder(TodoListItem.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<TodoListItem>() {
            @Override
            public void done(List<TodoListItem> todoListItems, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting TodoListItem", e);
                    return;
                }

                for (TodoListItem todoListItem : todoListItems) {
                    Log.i(TAG, "TodoListItem: " + todoListItem.getTitle());
                }

                allTodoListItems.addAll(todoListItems);
                adapter.notifyDataSetChanged();
            }
        });
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false; // not handling drag to move yet
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int position = viewHolder.getAdapterPosition();
                    TodoListItem todoListItem = allTodoListItems.remove(position);
                    deleteTodoListItem(todoListItem, position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();

            //adapter.notifyDataSetChanged();
        }
    };


    private void deleteTodoListItem(TodoListItem todoListItem, int position) {
        String objectId = todoListItem.getObjectId();
        String title = todoListItem.getTitle();
        todoListItem.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue delete TodoListItem: " + todoListItem.getObjectId(), e);
                    return;
                }

                Log.i(TAG, "Deleted TodoListItem: " + objectId + " title: " + title + " successfully");
                adapter.notifyItemRemoved(position);
            }
        });
//        ParseQuery<TodoListItem> query = ParseQuery.getQuery(TodoListItem.class);
//        query.include(TodoListItem.KEY_OBJECT_ID);
//        query.whereEqualTo(TodoListItem.KEY_TODO_LIST, todoListItem.getObjectId());
//
//        query.findInBackground(new FindCallback<TodoListItem>() {
//            @Override
//            public void done(List<TodoListItem> todoListItems, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Issue getting TodoListItem", e);
//                    return;
//                }
//
//                for (TodoListItem todoListItem : todoListItems) {
//                    Log.i(TAG, "Deleting TodoListItem: " + todoListItem.getTitle());
//                    todoListItem.deleteInBackground();
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//        });
    }
}
