package lk.cmb.fotnews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Screen extends AppCompatActivity {

    EditText username, pass, conpass, mail;
    Button submitbtn;
    FirebaseAuth mAuth;
    DatabaseReference databaseRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.username);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        conpass = findViewById(R.id.conpass);
        submitbtn = findViewById(R.id.submitbtn);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        submitbtn.setOnClickListener(v -> registerUser());

        togglePasswordVisibility(pass);
        togglePasswordVisibility(conpass);
    }

    private void registerUser() {
        String uname = username.getText().toString().trim();
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirmpass = conpass.getText().toString().trim();

        if (uname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            Toast.makeText(this, "Password must be at least 8 characters and contain at least one number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmpass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uname, email);
                        databaseRef.child(uid).setValue(user)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void togglePasswordVisibility(EditText editText) {
        editText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Right-side icon
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Show password
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_eye, 0);
                    } else {
                        // Hide password
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                    }
                    editText.setSelection(editText.getText().length()); // Keep cursor at end
                    return true;
                }
            }
            return false;
        });
    }

    public static class User {
        public String username, mail;

        public User() {}

        public User(String username, String mail) {
            this.username = username;
            this.mail = mail;
        }
    }
}
