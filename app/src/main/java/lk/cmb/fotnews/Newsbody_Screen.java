package lk.cmb.fotnews;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Newsbody_Screen extends AppCompatActivity {
    ImageView imageView, likeBtn;
    TextView titleView, contentView, likeCountView, timeView;
    boolean isLiked = false;
    int likeCount = 0;

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

        //add news
        imageView = findViewById(R.id.newsbodyimg);
        titleView = findViewById(R.id.newsbodytitle);
        contentView = findViewById(R.id.newscontent);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String content = intent.getStringExtra("content");

        titleView.setText(title);
        contentView.setText(content);
        Glide.with(this).load(imageUrl).into(imageView);

        //back arrow button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        //like count
        likeBtn = findViewById(R.id.likebtn);
        likeCountView = findViewById(R.id.likecount);

        likeCountView.setText(String.valueOf(likeCount));

        likeBtn.setOnClickListener(v -> {
            if (!isLiked) {
                isLiked = true;
                likeCount++;
                likeBtn.setImageResource(R.drawable.likedbutton);
            } else {
                isLiked = false;
                likeCount--;
                likeBtn.setImageResource(R.drawable.likebtn);
            }
            likeCountView.setText(String.valueOf(likeCount));
        });

        //time
        timeView = findViewById(R.id.timeView);

        long timestamp = intent.getLongExtra("timestamp", 0);
        if (timestamp > 0) {
            String relativeTime = getRelativeTime(timestamp);
            timeView.setText(relativeTime);
        } else {
            timeView.setText("Unknown time");
        }
    }
    private String getRelativeTime(long timeMillis) {
        long now = System.currentTimeMillis();
        long diff = now - timeMillis;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " min ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }
}