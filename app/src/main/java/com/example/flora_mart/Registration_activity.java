package com.example.flora_mart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Registration_activity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private TextView tv_sign_up;
    private EditText et_name, et_email, et_Give_password, et_confirm_password, et_user_phone_num;
    private Button bt_register_Button, bt_login_Button;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        tv_sign_up = findViewById(R.id.tv_sign_up);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_user_email);
        et_Give_password = findViewById(R.id.et_Give_password);
        et_confirm_password = findViewById(R.id.et_Confirm_password);
        et_user_phone_num = findViewById(R.id.et_user_phone);
        bt_register_Button = findViewById(R.id.bt_register_Button);
        bt_login_Button = findViewById(R.id.bt_login_Button);

        // Set button listeners
        bt_register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndDoRegister();
            }
        });

        bt_login_Button.setOnClickListener(v -> {
            Intent intent = new Intent(Registration_activity.this, login.class);
            startActivity(intent);
        });
    }

    private void validateAndDoRegister() {
        String username = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_Give_password.getText().toString().trim();
        String confirm_password = et_confirm_password.getText().toString().trim();
        String phone = et_user_phone_num.getText().toString().trim();

        // Validation patterns
        Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]{3,50}$");
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{2,6}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
        Pattern phonePattern = Pattern.compile("^01\\d{9}$");

        // Validation checks
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!namePattern.matcher(username).matches()) {
            et_name.setError("Name must be 3-50 characters long and contain only letters.");
            et_name.requestFocus();
            return;
        }

        if (!emailPattern.matcher(email).matches()) {
            et_email.setError("Invalid email format.");
            et_email.requestFocus();
            return;
        }

        if (!passwordPattern.matcher(password).matches()) {
            et_Give_password.setError("Password must be at least 6 characters with letters and digits.");
            et_Give_password.requestFocus();
            return;
        }

        if (!password.equals(confirm_password)) {
            et_confirm_password.setError("Passwords do not match!");
            et_confirm_password.requestFocus();
            return;
        }

        if (!phonePattern.matcher(phone).matches()) {
            et_user_phone_num.setError("Phone number must start with '01' and have 11 digits.");
            et_user_phone_num.requestFocus();
            return;
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful())
                       {
                           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                           assert user != null;
                           user.sendEmailVerification();
                           Toast.makeText(Registration_activity.this, " Register successfull", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(Registration_activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                       }
                    });
        }
    }
}