package com.ronen.sagy.firevest.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.activities.fragments.AboutYouFormFragment;
import com.ronen.sagy.firevest.activities.fragments.AccountFragment;
import com.ronen.sagy.firevest.custom_widgets.TabbedDialog;
import com.ronen.sagy.firevest.services.model.AppDatabase;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;
import com.ronen.sagy.firevest.viewModel.SignInViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    EditText et_usernameSignIn;
    EditText et_emailIdSignIn;
    EditText et_pwdSignIn;
    Button btn_signIn;
    TextView textToLogin;
    SignInViewModel signInViewModel;
    DatabaseViewModel databaseViewModel;
    String emailId;
    String fieldOfWork;
    String invOrCap;
    String shortBio;

    String pwd;
    EditText txt_invRound;
    EditText txt_field_of_work;
    EditText txt_shortbio;
    String userName;
    Context context;
    String userId;
    String imageUrl;
    String timeStamp;
    FirebaseUser currentUser;
    ToggleButton userType;
    FrameLayout progressBarSignInFrame;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        listeners();
    }

    private void listeners() {
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

            @Override
            public void onClick(View v) {
                et_usernameSignIn.clearFocus();
                et_emailIdSignIn.clearFocus();
                et_pwdSignIn.clearFocus();
                v.startAnimation(buttonClick);

                emailId = et_emailIdSignIn.getText().toString();
                pwd = et_pwdSignIn.getText().toString();
                userName = et_usernameSignIn.getText().toString();
                if ((pwd.isEmpty() && emailId.isEmpty() && userName.isEmpty())) {
                    Toast.makeText(SignupActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                    et_usernameSignIn.requestFocus();
                } else if (userName.isEmpty()) {
                    et_usernameSignIn.setError("Please enter a username.");
                    et_usernameSignIn.requestFocus();
                } else if (emailId.isEmpty()) {
                    et_emailIdSignIn.setError("Please enter your Email Id.");
                    et_emailIdSignIn.requestFocus();
                } else if (pwd.isEmpty()) {
                    et_pwdSignIn.setError("Please set your password.");
                    et_pwdSignIn.requestFocus();
                } else {
                    progressBarSignInFrame.setVisibility(View.VISIBLE);
                    et_usernameSignIn.setClickable(false);
                    et_emailIdSignIn.setClickable(false);
                    et_pwdSignIn.setClickable(false);
                    textToLogin.setClickable(false);
                    dismissKeyboard();


                    signInUsers();
                }
            }
        });

        textToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void openDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_form, null);
        Button btn_okay = (Button) mView.findViewById(R.id.btn_okay);

        txt_invRound = (EditText) mView.findViewById(R.id.RoundOfInv_txt);
        txt_field_of_work = (EditText) mView.findViewById(R.id.field_txt);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        if (!fieldOfWork.equals("") || !invOrCap.equals("") || !shortBio.equals("")) {
            txt_field_of_work.setText(fieldOfWork);
            txt_invRound.setText(invOrCap);
            txt_shortbio.setText(shortBio);
        }
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldOfWork = txt_field_of_work.getText().toString();
                invOrCap = txt_invRound.getText().toString();
                shortBio = txt_shortbio.getText().toString();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void signInUsers() {
        signInViewModel.userSignIn(userName, emailId, pwd);
        signInViewModel.signInUser.observe(this, new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                if (!task.isSuccessful()) {
                    et_usernameSignIn.setClickable(true);
                    et_emailIdSignIn.setClickable(true);
                    et_pwdSignIn.setClickable(true);
                    textToLogin.setClickable(true);

                    progressBarSignInFrame.setVisibility(View.GONE);
                    et_emailIdSignIn.setText("");
                    et_pwdSignIn.setText("");
                    et_usernameSignIn.setText("");
                    et_usernameSignIn.requestFocus();

                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Toast.makeText(context, "Email Id already exists.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthWeakPasswordException weakPassword) {
                        Toast.makeText(context, "Password length should be more then six characters.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                        Toast.makeText(context, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "SignUp unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    getUserSession();
                    addUserInDatabase(userName, emailId, userId);

                    getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content,
                                    new AboutYouFormFragment(
                                            userName,
                                            emailId,
                                            pwd,
                                            userType.getText().toString()))
                            .commit();

                }
            }

        });
    }

    private void addUserInDatabase(String userName, String email, String idUser) {
        long tsLong = System.currentTimeMillis();
        timeStamp = Long.toString(tsLong);
        imageUrl = "default";
        userId = currentUser.getUid();
        String userTypeString = userType.getText().toString();

        databaseViewModel.addUserDatabase(userId, userName, email, timeStamp, imageUrl,
                userTypeString, txt_field_of_work.getText().toString(), shortBio, txt_invRound.getText().toString());
        databaseViewModel.successAddUserDb.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
//                    Toast.makeText(context, "user created successfully", Toast.LENGTH_SHORT).show();

                else {
                    Toast.makeText(context, "ERROR WHILE ADDING DATA IN DATABASE.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getUserSession() {
        signInViewModel.getUserFirebaseSession();
        signInViewModel.userFirebaseSession.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                currentUser = firebaseUser;
            }
        });

    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    private void init() {
        et_usernameSignIn = findViewById(R.id.et_signin_username);
        et_emailIdSignIn = findViewById(R.id.et_signin_email);
        et_pwdSignIn = findViewById(R.id.et_signin_password);
        btn_signIn = findViewById(R.id.btn_signin);
        textToLogin = findViewById(R.id.text_to_login);
        userType = findViewById(R.id.toggle_user_type);

        txt_invRound = (EditText) findViewById(R.id.RoundOfInv_txt);
        txt_field_of_work = (EditText) findViewById(R.id.field_txt);

        fieldOfWork = "";
        invOrCap = "";
        shortBio = "";
        context = SignupActivity.this;
        signInViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()))
                .get(SignInViewModel.class);
        databaseViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()))
                .get(DatabaseViewModel.class);
        progressBarSignInFrame = findViewById(R.id.progress_bar_signIn);
    }
}
