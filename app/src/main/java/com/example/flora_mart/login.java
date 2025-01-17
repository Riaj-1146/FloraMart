package com.example.flora_mart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    private TextView tv_sign_in;
    private EditText et_email, et_Give_password;
    private Button bt_register_Button, bt_login_Button;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        tv_sign_in = findViewById(R.id.tv_sign_in);
        et_email = findViewById(R.id.et_user_email);
        et_Give_password = findViewById(R.id.et_Give_password);
        bt_login_Button = findViewById(R.id.bt_Login_Button);
        bt_register_Button = findViewById(R.id.bt_Register_Button);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set login button click listener
        bt_login_Button.setOnClickListener(v -> validateAndDoLogin());

        // Set register button click listener to navigate to registration activity
        bt_register_Button.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), Registration_activity.class);
            startActivity(intent);
        });

    }

    private void validateAndDoLogin() {
        String email = et_email.getText().toString().trim();
        String password = et_Give_password.getText().toString().trim();

        // Regex patterns
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{2,6}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");

        // Empty field validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Both fields must be filled out!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email format validation
        else if (!emailPattern.matcher(email).matches()) {
            Toast.makeText(this, "Incorrect email address!", Toast.LENGTH_SHORT).show();
            et_email.requestFocus();
            return;
        }

        // Password format validation
        else if (!passwordPattern.matcher(password).matches()) {
            Toast.makeText(this, "Password must be at least 6 characters with a number and a letter.", Toast.LENGTH_SHORT).show();
            et_Give_password.requestFocus();
            return;
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user != null && user.isEmailVerified())
                                {
                                    Intent intent = new Intent(getApplicationContext(), buyer_home.class);
                                    startActivity(intent);
                                    //finish();
                                }
                                else{
                                    Toast.makeText(login.this, " Verify email first", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(login.this, " Autentication failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

}
