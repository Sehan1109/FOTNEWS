package lk.cmb.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;


import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Newsbody_Screen extends AppCompatActivity {
        ImageView imageView, likeBtn;
        TextView titleView, contentView, likeCountView, timeView;
        boolean isLiked = false;
        int likeCount = 0;

        String postId;
        String userId;
        DatabaseReference likesRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_newsbody_screen);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Firebase auth
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Get intent data
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String imageUrl = intent.getStringExtra("imageUrl");
            String content = intent.getStringExtra("content");
            String timeString = intent.getStringExtra("time");

            // Generate a unique post ID from title + time (must match how you store news)
            postId = title + "_" + timeString; // You may use Firebase push ID instead in real apps

            // Setup UI
            imageView = findViewById(R.id.newsbodyimg);
            titleView = findViewById(R.id.newsbodytitle);
            contentView = findViewById(R.id.newscontent);
            likeBtn = findViewById(R.id.likebtn);
            likeCountView = findViewById(R.id.likecount);
            timeView = findViewById(R.id.timeView);
            ImageView backButton = findViewById(R.id.backButton);

            titleView.setText(title);
            contentView.setText(content);
            Glide.with(this).load(imageUrl).into(imageView);

            backButton.setOnClickListener(v -> finish());

            // Setup Firebase reference
            likesRef = FirebaseDatabase.getInstance().getReference("likes").child(postId);

            // Load existing like count and status
            loadLikeStatus();

            likeBtn.setOnClickListener(v -> {
                if (!isLiked) {
                    likePost();
                } else {
                    unlikePost();
                }
            });

            // Format time
            if (timeString != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date date = sdf.parse(timeString);
                    String relativeTime = getRelativeTime(date.getTime());
                    timeView.setText(relativeTime);
                } catch (Exception e) {
                    timeView.setText("Unknown time");
                }
            } else {
                timeView.setText("Unknown time");
            }
        }

        private void loadLikeStatus() {
            likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    likeCount = (int) snapshot.getChildrenCount();
                    likeCountView.setText(String.valueOf(likeCount));
                    if (snapshot.hasChild(userId)) {
                        isLiked = true;
                        likeBtn.setImageResource(R.drawable.likedbutton);
                    } else {
                        isLiked = false;
                        likeBtn.setImageResource(R.drawable.likebtn);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }

        private void likePost() {
            likesRef.child(userId).setValue(true);
            isLiked = true;
            likeBtn.setImageResource(R.drawable.likedbutton);
            likeCount++;
            likeCountView.setText(String.valueOf(likeCount));
        }

        private void unlikePost() {
            likesRef.child(userId).removeValue();
            isLiked = false;
            likeBtn.setImageResource(R.drawable.likebtn);
            likeCount--;
            likeCountView.setText(String.valueOf(likeCount));
        }

        private String getRelativeTime(long timeMillis) {
            long now = System.currentTimeMillis();
            long diff = now - timeMillis;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60) return "Just now";
            else if (minutes < 60) return minutes + " min ago";
            else if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
            else return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }