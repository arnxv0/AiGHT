package kan.aight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String usernameEntered;

    TextView blackBarSelected;
    TextView blackBarSelectedSignup;

    TextView signin;
    TextView signup;
    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button loginSignup;
    Boolean login;

    String passwordEntered;
    String confirmPasswordEntered;
    String emailEntered;

    TextView signUpMessage;

    ConstraintLayout constraintLayoutLoginPage;

    public void makeShortToast(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void loginAccount() {
        if (ParseUser.getCurrentUser() == null) {
            ParseUser.logInInBackground(usernameEntered, passwordEntered, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.i("clicked", "yes");

                        makeShortToast("Sign in successful!");
                        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        makeShortToast(e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textViewSignup) {

            login = false;
            username.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            confirmPassword.setVisibility(View.VISIBLE);
            loginSignup.setText("Sign Up");
            signUpMessage.setText("Sign up to see what your friends have to say!");

            int[] location1 = new int[2];
            signup.getLocationOnScreen(location1);
            int x1 = location1[0];
            blackBarSelected.animate().setDuration(300).x(x1);

        } else if (v.getId() == R.id.textViewSignin) {

            login = true;
            username.setVisibility(View.VISIBLE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            confirmPassword.setVisibility(View.GONE);
            loginSignup.setText("Sign In");
            signUpMessage.setText("Hey, Welcome back!");


            int[] location1 = new int[2];
            signin.getLocationOnScreen(location1);
            int x1 = location1[0];
            blackBarSelected.animate().setDuration(300).x(x1);

        } else if (v.getId() == R.id.buttonSignupLogin) {

            usernameEntered = username.getText().toString();
            emailEntered = email.getText().toString();
            passwordEntered = password.getText().toString();
            confirmPasswordEntered = confirmPassword.getText().toString();

            if (login) {
                int errorFlag = 0;

                if(usernameEntered.isEmpty()){
                    errorFlag += 1;
                    username.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    username.setHint("Please Enter Username!");
                    username.setHintTextColor(Color.RED);
                } else {
                    username.setBackground(getDrawable(R.drawable.custom_rectangle));
                    username.setHint("Username");
                    username.setHintTextColor(Color.BLACK);
                }

                if(passwordEntered.isEmpty()){
                    errorFlag += 1;
                    password.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    password.setHint("Please Enter Password!");
                    password.setHintTextColor(Color.RED);


                } else {
                    password.setBackground(getDrawable(R.drawable.custom_rectangle));
                    password.setHint("Password");
                    password.setHintTextColor(Color.BLACK);

                }

                if(errorFlag == 0){
                    loginAccount();
                }

            } else {
                int errorFlag = 0;

                if(usernameEntered.isEmpty()){
                    errorFlag += 1;
                    username.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    username.setHint("Please Enter Username!");
                    username.setHintTextColor(Color.RED);
                } else {
                    username.setBackground(getDrawable(R.drawable.custom_rectangle));
                    username.setHint("Username");
                    username.setHintTextColor(Color.BLACK);
                }

                if(emailEntered.isEmpty()){
                    errorFlag += 1;
                    email.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    email.setHint("Please Enter Email!");
                    email.setHintTextColor(Color.RED);

                } else {
                    email.setBackground(getDrawable(R.drawable.custom_rectangle));
                    email.setHint("Email");
                    email.setHintTextColor(Color.BLACK);

                }

                if(passwordEntered.isEmpty()){
                    errorFlag += 1;
                    password.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    password.setHint("Please Enter Password!");
                    password.setHintTextColor(Color.RED);


                } else {
                    password.setBackground(getDrawable(R.drawable.custom_rectangle));
                    password.setHint("Password");
                    password.setHintTextColor(Color.BLACK);

                }

                if (confirmPasswordEntered.isEmpty()){
                    errorFlag += 1;
                    confirmPassword.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                    confirmPassword.setHint("Please Re-enter Password!");
                    confirmPassword.setHintTextColor(Color.RED);

                } else {
                    confirmPassword.setBackground(getDrawable(R.drawable.custom_rectangle));
                    confirmPassword.setHint("Re-enter Password");
                    confirmPassword.setHintTextColor(Color.BLACK);
                }

                if (errorFlag==0){

                    if(passwordEntered.equals(confirmPasswordEntered)) {
                        ParseUser user = new ParseUser();
                        user.setUsername(usernameEntered);
                        user.setPassword(passwordEntered);
                        user.setEmail(emailEntered);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    makeShortToast("Sign up successful!");
                                    loginAccount();
                                    Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    makeShortToast(e.getMessage());
                                }
                            }
                        });
                    } else {
                        makeShortToast("Passwords do not match!");
                        confirmPassword.getText().clear();
                        confirmPassword.setBackground(getDrawable(R.drawable.custom_rectangle_red));
                        confirmPassword.setHint("Please Re-enter Password!");
                        confirmPassword.setHintTextColor(Color.RED);
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.editTextTitle);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        loginSignup = findViewById(R.id.buttonSignupLogin);

        blackBarSelected = findViewById(R.id.textViewBlackBarSelected);
        blackBarSelectedSignup = findViewById(R.id.textViewBlackBarSelectedSignup);

        signin = findViewById(R.id.textViewSignin);
        signup = findViewById(R.id.textViewSignup);

        signUpMessage = findViewById(R.id.textViewSignupMessage);

        constraintLayoutLoginPage = findViewById(R.id.constraintLayoutLoginPage);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        loginSignup.setOnClickListener(this);
        constraintLayoutLoginPage.setOnClickListener(this);



        login = true;

    }



}
