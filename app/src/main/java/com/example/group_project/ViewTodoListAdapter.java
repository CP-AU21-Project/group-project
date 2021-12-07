package com.example.group_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewTodoListAdapter extends RecyclerView.Adapter<ViewTodoListAdapter.ViewHolder> {

    private Context context;
    private List<TodoListItem> todoListItems;

    public ViewTodoListAdapter(Context context, List<TodoListItem> todoListItems) {
        this.context = context;
        this.todoListItems = todoListItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoListItem todoListItem = todoListItems.get(position);
        holder.bind(todoListItem);
    }

    @Override
    public int getItemCount() {
        return todoListItems.size();
    }

    // so we can parameterize our class
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvViewTodoListItemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvViewTodoListItemTitle = itemView.findViewById(R.id.tvViewTodoListItemTitle);
        }

        public void bind(TodoListItem todoListItem) {
            // bind the post data to the view elements
            tvViewTodoListItemTitle.setText(todoListItem.getTitle());
        }
    }
}
