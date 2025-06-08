package lk.cmb.fotnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_info_Screen extends AppCompatActivity {
    ImageView backArrow;
    TextView username, useremail, editName, editEmail;
    DatabaseReference userRef;
    FirebaseUser currentUser;
    Button editInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        // add username and email from  database
        username = findViewById(R.id.usernameinfo);
        useremail = findViewById(R.id.useremail);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("mail").getValue(String.class);

                    username.setText("Name : " + name);
                    useremail.setText("Email : " + email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //navigate to edit
        editInfo = findViewById(R.id.editinfo);

        editInfo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(User_info_Screen.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_user_info_edit_screen, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            EditText editName = dialogView.findViewById(R.id.editName);
            EditText editEmail = dialogView.findViewById(R.id.editEmail);
            Button okButton = dialogView.findViewById(R.id.okButton);
            ImageView backcross = dialogView.findViewById(R.id.backcross);

            backcross.setOnClickListener(v2 -> dialog.dismiss());

            editName.setText(username.getText().toString().replaceFirst("(?i)^Name *: *", "").trim());
            editEmail.setText(useremail.getText().toString().replaceFirst("(?i)^Email *: *", "").trim());

            okButton.setOnClickListener(view -> {
                String newName = editName.getText().toString().trim();
                String newEmail = editEmail.getText().toString().trim();

                // ✅ Save to Firebase
                FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("username").setValue(newName);
                FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("mail").setValue(newEmail);

                username.setText(newName);
                useremail.setText(newEmail);
                dialog.dismiss();
            });
        });
    }
}