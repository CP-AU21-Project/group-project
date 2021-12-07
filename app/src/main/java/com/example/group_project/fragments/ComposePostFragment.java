package com.example.group_project.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.group_project.R;
import com.example.group_project.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposePostFragment extends Fragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 44;
    public static final String TAG = "ComposePostFragment";
    public static final String photoFileName = "photo.jpg";
    private File photoFile;

    private Button btnCaptureImage;
    private Button btnPostSubmit;
    private EditText etPostCategory;
    private EditText etPostDescription;
    private EditText etPostTitle;
    private ImageView ivPostImage;

    public ComposePostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose_post, container, false);
    }

    // here's where we set up all the views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        btnPostSubmit = view.findViewById(R.id.btnPostSubmit);
        etPostCategory = view.findViewById(R.id.etPostCategory);
        etPostDescription = view.findViewById(R.id.etPostDescription);
        etPostTitle = view.findViewById(R.id.etPostTitle);
        ivPostImage = view.findViewById(R.id.ivPostImage);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirm that a title was written before posting
                String title = etPostTitle.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String category = etPostCategory.getText().toString();
                if (category.isEmpty()) {
                    category = "none";
                }

                // confirm that a description was written before posting
                String description = etPostDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // make the post with description and photo, for the current user
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(title, category, description, currentUser, photoFile); // actual uploading
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.GROUP_PROJECT", photoFile);
        //intent.setClipData(ClipData.newRawUri("", fileProvider));
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // will be invoked when child program (camera) returns to parent application (parstagram)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // RESULT_OK indicates user actually took picture
                // at this point, have photo on disk from camera app
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                // TODO: RESIZE BITMAP, see section below

                // load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // failure to take image
                Toast.makeText(getContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String title, String category, String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setCategory(category);
        post.setDescription(description);
        post.setTitle(title);
        if (photoFile != null) post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);

        // save the new post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }

                // successful save
                Log.i(TAG, "Post saved successfully");
                etPostTitle.setText("");
                etPostCategory.setText("");
                etPostDescription.setText(""); // visually indicate to user that post saved by emptying text field
                ivPostImage.setImageResource(0); // 0 -> empty resource ID

                // return to previous fragment
                getActivity().onBackPressed();
            }
        });
    }
}