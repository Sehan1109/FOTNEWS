package lk.cmb.fotnews;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Screen extends AppCompatActivity {

    EditText username, pass, conpass, mail;
    Button submitbtn;
    Firebase mAuth;
    DatabaseReference databaseRef;

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
    }

    private void registerUser() {
        String uname = username.getText().toString().trim();
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirmpass = conpass.getText().toString().trim();

        if (!password.equals(confirmpass)) {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail, pass)
    }
}