package com.example.group_project.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment implements PostsAdapter.OnPostListener {

    public static final String TAG = "PostsFragment";
    private static final int MIN_POSTS = 20;

    private Button btnComposePost;
    protected List<Post> allPosts;
    protected PostsAdapter adapter;
    private RecyclerView rvPosts;

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
        btnComposePost = view.findViewById(R.id.btnComposePost);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();

        // set onClickListener to create a new Post
        btnComposePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: btnComposePost");
                // TODO: navigate to ComposePostFragment
                ComposePostFragment composePostFragment = new ComposePostFragment();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer, composePostFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


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
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                    Log.i(TAG, "Post: " + post.getDescription() + ", photo: " + post.getImage());
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
        // TODO: navigate to EditPostFragment or ComposePostFragment (modified to accept
        //       arguments for initial EditText field values
    }
}
