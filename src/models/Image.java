package models;

/**
 * Created by shifar on 15/10/16.
 */
public class Image {
    private final String thumbImageUrl, imageUrl;
    private final int height, width;
    private final String name;

    public Image(String thumbImageUrl, String imageUrl, int height, int width) {
        this.thumbImageUrl = thumbImageUrl;
        this.imageUrl = imageUrl;
        this.height = height;
        this.width = width;
        this.name = imageUrl.replaceAll("[^A-Za-z0-9]", "");
    }

    public String getName() {
        return name;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "Image{" +
                "thumbImageUrl='" + thumbImageUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
