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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private OnPostListener mOnPostListener;

    public PostsAdapter(Context context, List<Post> posts, OnPostListener onPostListener) {
        this.context = context;
        this.posts = posts;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view, this.mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> posts) {
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    // so we can parameterize our class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private TextView tvCategory;
        private TextView tvDescription;
        private TextView tvTimestamp;
        private ImageView ivImage;
        OnPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivImage = itemView.findViewById(R.id.ivImage);
            this.onPostListener = onPostListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            // bind the post data to the view elements
            tvTitle.setText(post.getTitle());
            tvCategory.setText(post.getCategory());
            tvDescription.setText(post.getDescription());
            tvTimestamp.setText(post.getCreatedAt().toString());

            ivImage.setImageDrawable(null); // so that if there's laggy image from other post it's cleared
            ParseFile image = post.getImage();
            if (image != null) // some dummy images "aren't there", so can't get URL
            {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }

        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }
}
