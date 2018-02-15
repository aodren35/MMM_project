package com.mmm.pingmeat.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.mmm.pingmeat.HomeClientActivity;
import com.mmm.pingmeat.R;

public class SettingsClient extends Fragment {


    private TextView nameEditText;
    private ImageView imageView;
    private ProgressBar progressBar;
    private StorageReference storeRefImageProfile;

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
        nameEditText = (TextView) getActivity().findViewById(R.id.nameEditText);
        getActivity().setTitle("Settings client");

        // load profile
        fillUIFields();
        StorageReference storageRef = ((HomeClientActivity) getActivity()).getStorage().getReference();
        storeRefImageProfile = storageRef.child(((HomeClientActivity) getActivity()).getUser().getUid()+"/user.jpg");

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

}
