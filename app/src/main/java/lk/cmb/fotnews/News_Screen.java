package lk.cmb.fotnews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class News_Screen extends AppCompatActivity {

    RecyclerView newsRecycler;
    NewsAdapter adapter;
    List<NewsItem> newsList = new ArrayList<>();
    TextView welcomemsg;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String username = getIntent().getStringExtra("username");

        welcomemsg = findViewById(R.id.welcomemsg);
        welcomemsg.setText("Hi, " + username);

        newsRecycler = findViewById(R.id.newsRecycler);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(newsList);
        newsRecycler.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("news").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NewsItem item = child.getValue(NewsItem.class);
                    newsList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_all) {
                    loadNews();
                    return true;
                } else if (id == R.id.nav_academics) {
                    loadAcademics();
                    return true;
                } else if (id == R.id.nav_sports) {
                    loadSports();
                    return true;
                } else if (id == R.id.nav_events) {
                    loadEvents();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadNews() {
        FirebaseDatabase.getInstance().getReference("news").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NewsItem item = child.getValue(NewsItem.class);
                    if (item != null) {
                        newsList.add(item); // ✅ add ALL news items regardless of type
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadAcademics() {
        FirebaseDatabase.getInstance().getReference("news").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NewsItem item = child.getValue(NewsItem.class);
                    if("Academics".equals(child.child("type").getValue(String.class))) {
                        newsList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadSports() {
        FirebaseDatabase.getInstance().getReference("news").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NewsItem item = child.getValue(NewsItem.class);
                    if("Sports".equals(child.child("type").getValue(String.class))) {
                        newsList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadEvents() {
        FirebaseDatabase.getInstance().getReference("news").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    NewsItem item = child.getValue(NewsItem.class);
                    if("Events".equals(child.child("type").getValue(String.class))) {
                        newsList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}