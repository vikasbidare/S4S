package com.example.android.s4s;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {

    /**
     * Declaration of views
     */
    EditText name;
    EditText email;
    EditText phone;
    EditText password;
    EditText confirmpassword;
    TextInputLayout name_layout, email_layout, phone_layout, password_layout, confirm_layout;
    Button register;

    private FirebaseDatabase database;
    private DatabaseReference Ref_name, Ref_email, Ref_phone, Ref_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        /**
         * Declaration and linking of views
         */
        name = findViewById(R.id.name_text_view);
        email = findViewById(R.id.email_text_view);
        phone = findViewById(R.id.phone_text_view);
        password = findViewById(R.id.password_text_view);
        confirmpassword = findViewById(R.id.confirm_password_text_view);
        name_layout = findViewById(R.id.layout_name);
        email_layout = findViewById(R.id.layout_email);
        phone_layout = findViewById(R.id.layout_phone);
        password_layout = findViewById(R.id.layout_password);
        confirm_layout = findViewById(R.id.layout_confirm);
        register = findViewById(R.id.register_button);

        database = FirebaseDatabase.getInstance();
        Ref_name = database.getReference().child("name");
        Ref_email = database.getReference().child("email");
        Ref_phone = database.getReference().child("phone");
        Ref_password = database.getReference().child("password");


        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmpty(name)) {
                        name_layout.setError("Name is required!");
                    } else
                        name_layout.setError(null);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmail(email) == false) {
                        email_layout.setError("Enter valid email!");
                    } else
                        email_layout.setError(null);
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isValidMobile(phone) == false) {
                        phone_layout.setError("Phone Number must have 10 digits!");
                    } else
                        phone_layout.setError(null);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isValidPassword(password) == false) {
                        password_layout.setError("Password must be at least 8 characters!");
                    } else
                        password_layout.setError(null);
                }
            }
        });

        confirmpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!password.getText().toString().equals(confirmpassword.getText().toString())) {
                        confirm_layout.setError("Password did not match!");
                    } else
                        confirm_layout.setError(null);
                }
            }
        });


        /** name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
        if (isEmpty(name)) {
        name_layout.setError("Name is required!");
        valid = false;
        } else
        name_layout.setError(null);
        }
        }
        }); **/

        /**
         * When register button is clicked ,
         * checkDataEntered() is evaluated
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataEntered();
                Ref_name.push().setValue(name.getText().toString());
                Ref_email.push().setValue(email.getText().toString());
                Ref_phone.push().setValue(phone.getText().toString());
                Ref_password.push().setValue(password.getText().toString());
            }
        });


    }


    /**
     * To check the validation of Email address
     *
     * @param text
     * @return '1' for accepted email and '0' for not accepted email
     */
    private boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * To Check if EditText View is empty
     *
     * @param text
     * @return '1' for empty and '0' for not empty
     */
    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    /**
     * To check the validation of Phone Number
     *
     * @param text
     * @return '1' if valid '0' if not valid
     */
    private boolean isValidMobile(EditText text) {
        CharSequence phone = text.getText().toString();
        return phone.length() == 10;

    }

    /**
     * To  check the validation of Password
     *
     * @param text
     * @return '1' if valid '0' if not valid
     */
    public static boolean isValidPassword(EditText text) {
        CharSequence password = text.getText().toString();
        return password.length() >= 8;
    }

    /**
     * To check the validity of all the views
     */
    void checkDataEntered() {

        boolean valid = true;
        /**
         * Error display for invalid name
         */
        if (isEmpty(name)) {
            name_layout.setError("Name is required!");
            valid = false;
        } else
            name_layout.setError(null);

        /**
         * Error display for invalid email
         */
        if (isEmail(email) == false) {
            email_layout.setError("Enter valid email!");
            valid = false;
        } else
            email_layout.setError(null);

        /**
         * Error display for invalid phone
         */
        if (isValidMobile(phone) == false) {
            phone_layout.setError("Phone Number must have 10 digits!");
            valid = false;
        } else
            phone_layout.setError(null);

        /**
         * Error display for invalid password
         */
        if (isValidPassword(password) == false) {
            password_layout.setError("Password must be at least 8 characters!");
            valid = false;
        } else
            password_layout.setError(null);

        /**
         * Password entered in confirm password view is checked for a match ,
         * with already entered password
         */
        if (!password.getText().toString().equals(confirmpassword.getText().toString())) {
            confirm_layout.setError("Password did not match!");
            valid = false;
        } else
            confirm_layout.setError(null);

        if (valid) {
            Toast.makeText(this, "Registration Successful",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    /**
     * openTerms() is evaluated when user clicks <u>terms</u>,
     * Terms and conditions are displayed
     *
     * @param view
     */
    public void openTerms(View view) {
        Intent i = new Intent(this, TermsActivity.class);
        startActivity(i);
    }


    /**
     * To check the status of radio button
     *
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_create_account:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_login:
                if (checked) {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    break;
                }
        }
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


}