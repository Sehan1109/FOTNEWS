package lk.cmb.fotnews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.newsimg);
            titleView = itemView.findViewById(R.id.newstitle);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.titleView.setText(item.title);
        Glide.with(holder.imageView.getContext())
                .load(item.imageUrl)
                .into(holder.imageView);

        // click listener to open the Newsbody screen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Newsbody_Screen.class);
            intent.putExtra("title", item.title);
            intent.putExtra("imageUrl", item.imageUrl);
            intent.putExtra("content",item.content);
            intent.putExtra("time",item.getTime());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
