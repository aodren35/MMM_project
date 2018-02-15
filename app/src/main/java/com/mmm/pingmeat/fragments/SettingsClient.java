package com.mmm.pingmeat.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mmm.pingmeat.HomeClientActivity;
import com.mmm.pingmeat.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class SettingsClient extends Fragment {


    private TextView nameEditText;
    private ImageView imageView;
    private ProgressBar progressBar;
    private StorageReference storeRefImageProfile;

    static final int REQUEST_TAKE_PHOTO = 2;
    private String mCurrentPhotoPath;
    private Uri photoURI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_settings_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        imageView.setOnClickListener(takePicture);
        nameEditText = (TextView) getActivity().findViewById(R.id.nameEditText);
        getActivity().setTitle("Settings client");

        // load profile
        fillUIFields();
        StorageReference storageRef = ((HomeClientActivity) getActivity()).getStorage().getReference();
        storeRefImageProfile = storageRef.child(((HomeClientActivity) getActivity()).getUser().getUid()+"/user.jpg");

    }

    ImageView.OnClickListener takePicture = new ImageView.OnClickListener() {
        public void onClick(View v) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    };

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "user";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void fillUIFields() {
        FirebaseUser user = ((HomeClientActivity) getActivity()).getUser();
        if (user != null) {
            nameEditText.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                Log.d("LOL", "DANS NON NUL" + user.getPhotoUrl());


                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storeRefImageProfile)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .error(R.drawable.user)
                        .listener(new RequestListener<StorageReference, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                scheduleStartPostponedTransition(imageView);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                scheduleStartPostponedTransition(imageView);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);

                /*Glide.with(this)
                        .load(user.getPhotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .error(R.drawable.user)
                        .listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                scheduleStartPostponedTransition(imageView);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                scheduleStartPostponedTransition(imageView);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);*/
            } else {
                Log.d("LOL", "DANS NUL");

                progressBar.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.user);
            }
        }
    }

    private void scheduleStartPostponedTransition(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LOL", "DANS NUL" + (data == null));
        if (requestCode == this.REQUEST_TAKE_PHOTO) {

            StorageReference riversRef = ((HomeClientActivity) getActivity()).getStorage().getReference().child(((HomeClientActivity) getActivity()).getUser().getUid() + "/user.jpg");
            UploadTask uploadTask = riversRef.putFile(this.photoURI);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getActivity(), "Problème d'upload", Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getActivity(), "Problème d'upload", Toast.LENGTH_SHORT).show();

        }
    }

}
