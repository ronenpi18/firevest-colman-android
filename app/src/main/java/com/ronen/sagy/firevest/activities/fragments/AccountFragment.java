package com.ronen.sagy.firevest.activities.fragments;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.SignupActivity;
//import com.ronen.sagy.firevest.adapters.SliderAdapter;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.services.model.Users;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private static final int IMAGE_REQUEST = 1;

    DatabaseViewModel databaseViewModel;
    View rootLayout;
    TextView titleName;
    TextView biodisplay;
    TextView fieldOfWork;
    ImageButton editProfile;
    ImageView btn_profile_image_change;
    private Uri imageUri;
    String timeStamp;
    private StorageTask uploadImageTask;
    private StorageReference fileReference;
    Boolean isUsername;
    byte[] dataImageByte;

    String username;
    String imageUrl;
    String userBio;
    AppDatabase db;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_account, container, false);
        init(rootLayout);

        try {
            fetchCurrentUserdata();
        } catch (Exception e) {
            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.loginActivity);

        }

        rootLayout.findViewById(R.id.edit_profile_pen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_setupProfileActivity);
            }
        });
        rootLayout.findViewById(R.id.signoutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignupActivity.class);
                startActivity(i);
                ((ActivityManager) getActivity().getApplicationContext()
                        .getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();

            }
        });
        return rootLayout;
    }

    private void init(View view) {

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication()))
                .get(DatabaseViewModel.class);

        titleName = view.findViewById(R.id.First_title_account);
        fieldOfWork = view.findViewById(R.id.second_title_account);
        biodisplay = view.findViewById(R.id.biodisplay);
        btn_profile_image_change = view.findViewById(R.id.profile_image_account);
//        UserDao userDao = db.userDao();
//        List<Users> user = userDao.getAll();
//        if (!user.isEmpty()) {
//            if (user.get(0).getInvestmentStageOrCapital() != null) {
//                titleName.setText(String.format("%s, %s", user.get(0).getUsername(), user.get(0).getInvestmentStageOrCapital()));
//            } else {
//                titleName.setText(user.get(0).getUsername());
//            }
//        }

        btn_profile_image_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        biodisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet(false);
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void fetchCurrentUserdata() {
        databaseViewModel.fetchingUserDataCurrent();
        databaseViewModel.fetchUserCurrentData.observe(this.getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    username = user.getUsername();
                    imageUrl = user.getImageUrl();
                    userBio = user.getBio();
                    if (user.getInvestmentStageOrCapital() != null) {
                        titleName.setText(String.format("%s, %s", user.getUsername(), user.getInvestmentStageOrCapital()));
                    } else {
                        titleName.setText(user.getUsername());
                    }
                    fieldOfWork.setText(user.getFieldOfWork());
                    biodisplay.setText(user.getBio());
                    if (imageUrl.equals("default")) {
                        btn_profile_image_change.setImageResource(R.drawable.sample_img);
                    } else {
                        Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(btn_profile_image_change);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "User not found..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
            timeStamp = Long.toString(tsLong);
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

    private void openBottomSheet(Boolean isUsername) {
        BottomSheetFragmentUsernameAndBioUpdate bottomSheetFragmentUsernameAndBioUpdate =
                new BottomSheetFragmentUsernameAndBioUpdate(
                        getActivity().getApplicationContext(),
                        isUsername
                );
        assert getFragmentManager() != null;
        bottomSheetFragmentUsernameAndBioUpdate.show(getFragmentManager(), "edit");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
