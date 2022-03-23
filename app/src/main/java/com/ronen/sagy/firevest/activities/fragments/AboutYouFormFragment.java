package com.ronen.sagy.firevest.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.MainActivity;
import com.ronen.sagy.firevest.activities.SignupActivity;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;
import com.ronen.sagy.firevest.viewModel.SignInViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AboutYouFormFragment extends Fragment {
    String username, password, email;
    TextView continueToSignUp;
    EditText field_of_work_te;
    EditText round_te;
    DatabaseViewModel databaseViewModel;
    SignInViewModel signInViewModel;
    FirebaseUser currentUser;
    String invOrCap;
    String shortBio;
    String fieldOfWork;
    String usertype;
    ImageView btn_profile_image_change;
    byte[] dataImageByte;

    private StorageTask uploadImageTask;
    private StorageReference fileReference;
    private Uri imageUri;

    public AboutYouFormFragment() {
        // Required empty public constructor
    }

    public AboutYouFormFragment(String username, String email, String password, String usertype) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.requireActivity().getApplication()))
                .get(SignInViewModel.class);
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.requireActivity().getApplication()))
                .get(DatabaseViewModel.class);
    }

    public static AboutYouFormFragment createInstance() {
        AboutYouFormFragment fragment = new AboutYouFormFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_you_form, container, false);
        continueToSignUp = v.findViewById(R.id.choose_inst3);
        btn_profile_image_change = v.findViewById(R.id.choose_image);
        addUserInDatabase();

        continueToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        btn_profile_image_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        return v;
    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert bmp != null;
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);   //compression
            dataImageByte = baos.toByteArray();

            if (uploadImageTask != null && uploadImageTask.isInProgress()) {
                Toast.makeText(getActivity().getApplication().getApplicationContext(), "Upload in progress.", Toast.LENGTH_SHORT).show();
            } else {

                uploadImage();
            }
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading Image.");
        progressDialog.show();


        if (imageUri != null) {
            long tsLong = System.currentTimeMillis();
            String timeStamp = Long.toString(tsLong);
            databaseViewModel.fetchImageFileReference(timeStamp, imageUri, getContext());
            databaseViewModel.imageFileReference.observe(this, new Observer<StorageReference>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onChanged(StorageReference storageReference) {
                    fileReference = storageReference;
                    uploadImageTask = fileReference.putBytes(dataImageByte);  //image address
                    uploadImageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downLoadUri = task.getResult();
                                assert downLoadUri != null;
                                String mUri = downLoadUri.toString();
                                databaseViewModel.addImageUrlInDatabase("imageUrl", mUri);
                            } else {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });

        } else {
            Toast.makeText(getContext(), "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUserInDatabase() {

        signInViewModel.getUserFirebaseSession();
        signInViewModel.userFirebaseSession.observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                currentUser = firebaseUser;
            }
        });


//        long tsLong = System.currentTimeMillis();
//        String timeStamp = Long.toString(tsLong);
//        String userId = currentUser.getUid();
//
//        databaseViewModel.addUserDatabase(userId, this.username, this.email, timeStamp, imageUri.toString(),
//                this.usertype, this.fieldOfWork, this.shortBio, this.invOrCap);
//        databaseViewModel.successAddUserDb.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                } else {
//                }
//            }
//        });
    }

}