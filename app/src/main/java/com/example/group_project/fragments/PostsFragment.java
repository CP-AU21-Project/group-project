package com.example.group_project.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_project.Post;
import com.example.group_project.PostsAdapter;
import com.example.group_project.R;
import com.example.group_project.TodoList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment implements PostsAdapter.OnPostListener {

    public static final String TAG = "PostsFragment";
    private static final int MIN_POSTS = 20;

    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();

        // steps to use the recycler view:
        // 0: create layout for one row in the list

        // 1: create the adapter
        adapter = new PostsAdapter(getContext(), allPosts, this);

        // 2: create the data source (Post)
        // 3: set the adapter on the recycler view
        rvPosts.setAdapter(adapter);

        // 4: set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // display posts, update data source, notify adapter of new data
        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        //query.setLimit(MIN_POSTS);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);

        // query all Post objects for user from Parse db
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting posts", e);
                    return;
                }

                // acquire all posts for a given user to later display
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // display users posts
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Method to set up onClickListener for a given TodoList, so that we can
     * navigate to that list and view the associated list items.
     * @param position
     */
    @Override
    public void onPostClick(int position) {
        Post post = allPosts.get(position); // gets the current
        Log.i(TAG, "clicked on position: " + position + " Post: " + post.getTitle());
        // Intent intent = new Intent(this, );
        // startActivity(intent);
        // TODO: navigate to target activity/fragment here
    }
}
