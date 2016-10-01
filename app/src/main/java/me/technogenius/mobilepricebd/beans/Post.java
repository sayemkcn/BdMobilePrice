package me.technogenius.mobilepricebd.beans;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class Post {
    private String title;
    private String price;
    private String imageIconUrl;
    private String detailsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageIconUrl() {
        return imageIconUrl;
    }

    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", imageIconUrl='" + imageIconUrl + '\'' +
                ", detailsUrl='" + detailsUrl + '\'' +
                '}';
    }
}
