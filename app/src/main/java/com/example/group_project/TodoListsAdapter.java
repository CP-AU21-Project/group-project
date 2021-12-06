package com.example.group_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class TodoListsAdapter extends RecyclerView.Adapter<TodoListsAdapter.ViewHolder> {

    private Context context;
    private List<TodoList> todoLists;

    public TodoListsAdapter(Context context, List<TodoList> todoLists) {
        this.context = context;
        this.todoLists = todoLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListsAdapter.ViewHolder holder, int position) {
        TodoList todoList = todoLists.get(position);
        holder.bind(todoList);
    }

    @Override
    public int getItemCount() {
        return todoLists.size();
    }

    // so we can parameterize our class
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvCategory;
        private TextView tvDescription;
        private TextView tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        public void bind(TodoList todoList) {
            // bind the post data to the view elements
            tvTitle.setText(todoList.getTitle());
            tvCategory.setText(todoList.getCategory());
            tvDescription.setText(todoList.getDescription());
            tvTimestamp.setText(todoList.getCreatedAt().toString());


        }
    }
}
