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
    private OnTodoListListener mOnTodoListListener;

    public TodoListsAdapter(Context context, List<TodoList> todoLists, OnTodoListListener onTodoListListener) {
        this.context = context;
        this.todoLists = todoLists;
        this.mOnTodoListListener = onTodoListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_list, parent, false);
        return new ViewHolder(view, this.mOnTodoListListener);
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvCategory;
        private TextView tvDescription;
        private TextView tvTimestamp;
        OnTodoListListener onTodoListListener;

        public ViewHolder(@NonNull View itemView, OnTodoListListener onTodoListListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            this.onTodoListListener = onTodoListListener;

            itemView.setOnClickListener(this);
        }

        public void bind(TodoList todoList) {
            // bind the post data to the view elements
            tvTitle.setText(todoList.getTitle());
            tvCategory.setText(todoList.getCategory());
            tvDescription.setText(todoList.getDescription());
            tvTimestamp.setText(todoList.getCreatedAt().toString());
        }

        @Override
        public void onClick(View view) {
            onTodoListListener.onTodoListClick(getAdapterPosition());
        }

    }

    public interface OnTodoListListener {
        void onTodoListClick(int position);
    }
}
