package lk.cmb.fotnews;

public class NewsItem {
    public String title, content;
    public String imageUrl;
    private long timestamp;

    // Needed for Firebase automatic mapping
    public NewsItem() {}

    // For manual use (e.g., adding test data)
    public NewsItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
