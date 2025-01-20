package com.example.flora_mart;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Registration_activity extends AppCompatActivity {

    private TextView tv_sign_up, tv_Instruction;
    private EditText et_name, et_email, et_user_password, et_user_confirm_password, et_user_phone_num;
    private Button bt_register_Button;
    private ProgressBar progressBar;

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
        tv_Instruction = findViewById(R.id.tv_Instruction);
        et_name = findViewById(R.id.et_name);
        et_user_password = findViewById(R.id.et_user_password);
        et_user_confirm_password = findViewById(R.id.et_user_confirm_password);
        et_email = findViewById(R.id.et_email);
        et_user_phone_num = findViewById(R.id.et_user_phone);
        bt_register_Button = findViewById(R.id.bt_sign_up_Button);

        progressBar = findViewById(R.id.progressBar_registration);

        // Hide ProgressBar initially
        progressBar.setVisibility(View.GONE);

        // Register Button
        bt_register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndDoRegister();
            }
        });

        // Instruction TextView Click Listener
        tv_Instruction.setOnClickListener(v -> {
            Intent intent = new Intent(Registration_activity.this, login.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateAndDoRegister() {
        String username = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_user_password.getText().toString().trim();
        String confirm_password = et_user_confirm_password.getText().toString().trim();
        String phone = et_user_phone_num.getText().toString().trim();

        // Validation patterns
        Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]{3,20}$");
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{2,6}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
        Pattern phonePattern = Pattern.compile("^01\\d{9}$");

        // Validation checks
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!namePattern.matcher(username).matches()) {
            et_name.setError("Name must be 3-20 characters long and contain only letters.");
            et_name.requestFocus();
            return;
        }

        if (!emailPattern.matcher(email).matches()) {
            et_email.setError("Invalid email format.");
            et_email.requestFocus();
            return;
        }

        if (!passwordPattern.matcher(password).matches()) {
            et_user_password.setError("Password must be at least 6 characters with letters and digits.");
            et_user_confirm_password.requestFocus();
            return;
        }

        if (!password.equals(confirm_password)) {
            et_user_confirm_password.setError("Passwords do not match!");
            et_user_confirm_password.requestFocus();
            return;
        }

        if (!phonePattern.matcher(phone).matches()) {
            et_user_phone_num.setError("Phone number must start with '01' and have 11 digits.");
            et_user_phone_num.requestFocus();
            return;
        }

        // Show ProgressBar and start registration
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification();
                        }
                        Toast.makeText(Registration_activity.this, "Register successful. Verify your email!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Registration_activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
