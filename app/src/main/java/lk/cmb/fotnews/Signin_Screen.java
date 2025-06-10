package lk.cmb.fotnews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Signin_Screen extends AppCompatActivity {

    TextView btnSignup, forgotpass;
    FirebaseAuth mAuth;
    EditText emailEditText, passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.username);
        passEditText = findViewById(R.id.pass);
        Button loginbtn = findViewById(R.id.loginbtn);

        // 👁️ Add eye icon toggle for password field
        togglePasswordVisibility(passEditText);

        loginbtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passEditText.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email address and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signin_Screen.this, News_Screen.class);
                    intent.putExtra("username", "Sehan");
                    startActivity(intent);
                } else {
                    Exception e = task.getException();
                    if(e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(this, "Couldn't find account. Please sign up.", Toast.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Incorrect password. Try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        forgotpass = findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email to reset password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Reset email sent! Check your inbox.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnSignup = findViewById(R.id.signuplink);
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(Signin_Screen.this, Signup_Screen.class);
            startActivity(intent);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void togglePasswordVisibility(EditText editText) {
        editText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Right-side drawable
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_eye, 0);
                    } else {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                    }
                    editText.setSelection(editText.getText().length());
                    return true;
                }
            }
            return false;
        });
    }
}
