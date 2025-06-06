package lk.cmb.fotnews;

public class NewsItem {
    public String title;
    public String imageUrl;

    // Needed for Firebase automatic mapping
    public NewsItem() {}

    // For manual use (e.g., adding test data)
    public NewsItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
